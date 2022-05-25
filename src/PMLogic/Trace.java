package PMLogic;
import java.util.ArrayList;

public class Trace {
private
	int ID;
	ArrayList<ActivityInstance> AI;
	ArrayList<String> Timestamps;
	ArrayList<String> LifeCycleStages;
	Description CD;
public
	Trace() {
		ID = -1;
		AI = new ArrayList<ActivityInstance>();
		LifeCycleStages = new ArrayList<String>();
		CD = null;
		Timestamps = null;
	}

	Trace(ArrayList<ActivityInstance> AI_in, ArrayList<String> Timestamps_in, ArrayList<String> LifeCycleStages_in) {
		ID = -1;
		AI = AI_in;
		Timestamps = Timestamps_in;
		LifeCycleStages = LifeCycleStages_in;
		CD = null;
	}
	
	public Trace(int ID_in){
		ID = ID_in;
		AI = new ArrayList<ActivityInstance>();
		Timestamps = new ArrayList<String>();
		LifeCycleStages = new ArrayList<String>();
		CD = null;
	}
	public Trace(int ID_in, ArrayList<ActivityInstance> AI_in, Description D, ArrayList<String> Timestamps_in, ArrayList<String> LifeCycleStages_in){
		ID = ID_in;
		AI = new ArrayList<ActivityInstance>(AI_in);
		Timestamps = new ArrayList<String>(Timestamps_in);
		LifeCycleStages = new ArrayList<String>(LifeCycleStages_in);
		CD = D;
	}

	Trace(Trace T){
		ID = T.getID();
		AI = new ArrayList<ActivityInstance>(T.getAI());
		Timestamps = new ArrayList<String>(T.getTimestamps());
		LifeCycleStages = new ArrayList<String>(T.getLifeCycleStages());
		CD = T.getCD();
	}

	public Trace(int aTID, ArrayList<ActivityInstance> aIListPerTrace, Description description) {
		ID = aTID;
		AI = new ArrayList<ActivityInstance>(aIListPerTrace);
		CD = new Description(description);
	}

	public ArrayList<ActivityInstance> getAI() {
		return AI;
	}

	public void setAI(ArrayList<ActivityInstance> aI) {
		AI = aI;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((AI == null) ? 0 : AI.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object T) {
		if(T == null)
			return false;
		if(((Trace) T).getAI().size() != AI.size())
			return false;
		boolean equal = true;
		for (int i = 0; i<((Trace) T).getAI().size(); i++) {
			if(!((Trace) T).getAI().get(i).getA().getName().equals(AI.get(i).getA().getName()))
				equal = false;
		}
		return equal;
						
		
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public Description getCD() {
		return CD;
	}

	public void setCD(Description cD) {
		CD = cD;
	}

	public ArrayList<String> getTimestamps() {
		return Timestamps;
	}

	public void setTimestamps(ArrayList<String> timestamps) {
		Timestamps = timestamps;
	}

	public ArrayList<String> getLifeCycleStages() {
		return LifeCycleStages;
	}

	public void setLifeCycleStages(ArrayList<String> lifeCycleStages) {
		LifeCycleStages = lifeCycleStages;
	}

	
	
	
}
