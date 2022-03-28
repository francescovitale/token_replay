package PMLogic;
import java.util.ArrayList;
import java.util.HashMap;


public class PetriNet {
	
class UnfiredActivity{
	Activity A;
	boolean taken;
	
	UnfiredActivity(Activity Ain){
		A = Ain;
		taken = false;
	}
	
	boolean isTaken() {
		return taken;
	}
	
	void setTaken(boolean t) {
		taken = t;
	}
	
	Activity getActivity() {
		return A;
	}
}
	
private
	ProcessModel P;
	ArrayList<String> Places;
	ArrayList<Activity> Transitions;
	BiHashMap<String,String,Boolean> PT;
	BiHashMap<String,String,Boolean> TP;
	HashMap<String,Integer> Marking;
	HashMap<String,Integer> DefaultMarking;
	String TerminatingEvent;
	
	static HashMap<String,Integer> UA = new HashMap<String,Integer>();
public
	PetriNet() {
		P = null;
		Places = null;
		Transitions = null;
		PT = null;
		TP = null;
		Marking = null;
		DefaultMarking = null;
		TerminatingEvent = null;
		UA = null;
	}

	public PetriNet(ProcessModel P_in, ArrayList<String> Places_in, ArrayList<Activity> Transitions_in, BiHashMap<String,String,Boolean> PT_in, BiHashMap<String,String,Boolean> TP_in,
			HashMap<String,Integer> DefaultMarking_in, String TerminatingEvent_in) {
		P = new ProcessModel(P_in);
		Places = new ArrayList<String>(Places_in);
		Transitions = new ArrayList<Activity>(Transitions_in);
		PT = PT_in;
		TP = TP_in;
		DefaultMarking = new HashMap<String,Integer>(DefaultMarking_in);
		Marking = new HashMap<String,Integer>(DefaultMarking_in);
		TerminatingEvent = TerminatingEvent_in;
		for(int i=0;i<Transitions.size();i++)
			UA.put(Transitions.get(i).getName(), 0);
	}

	public PetriNet(PetriNet PN){
		P = new ProcessModel(PN.getP());
		Places = PN.Places;
		Transitions = new ArrayList<Activity>(PN.getTransitions());
		PT = new BiHashMap<String,String,Boolean>(PN.getPT());
		TP = new BiHashMap<String,String,Boolean>(PN.getTP());
		Marking = new HashMap<String,Integer>(PN.getMarking());
		DefaultMarking = new HashMap<String,Integer>(PN.getDefaultMarking());
		TerminatingEvent = PN.getTerminatingEvent();
	}

ReplayParameters fire(Activity A, ReplayParameters P) {
		
		int missingTokens = countM(A.getName(),false);
		
		if(P.getM()<P.getM()+missingTokens) {
			
			ArrayList<Activity> EnabledActivities = getEnabledActivities();
			
			for(int i=0; i<EnabledActivities.size(); i++) {
				UA.put(EnabledActivities.get(i).getName(), UA.get(EnabledActivities.get(i).getName())+1);
			}
		}
		
		P.setM(P.getM() + countM(A.getName(),true));
		P.setC(P.getC() + countC(A.getName()));
		P.setP(P.getP() + countP(A.getName()));
		if(Marking.get(Places.get(Places.size()-1)) != 0) {
			P.setC(P.getC()+1);
			Marking.put(Places.get(Places.size()-1), Marking.get(Places.get(Places.size()-1))-1);
			P.setEnd(true);
			P.setR(countR());
		}
		
		return P;
	};
	
	private ArrayList<Activity> getEnabledActivities() {
		ArrayList<Activity> EnabledActivities = new ArrayList<Activity>();
		int[] MarkingVector = new int[Places.size()];
		int[] NeededMarking = new int[Places.size()];
		for(int i=0; i<MarkingVector.length; i++)
			MarkingVector[i] = 0;
		for(int i=0; i<Places.size(); i++) {
			MarkingVector[i] = Marking.get(Places.get(i));
		}
		for(int i=0; i<Transitions.size(); i++) {
			for(int j=0; j<Places.size(); j++) {
				NeededMarking[j] =  PT.get(Places.get(j), Transitions.get(i).getName()) ? 1 : 0;
			}
			boolean canFire = true;
			for(int j=0; j<Places.size(); j++) {
				if(MarkingVector[j] < NeededMarking[j])
					canFire = false;
			}
			if(canFire) {
				EnabledActivities.add(Transitions.get(i));
			}
		}
		return EnabledActivities;
	}
	
