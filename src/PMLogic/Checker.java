package PMLogic;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Queue;

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
	public ArrayList<Trace> ConformanceChecking(boolean aware) throws FileNotFoundException, IOException, SQLException{
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
			FileSystemFacade FSF = FileSystemFacade.getInstance();
			// The private data structures stored in the files are fetched
			initializeFSDataStructures(FSF,found_PM.get(i));
			
			
			// This ArrayList of ArrayLists will contain the base traces and the extended traces
			ArrayList<Trace> ObtainedTraces;
				
			// The last transition must be taken into account.
			ObtainedTraces = buildTraces();
			/*
			System.out.println("Base trace n." + i);
			for(int j=0; j<ObtainedTraces.get(0).size(); j++) {
				for(int k=0; k<ObtainedTraces.get(0).get(j).getAI().size(); k++) {
					System.out.print(ObtainedTraces.get(0).get(j).getAI().get(k).getA().getName()+" ");
				}
				System.out.println();
			}
			*/
			
			// To the ArrayList of Descriptions the results of the tokenReplay() method are considered.
			// The tokenReplay method applies the Token Replay technique and infers a fitness parameter.
			CDL.add(tokenReplay(ObtainedTraces,aware));
			
			System.out.println("Fitness: " + CDL.get(i).getFitness());
			for(int j=0;j<CDL.get(i).getAnomalousTraces().size();j++) {
				System.out.println("Anomalous trace " + j + ": ");
				for(int k=0; k<CDL.get(i).getAnomalousTraces().get(j).getAI().size(); k++)
					System.out.print(CDL.get(i).getAnomalousTraces().get(j).getAI().get(k).getA().getName()+ " ");
				System.out.println();
				int[] ActID = new int[CDL.get(i).getAnomalousTraces().get(j).getAI().size()];
				for(int k=0; k<ActID.length; k++)
					ActID[k] = CDL.get(i).getAnomalousTraces().get(j).getAI().get(k).getID();
				DBF.insertAnomalousTrace(ActID, CDL.get(i).getFitness());
				
			}
			
			ArrayList<String> TransitionsList = new ArrayList<String>();
			for(int k=0;k<PN.getTransitions().size();k++) {
				TransitionsList.add(PN.getTransitions().get(k).getName());
			}
			
			FSF.writeDiagnostics(CDL.get(0),TransitionsList);
			
			/*
			int[] ActID;
			int CaseID;
			for(int j=0; j<ObtainedTraces.size(); j++) {
				ActID = new int[ObtainedTraces.get(j).getAI().size()];
				CaseID = ObtainedTraces.get(j).getAI().get(0).getPI().getCaseID();
				for(int k=0; k<ObtainedTraces.get(j).getAI().size(); k++)
					ActID[k] = ObtainedTraces.get(j).getAI().get(k).getID();
				DBF.deleteEvent(ActID, CaseID);
			}*/
		}
		System.out.println("*** Trace-level performance statistics ***");
		System.out.println("Trace ACET: " + CDL.get(0).getTraceACET() +"\nTrace BCET: " + CDL.get(0).getTraceBCET() + "\nTrace WCET: " + CDL.get(0).getTraceWCET());
		System.out.println("*** Transition-level performance statistics ***");
		for(int i=0;i<PN.getTransitions().size();i++) {
			String TransitionName = PN.getTransitions().get(i).getName();
			System.out.println("Statistics for transition: " + TransitionName + "\nTransition ACET: " + CDL.get(0).getTransitionsACETs().get(TransitionName) +
					"\nTransition BCET: " + CDL.get(0).getTransitionsBCETs().get(TransitionName) + "\nTransition WCET: " + CDL.get(0).getTransitionsWCETs().get(TransitionName));
		}
		
		
		
		for(int i=0; i<CDL.size(); i++)
			for(int j=0; j<CDL.get(i).getAnomalousTraces().size(); j++) {
				ReturnedTraces.add(CDL.get(i).getAnomalousTraces().get(j));
				
			}
		/*
		for(int i=0; i<TRP.size(); i++) {
			System.out.println("Resource: " + TRP.get(i).getResource() + " Counter: " + TRP.get(i).getCounter());
		}*/
		/*for(int i=0; i<ReturnedTraces.size(); i++)
			for(int j=0; j<ReturnedTraces.get(i).getAI().size(); j++)
				System.out.println(ReturnedTraces.get(i).getAI().get(j));*/
		DBF.closeConnection();
		return ReturnedTraces;
	}
	
	// This method performs the so-called Token Replay technique.
	Description tokenReplay(ArrayList<Trace> ProcessTraces, boolean Aware) {
		
		
		// The data structures used are here defined. The ReplayParameters variable is initialized to
		// hold the p, c, m and r variables. However, one should note that there are two of these variables:
		// 		1) RPGlobal considers *all* the p,c,m and r calculated for each trace
		// 		2) RPLocal considers the p,c,m and r calculated for the single trace.
		
		Description DL = new Description();
		
		float Fitness = (float) 0.0;
		ArrayList<Trace> AnomalousTraces = new ArrayList<Trace>();
		double TraceBCET = Math.round(Double.MAX_VALUE*1000000.0)/1000000.0;
		double TraceACET = (double)0.0;
		double TraceWCET = -Math.round(Double.MAX_VALUE*1000000.0)/1000000.0;
		HashMap<String,Double> TransitionsBCETs = new HashMap<String, Double>();
		HashMap<String,Double> TransitionsACETs = new HashMap<String, Double>();
		HashMap<String,Double> TransitionsWCETs = new HashMap<String, Double>();
		
		for(int i=0;i<PN.getTransitions().size();i++) {
			TransitionsBCETs.put(PN.getTransitions().get(i).getName(), Math.round(Double.MAX_VALUE*1000000.0)/1000000.0);
			TransitionsWCETs.put(PN.getTransitions().get(i).getName(), -Math.round(Double.MAX_VALUE*1000000.0)/1000000.0);
			TransitionsACETs.put(PN.getTransitions().get(i).getName(), (double)0.0);
		}
		
		ArrayList<Double> TracesExecutionTimes = new ArrayList<Double>();
		ArrayList<HashMap<String,ArrayList<Double>>> TransitionsExecutionTimes = new ArrayList<HashMap<String,ArrayList<Double>>>();
		
		ReplayParameters RPGlobal = new ReplayParameters();
		ReplayParameters RPLocalTemp;
		
		// The Token Replay techniques is applied for each trace.
		for(int i=0; i<ProcessTraces.size(); i++) {
			
			System.out.println("Trace n. : " + (i+1));
			
			/* Here are the initializations for each trace */
			LinkedList<HashMap<String,String>> StartTransitionsList = new LinkedList<HashMap<String,String>>();
			double TraceExecutionTime = (double)0.0;
			HashMap<String,ArrayList<Double>> TransitionsExecutionTime = initializeTransitionsExecutionTime();
			ReplayParameters RPLocal = new ReplayParameters(1,0,0,0,false);
			boolean end = false;
			double StartingTime = Double.parseDouble(ProcessTraces.get(i).getTimestamps().get(0));
			
			
			// The Token Replay technique fires each activity found in the trace.
			for(int j=0; j<ProcessTraces.get(i).getAI().size() && !end;j++) {
				
				String CorrespondingStartingTransitionTimestamp = null;
				String TraceCurrentActivityName = ProcessTraces.get(i).getAI().get(j).getA().getName();
				String TraceCurrentActivityTimestamp = ProcessTraces.get(i).getTimestamps().get(j);
				String TraceCurrentActivityLifeCycleStage = ProcessTraces.get(i).getLifeCycleStages().get(j);
				HashMap<String,String> ActivityInfo = new HashMap<String,String>();
				ActivityInfo.put("ActivityName", TraceCurrentActivityName);
				ActivityInfo.put("ActivityTimestamp", TraceCurrentActivityTimestamp);
				ActivityInfo.put("ActivityLifeCycleStage", TraceCurrentActivityLifeCycleStage);
				RPLocalTemp = new ReplayParameters(RPLocal);
				
				Activity AToFire = null;
				// The activity to fire in the Petri Net is recovered from the trace.
				for(int k=0; k<A.size(); k++)
					if(A.get(k).getName().equals(TraceCurrentActivityName))
						AToFire = A.get(k);
				
				
				if(TraceCurrentActivityLifeCycleStage.equals("start")) {
					StartTransitionsList.add(ActivityInfo);
					RPLocal = PN.fire(AToFire, RPLocal,TraceCurrentActivityLifeCycleStage);
				}
				else if(TraceCurrentActivityLifeCycleStage.equals("end")) {
					CorrespondingStartingTransitionTimestamp = findCorrespondingStartingTransitionTimestamp(StartTransitionsList, TraceCurrentActivityName);
					if(CorrespondingStartingTransitionTimestamp != null) {
							Double TransitionExecutionTime = Double.parseDouble(TraceCurrentActivityTimestamp) - Double.parseDouble(CorrespondingStartingTransitionTimestamp);
							System.out.println("Activity " + TraceCurrentActivityName + " Execution time:" + TransitionExecutionTime);
							TransitionsExecutionTime.get(TraceCurrentActivityName).add(TransitionExecutionTime);
					}
					RPLocal = PN.fire(AToFire, RPLocal,TraceCurrentActivityLifeCycleStage);
				}
				
				
				if(RPLocal.getM()-RPLocalTemp.getM()>0) {
					
					ArrayList<Activity> UnfiredActivities;
					UnfiredActivities = PN.getUnfiredActivities();
					/*for(int k = 0; k<UnfiredActivities.size(); k++) {
						System.out.println("Unfired activity: " + UnfiredActivities.get(k).getName());
					}*/
					
				
				}
				
				
				//System.out.println("Iteration " + j + " for activity " + AToFire.getName() + ": Local p: "+RPLocal.getP() + " Local c: "+RPLocal.getC() + " Local m: "+RPLocal.getM() + "Local r: "+RPLocal.getR());
				
				end = RPLocal.isEnd(); // Check if the Token Replay has ended for the current trace
				if(end) TraceExecutionTime = Double.parseDouble(ProcessTraces.get(i).getTimestamps().get(j)) - StartingTime;
			}
			
			System.out.println("Produced tokens: " + RPLocal.getP() + " Consumed tokens:" + RPLocal.getC() + " Missing tokens: " + RPLocal.getM() + " Remaining tokens: " + RPLocal.getR());
			// The anomalous traces are here considered and added to the Description variable that was defined before.
			if(RPLocal.getM()!=0 || RPLocal.getR()!= 0) {
				/* The following commented part is wrong */
				/*
				boolean found = false;
				int k = 0;
				do {
					if(AnomalousTraces.size() != 0 && AnomalousTraces.get(k).equals(ProcessTraces.get(i))) {
						found = true;
					}
					k++;
				}while(found == false && k<DL.getAnomalousTraces().size());
				if(found == false)
				*/
				AnomalousTraces.add(ProcessTraces.get(i));
			}
			// RPGlobal is updated.
			RPGlobal.setC(RPGlobal.getC()+RPLocal.getC());
			RPGlobal.setP(RPGlobal.getP()+RPLocal.getP());
			RPGlobal.setM(RPGlobal.getM()+RPLocal.getM());
			RPGlobal.setR(RPGlobal.getR()+RPLocal.getR());
			// The Petri Net is re-initialized.
			PN.initializeMarking();
			PN.resetUnfiredActivities();
			
			System.out.println("Trace execution time: " + TraceExecutionTime);
			TracesExecutionTimes.add(TraceExecutionTime);
			TransitionsExecutionTimes.add(TransitionsExecutionTime);
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
		
		float First_factor = ((float)1/2 * (1-((float)RPGlobal.getM()/((float)RPGlobal.getC()))) + (float)1/2 * (1-((float)RPGlobal.getR()/((float)RPGlobal.getP()))));
		
		Fitness = (float)(First_factor * Second_factor);
		Fitness = (float) (Math.round(Fitness*1000000.0)/1000000.0);
		
		/* Trace-level statistics calculation code segment */
		
		for(int i=0;i<TracesExecutionTimes.size();i++) {
			TraceACET += TracesExecutionTimes.get(i);
			if(TracesExecutionTimes.get(i) < TraceBCET) {
				TraceBCET = Math.round(TracesExecutionTimes.get(i)*1000000.0)/1000000.0;
				
			}
			else if(TracesExecutionTimes.get(i) > TraceWCET) {
				TraceWCET = Math.round(TracesExecutionTimes.get(i)*100000.0)/100000.0;
			}
		}
		TraceACET = TraceACET/TracesExecutionTimes.size();
		TraceACET = Math.round(TraceACET*100000.0)/100000.0;
		
		/* Transition-level statistics calculation code segment */
		HashMap<String, Integer> TempCounter = new HashMap<String,Integer>();
		for(int i=0;i<PN.getTransitions().size();i++)
			TempCounter.put(PN.getTransitions().get(i).getName(), 0);
		
		for(int i=0;i<TransitionsExecutionTimes.size();i++) {
			for(int j=0;j<PN.getTransitions().size();j++) {
				ArrayList<Double> TraceLevelTransitionExecutionTimes = TransitionsExecutionTimes.get(i).get(PN.getTransitions().get(j).getName());
				for(int k=0;k<TraceLevelTransitionExecutionTimes.size();k++) {
					Double ExecutionTime = TraceLevelTransitionExecutionTimes.get(k);
					if(ExecutionTime < TransitionsBCETs.get(PN.getTransitions().get(j).getName())) {
						ExecutionTime = Math.round(ExecutionTime*1000000.0)/1000000.0;
						TransitionsBCETs.put(PN.getTransitions().get(j).getName(),ExecutionTime);
					}
					else if(ExecutionTime > TransitionsWCETs.get(PN.getTransitions().get(j).getName())) {
						ExecutionTime = Math.round(ExecutionTime*1000000.0)/1000000.0;
						TransitionsWCETs.put(PN.getTransitions().get(j).getName(),ExecutionTime);
					}
					TransitionsACETs.put(PN.getTransitions().get(j).getName(),TransitionsACETs.get(PN.getTransitions().get(j).getName()) + ExecutionTime);
					TempCounter.put(PN.getTransitions().get(j).getName(), TempCounter.get(PN.getTransitions().get(j).getName())+1);
				}
			}
		}
		for(int j=0;j<PN.getTransitions().size();j++) {
			Double TempACET = TransitionsACETs.get(PN.getTransitions().get(j).getName())/TempCounter.get(PN.getTransitions().get(j).getName());
			TransitionsACETs.put(PN.getTransitions().get(j).getName(),Math.round(TempACET*1000000.0)/1000000.0);
		}
		
		DL.setFitness(Fitness);
		DL.setAnomalousTraces(AnomalousTraces);
		DL.setTraceACET(TraceACET);
		DL.setTraceBCET(TraceBCET);
		DL.setTraceWCET(TraceWCET);
		DL.setTransitionsACETs(TransitionsACETs);
		DL.setTransitionsBCETs(TransitionsBCETs);
		DL.setTransitionsWCETs(TransitionsWCETs);
		
		return DL;
	}
	
	private String findCorrespondingStartingTransitionTimestamp(LinkedList<HashMap<String,String>> StartTransitionsList, String EndingTransitionName) {
		String FoundTimestamp = null;
		ListIterator<HashMap<String,String>> StartTransitionsIterator = StartTransitionsList.listIterator();
		HashMap<String,String> CurrentStartTransitionRetrieved = new HashMap<String,String>();
		boolean FoundCorrespondingStartingActivity = false;
		
		while (StartTransitionsIterator.hasNext() && !FoundCorrespondingStartingActivity) {
			CurrentStartTransitionRetrieved = StartTransitionsIterator.next();
			String CurrentStartTransitionName = CurrentStartTransitionRetrieved.get("ActivityName");
			String CurrentStartTransitionTimestamp = CurrentStartTransitionRetrieved.get("ActivityTimestamp");
			if(CurrentStartTransitionName.equals(EndingTransitionName) ) {
				FoundCorrespondingStartingActivity = true;
				FoundTimestamp = CurrentStartTransitionTimestamp;
			}
		}
		return FoundTimestamp;
	}
	private HashMap<String, ArrayList<Double>> initializeTransitionsExecutionTime() {
		HashMap<String, ArrayList<Double>> InitializedTransitionsExecutionTime = new HashMap<String,ArrayList<Double>>();
		ArrayList<Activity> Transitions = PN.getTransitions();
		
		for(int i=0;i<Transitions.size(); i++) {
			InitializedTransitionsExecutionTime.put(Transitions.get(i).getName(), new ArrayList<Double>());
			
		}
		return InitializedTransitionsExecutionTime;
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
			if(LastActivity.getA().getName().equals(PN.getTransitions().get(PN.getTransitions().size()-1).getName())) {
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
		ArrayList<String> OrderedTimestamps = new ArrayList<String>();
		ArrayList<String> OrderedLifeCycleStages = new ArrayList<String>();
		for(int i=0; i<EventListToOrder.size(); i++) {
			OrderedAIArray.add(EventListToOrder.get(i).getAI());
			OrderedTimestamps.add(EventListToOrder.get(i).getT());
			OrderedLifeCycleStages.add(EventListToOrder.get(i).getLifeCycleStage());
		}
		// The Built Trace is populated with the recovered Activity Instance ArrayList.
		BuiltTrace.setAI(OrderedAIArray);
		BuiltTrace.setTimestamps(OrderedTimestamps);
		BuiltTrace.setLifeCycleStages(OrderedLifeCycleStages);
		
		return BuiltTrace;
	}
	
	
	
	// This method sorts the events by their timestamps
	ArrayList<Event> sort(ArrayList<Event> EventListToOrder){
		int n = EventListToOrder.size();
	    for (int i = 1; i < n; ++i) {
	        Event key = EventListToOrder.get(i);
	        int j = i - 1;
	        while (j >= 0 && Double.parseDouble(EventListToOrder.get(j).getT()) > Double.parseDouble(key.getT())) {
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
		
		
		ArrayList<Trace> NonConformantTraces = C.ConformanceChecking(aware);
		/*for(int i=0; i<NonConformantTraces.size(); i++) {
			System.out.print("Non conformant trace " + i + ": ");
			for(int j=0; j<NonConformantTraces.get(i).getAI().size(); j++)
				System.out.print(NonConformantTraces.get(i).getAI().get(j).getA().getName() + " ");
			System.out.println();
		}
		*/
	}
	
	
	
}
