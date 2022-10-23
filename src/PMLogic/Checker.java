package PMLogic;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import DatabaseAccess.DBFacade;
import FileSystemAccess.FileSystemFacade;

public class Checker {
private

	ArrayList<Event> E;
	ArrayList<ActivityInstance> AI;
	ArrayList<Activity> A;
	ArrayList<ProcessInstance> PI;
	ArrayList<ProcessModel> P;
	PetriNet PN;
	ArrayList<TRParameter> TRP;
	
public
	Checker(){}
	// This method fetches the contents of the database and place them in the private data structures
	void initializeDBDataStructures(DBFacade DBF) throws SQLException {
		P = DBF.getProcessList();
		A = DBF.getActivityList(P);
		PI = DBF.getProcessInstanceList(P);
		AI = DBF.getActivityInstanceList(A, PI);
		E = DBF.getEventList(PI,AI);
		
	}
	
	// This method fetches the contents in the files that contain what's necessary for applying the Conformance Checking algorithm
	void initializeFSDataStructures(FileSystemFacade FSF, ProcessModel PM) throws FileNotFoundException, IOException {
		PN = FSF.getPetriNet(PM, A);
		TRP = FSF.getTRParameterList(PM);
	}
	
	// This method performs the Conformance Checking through use of the Token Replay technique.
	public ArrayList<Trace> onlineConformanceChecking(boolean aware, String InputPath, String OutputPath) throws FileNotFoundException, IOException, SQLException{
		DBFacade DBF = new DBFacade();
		
		// The private data structures stored in the Database are fetched
		initializeDBDataStructures(DBF);
		
		// What's needed throughout the algorithm is here instantiated
		ArrayList<Trace> ReturnedTraces = new ArrayList<Trace>();
		ArrayList<Description> CDL = new ArrayList<Description>();
		ArrayList<ProcessModel> found_PM = new ArrayList<ProcessModel>();
		
		// In this cycle only one Process Model is considered per group of Process Instances
		for(int i = 0; i<PI.size(); i++) {
			boolean found = false;
			int k = 0;
			do {
				if(found_PM.size() != 0 && found_PM.get(k).getName().equals(PI.get(i).getP().getName()))
					found = true;
				k++;
			}while(found == false && k<found_PM.size());
			if(found == false)
				found_PM.add(PI.get(i).getP());
		}
		
		// This is the main cycle. It cycles over all the Process Models found in the Database.
		for(int i = 0; i<found_PM.size(); i++) {
			
			FileSystemFacade FSF = FileSystemFacade.getInstance(InputPath, OutputPath);
			// The private data structures stored in the files are fetched
			initializeFSDataStructures(FSF,found_PM.get(i));
			
			// This ArrayList of ArrayLists will contain the base traces and the extended traces
			ArrayList<Trace> ObtainedTraces;
				
			// The last transition must be taken into account.
			ObtainedTraces = buildTraces();
			
			/*
			for(int j=0;j<ObtainedTraces.size();j++) {
				for(int k=0; k<ObtainedTraces.get(j).getAI().size(); k++) {
					System.out.print(ObtainedTraces.get(j).getAI().get(k).getA().getName() + " ");
				}
				System.out.println();
			}
			*/
			
			// To the ArrayList of Descriptions the results of the tokenReplay() method are considered.
			// The tokenReplay method applies the Token Replay technique and infers a fitness parameter.
			
			CDL.add(tokenReplay(ObtainedTraces,aware));
			
			System.out.println("Fitness: " + CDL.get(i).getFitness());
			/*
			for(int j=0;j<CDL.get(i).getT().size();j++) {
				System.out.println("Anomalous trace " + j + ": ");
				for(int k=0; k<CDL.get(i).getT().get(j).getAI().size(); k++)
					System.out.print(CDL.get(i).getT().get(j).getAI().get(k).getA().getName()+ " ");
				System.out.println();
				
				int[] ActID = new int[CDL.get(i).getT().get(j).getAI().size()];
				for(int k=0; k<ActID.length; k++)
					ActID[k] = CDL.get(i).getT().get(j).getAI().get(k).getID();
				DBF.insertAnomalousTrace(ActID, CDL.get(i).getFitness());
				
			}
			*/
			for(int k=0; k<PN.getTransitions().size(); k++) {
				System.out.println("Number of anomalies for transition " + PN.getTransitions().get(k).getName() + ": " + CDL.get(i).getAnomalyCount().get(PN.getTransitions().get(k).getName()));
			}
			int[] ActID;
			int CaseID;
			for(int j=0; j<ObtainedTraces.size(); j++) {
				ActID = new int[ObtainedTraces.get(j).getAI().size()];
				CaseID = ObtainedTraces.get(j).getAI().get(0).getPI().getCaseID();
				for(int k=0; k<ObtainedTraces.get(j).getAI().size(); k++)
					ActID[k] = ObtainedTraces.get(j).getAI().get(k).getID();
				//DBF.deleteEvent(ActID, CaseID);
			}
			FSF.writeDiagnostics(CDL.get(i));
		}
		for(int i=0; i<CDL.size(); i++)
			for(int j=0; j<CDL.get(i).getT().size(); j++) {
				ReturnedTraces.add(CDL.get(i).getT().get(j));
				
			}
		DBF.closeConnection();
		return ReturnedTraces;
	}
	