	public HashMap<String,Integer> getUnfiredActivities(){
		HashMap<String,Integer> ReturnedMap;
		
		ReturnedMap = new HashMap<String,Integer>(UA);
		
		return ReturnedMap;
	}

	int countC(String ActivityName) {
		int c = 0;
		ArrayList<String> PlacesToReset = new ArrayList<String>();
		for(int i=0; i<Places.size(); i++) {
			if(PT.get(Places.get(i), ActivityName) == true) {
				c++;
				PlacesToReset.add(Places.get(i));
			}
		}
		updateMarking(null, PlacesToReset);
		return c;
	}
	
	int countP(String ActivityName) {
		int p = 0;
		ArrayList<String> PlacesToSet = new ArrayList<String>();
		for(int i=0; i<Places.size(); i++) {
			if(TP.get(Places.get(i), ActivityName) == true) {
				p++;
				PlacesToSet.add(Places.get(i));
			}
		}
		updateMarking(PlacesToSet, null);
		return p;
	}
	
	int countM(String ActivityName, boolean clear) {
		int m = 0;
		ArrayList<String> PlacesToSet = new ArrayList<String>();
		
		for(int i=0; i<Places.size(); i++) {
			if(PT.get(Places.get(i), ActivityName) == true && Marking.get(Places.get(i)) == 0) {
				//System.out.println("Activity to fire: " + ActivityName + ", Place considered: " + Places.get(i) +", PT value: " + PT.get(Places.get(i), ActivityName) 
				//+ ", Actual marking value: " + Marking.get(Places.get(i)));
				m++;
				PlacesToSet.add(Places.get(i));
			}
		}
		
		if(clear == true)
			updateMarking(PlacesToSet, null);
		return m;
	}
	
	int countR() {
		int r = 0;
		for(int i=0; i<Places.size(); i++) {
			r = r + Marking.get(Places.get(i));
		}
		return r;
	}
	
	void initializeMarking() {
		Marking = new HashMap<String, Integer>(DefaultMarking);
	};
	void updateMarking(ArrayList<String> PlacesToSet, ArrayList<String> PlacesToReset) {
		if(PlacesToSet != null) {
			for(int i=0; i<PlacesToSet.size(); i++) {
				Marking.put(PlacesToSet.get(i), Marking.get(PlacesToSet.get(i))+1);
			}
		}
		if(PlacesToReset != null) {
			for(int i=0; i<PlacesToReset.size(); i++) {
				Marking.put(PlacesToReset.get(i), Marking.get(PlacesToReset.get(i))-1);
			}
		}
	}
	
	void resetUnfiredActivities() {
		for(int i=0;i<Transitions.size();i++)
			UA.put(Transitions.get(i).getName(), 0);
	}
	
	
	
	public ProcessModel getP() {
		return P;
	}
	public void setP(ProcessModel p) {
		P = p;
	}
	public ArrayList<String> getPlaces() {
		return Places;
	}
	public void setPlaces(ArrayList<String> places) {
		Places = places;
	}
	public ArrayList<Activity> getTransitions() {
		return Transitions;
	}
	public void setTransitions(ArrayList<Activity> transitions) {
		Transitions = transitions;
	}
	public BiHashMap<String, String, Boolean> getPT() {
		return PT;
	}
	public void setPT(BiHashMap<String, String, Boolean> pT) {
		PT = pT;
	}
	public BiHashMap<String, String, Boolean> getTP() {
		return TP;
	}
	public void setTP(BiHashMap<String, String, Boolean> tP) {
		TP = tP;
	}
	public HashMap<String, Integer> getMarking() {
		return Marking;
	}
	public void setMarking(HashMap<String, Integer> marking) {
		Marking = marking;
	}

	public HashMap<String, Integer> getDefaultMarking() {
		return DefaultMarking;
	}

	public void setDefaultMarking(HashMap<String, Integer> defaultMarking) {
		DefaultMarking = defaultMarking;
	}

	public String getTerminatingEvent() {
		return TerminatingEvent;
	}

	public void setTerminatingEvent(String terminatingEvent) {
		TerminatingEvent = terminatingEvent;
	};
}