	// This method performs the so-called Token Replay technique.
	Description tokenReplay(ArrayList<Trace> ProcessTraces, boolean Aware) {
		HashMap<String, Integer> AnomalyCount = new HashMap<String, Integer>();
		for(int j=0; j<PN.getTransitions().size(); j++) {
			AnomalyCount.put(PN.getTransitions().get(j).getName(), 0);
		}
		/*for(int i=0; i<ProcessTraces.size(); i++) {
			for(int j=0;j<ProcessTraces.get(i).getAI().size(); j++)
				System.out.print(ProcessTraces.get(i).getAI().get(j).getName() + " ");
		}
		System.out.println();*/
		
		// The data structures used are here defined. The ReplayParameters variable is initialized to
		// hold the p, c, m and r variables. However, one should note that there are two of these variables:
		// 		1) RPGlobal considers *all* the p,c,m and r calculated for each trace
		// 		2) RPLocal considers the p,c,m and r calculated for the single trace.
		Description DL = new Description();
		float Fitness = (float) 0.0;
		ReplayParameters RPGlobal = new ReplayParameters();
		ReplayParameters RPLocalTemp;
		String ActResource;
		ArrayList<Activity> UnfiredActivities = new ArrayList<Activity>();
		// The Token Replay techniques is applied for each trace.
		for(int i=0; i<ProcessTraces.size(); i++) {
			
			ReplayParameters RPLocal = new ReplayParameters(1,0,0,0,false);
			boolean end = false;
			// The Token Replay technique fires each activity found in the trace.
			for(int j=0; j<ProcessTraces.get(i).getAI().size() && !end;j++) {
				
				Activity AToFire = null;
				// The activity to fire in the Petri Net is recovered from the trace.
				for(int k=0; k<A.size(); k++)
					if(A.get(k).getName().equals(ProcessTraces.get(i).getAI().get(j).getA().getName()))
						AToFire = A.get(k);
				// The activity is fired on the Petri Net. The Petri Net itself updates the parameters
				
				
				RPLocalTemp = new ReplayParameters(RPLocal);
				
				RPLocal = PN.fire(AToFire, RPLocal);
				
				
				
				
				if(RPLocal.getM()-RPLocalTemp.getM()>0) {
					
					
					UnfiredActivities = PN.getUnfiredActivities();
					/*
					for(int k = 0; k<UnfiredActivities.size(); k++) {
						System.out.println("Unfired activity: " + UnfiredActivities.get(k).getName());
					}
					System.out.println();
					
					for(int k=0; k<UnfiredActivities.size(); k++) {
						for(int l=0; l<A.size(); l++) {
							if(UnfiredActivities.get(k).getName().equals(A.get(l).getName())) {
								for(int s=0; s<TRP.size(); s++) {
									if(TRP.get(s).getResource().equals(A.get(l).getResource())) {
										TRP.get(s).setCounter(TRP.get(s).getCounter()+1);
									}
								}
							}
						}
					}*/
				}
				
				
				//System.out.println("Iteration " + j + " for activity " + AToFire.getName() + ": Local p: "+RPLocal.getP() + " Local c: "+RPLocal.getC() + " Local m: "+RPLocal.getM() + "Local r: "+RPLocal.getR());
				
				end = RPLocal.isEnd(); // Check if the Token Replay has ended for the current trace
			}
			// The anomalous traces are here considered and added to the Description variable that was defined before.
			if(RPLocal.getM()!=0 || RPLocal.getR()!= 0) {
				boolean found = false;
				int k = 0;
				do {
					if(DL.getT().size() != 0 && DL.getT().get(k).equals(ProcessTraces.get(i))) {
						found = true;
					}
					k++;
				}while(found == false && k<DL.getT().size());
				if(found == false)
					DL.getT().add(ProcessTraces.get(i));
			}
			//ArrayList<Activity> UnfiredActivities = PN.getUnfiredActivities();
			for(int j=0;j<UnfiredActivities.size(); j++) {
				AnomalyCount.put(UnfiredActivities.get(j).getName(), AnomalyCount.get(UnfiredActivities.get(j).getName())+1);
			}
			
			// RPGlobal is updated.
			RPGlobal.setC(RPGlobal.getC()+RPLocal.getC());
			RPGlobal.setP(RPGlobal.getP()+RPLocal.getP());
			RPGlobal.setM(RPGlobal.getM()+RPLocal.getM());
			RPGlobal.setR(RPGlobal.getR()+RPLocal.getR());
			// The Petri Net is re-initialized.
			PN.initializeMarking();
			PN.resetUnfiredActivities();
		}
		// The Fitness parameter is updated.
		/*
		 * Fitness(sigma, N, lambda_i) = (1/2(1-m/c)+1/2(1-r/p))(a/(1+sum_i lambda_i*m_i)+(1-a))
		 */
		float Second_factor_den_sum = (float) 1.0;
		for(int i=0; i<TRP.size(); i++) {
			Second_factor_den_sum += (float)((float)TRP.get(i).getCounter()*(float)TRP.get(i).getValue());
		}
		float Second_factor = (float)(Aware ? 0 : 1)/((float)Second_factor_den_sum) + 1-(float)(Aware ? 0 : 1);
		//System.out.println("Second factor:" + Second_factor);
		
		float First_factor = ((float)1/2 * (1-((float)RPGlobal.getM()/((float)RPGlobal.getC()))) + (float)1/2 * (1-((float)RPGlobal.getR()/((float)RPGlobal.getP()))));
		//System.out.println("First factor:" + First_factor);
		Fitness = (float)(First_factor * Second_factor);
		
		DL.setFitness(Fitness);
		DL.setAnomalyCount(AnomalyCount);
		
		return DL;
	}
	
	// This method build the Base Traces and the Extended Traces.
	ArrayList<Trace> buildTraces() {
		ArrayList<Trace> BuiltTraces = new ArrayList<Trace>();

		
		for(int i=0; i<PI.size(); i++)
		{
			// The trace, for each Process Instance, is obtained through ordering the events using the 
			// orderEvents() method
			Trace ExtractedTrace = orderEvents(PI.get(i).getCaseID());
			
			// The trace is checked for completeness through the isComplete() method.
			if(isComplete(ExtractedTrace)) {
				BuiltTraces.add(ExtractedTrace);
				// The Modified Trace is added to the list of traces.
				}
		}
		return BuiltTraces;
		
	}

	// Checks for the trace completeness.
	boolean isComplete(Trace T) {
		boolean complete = false;
		if(T.getAI().size() != 0) {
			ActivityInstance LastActivity = T.getAI().get(T.getAI().size()-1);
			//System.out.println(PN.getLastTransition());
			if(LastActivity.getA().getName().equals(PN.getLastTransition())) {
				complete = true;
			}
		}
		return complete;
		
	}
	
	// Orders the events for a particular Process Instance
	Trace orderEvents(int CaseID) {
		Trace BuiltTrace = new Trace();
		// This will be the Event List to order.
		ArrayList<Event> EventListToOrder = new ArrayList<Event>();
		// A copy of the Event List is made. This is to not lose the references for the original list.
		ArrayList<Event> TempEvent = new ArrayList<Event>(E);
		boolean found = false;
		
		do {
			
			found = false;
			
			// The copy of the Event List is cycled through: if the CaseID matches with what's expected
			// then the event is added to the Event List to order and is removed from the copy (this is why the copy was made)
			for(int i = 0; i<TempEvent.size(); i++)
			{
				if(TempEvent.get(i).getPI().getCaseID() == CaseID) {
					found = true;
					EventListToOrder.add(TempEvent.get(i));
					TempEvent.remove(i);
				}
				
			}
		} while(found == true);
		
		// Through the sort method, which simply implements an Insertion Sort, the events are sorted by their timestamp
		EventListToOrder = sort(EventListToOrder);
		
		// An ArrayList of ActivityInstance is made and built with the activity instances recovered from
		// the Event List to order.
		ArrayList<ActivityInstance> OrderedAIArray = new ArrayList<ActivityInstance>();
		for(int i=0; i<EventListToOrder.size(); i++) {
			OrderedAIArray.add(EventListToOrder.get(i).getAI());
		}
		// The Built Trace is populated with the recovered Activity Instance ArrayList.
		BuiltTrace.setAI(OrderedAIArray);
		
		return BuiltTrace;
	}
	
	
	
	// This method sorts the events by their timestamps
	ArrayList<Event> sort(ArrayList<Event> EventListToOrder){
		int n = EventListToOrder.size();
	    for (int i = 1; i < n; ++i) {
	        Event key = EventListToOrder.get(i);
	        int j = i - 1;
	        while (j >= 0 && EventListToOrder.get(j).getT() > key.getT()) {
	        	EventListToOrder.set(j+1, EventListToOrder.get(j));
	            j = j - 1;
	        }
	        EventListToOrder.set(j + 1,key);
	    }
	    
	    return EventListToOrder;
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException, SQLException {
		Checker C = new Checker();
		boolean aware = true;
		ArrayList<Trace> NonConformantTraces = C.onlineConformanceChecking(aware, args[0], args[1]);
	}
	
	
	
}
