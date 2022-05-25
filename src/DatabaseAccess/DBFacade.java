package DatabaseAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import PMLogic.Activity;
import PMLogic.ActivityInstance;
import PMLogic.Description;
import PMLogic.Event;
import PMLogic.ProcessInstance;
import PMLogic.ProcessModel;
import PMLogic.Trace;

public class DBFacade {
	private Connection Conn;
public
	DBFacade() {
		try {
			Conn = getConnection();
		} catch (SQLException e) {
			System.out.println(e);
			//System.out.println("Couldn't connect to the database");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void closeConnection() throws SQLException {
		Conn.close();
	}
	private Connection getConnection() throws SQLException, ClassNotFoundException {
		 Connection conn = null;
		 Properties connectionProps = new Properties();
		 connectionProps.put("user", "root");
		 connectionProps.put("password", "root");
		 Class.forName("com.mysql.cj.jdbc.Driver");
		 conn = DriverManager.getConnection(
			     "jdbc:" + "mysql" + "://" + "127.0.0.1" +":" + "3306" + "/",connectionProps);

		 return conn;
	}
	public ArrayList<ProcessModel> getProcessList() throws SQLException{
		
		ArrayList<ProcessModel> PM;
		
		PM = ProcessDAO.getProcessList(Conn);
		
		return PM;
		
		
	}
	public ArrayList<Activity> getActivityList(ArrayList<ProcessModel> PMList) throws SQLException{
		ArrayList<Activity> AList;
		
		AList = ActivityDAO.getActivityList(PMList, Conn);
		return AList;
	}
	
	public ArrayList<ActivityInstance> getActivityInstanceList(ArrayList<Activity> AList, ArrayList<ProcessInstance> PIList) throws SQLException{
		ArrayList<ActivityInstance> AI;
		
		AI = ActivityInstanceDAO.getActivityInstanceList(AList, PIList, Conn);
		
		
		return AI;
	}
	
	public ArrayList<Event> getEventList(ArrayList<ProcessInstance> PIList, ArrayList<ActivityInstance> AIList) throws SQLException{
		ArrayList<Event> EList;
		
		EList = EventDAO.getEventList(PIList,AIList,Conn);
	
		
		return EList;
	}
	public ArrayList<ProcessInstance> getProcessInstanceList(ArrayList<ProcessModel> PMList) throws SQLException{
		
		ArrayList<ProcessInstance> PIList;
		
		
		PIList = ProcessInstanceDAO.getProcessInstanceList(Conn, PMList);
		
		return PIList;
		
	}
	
	public ArrayList<Description> getDescriptionList() throws SQLException{
		ArrayList<Description> DList;
		
		DList = DescriptionDAO.getDescriptionList(Conn);
		
		return DList;
	}
	
	public ArrayList<Trace> getAnomalousTraceList(ArrayList<ActivityInstance> AIList /* In this case, also the Activity Instances are fetched. */, ArrayList<Description> DList) throws SQLException{
		ArrayList<Trace> ATList;
		
		ATList = AnomalousTraceDAO.getAnomalousTraceList(Conn, AIList,DList);
		
		return ATList;
	}
	
	public void insertAnomalousTrace(int[] ActID, float Fitness) throws SQLException {
		// Insert a new tuple in the 'description' table first.
		
		DescriptionDAO.insertDescription(Conn, Fitness);
		
		// Get the last inserted ID
		
		ArrayList<Description> DList = getDescriptionList();
		int LastInsertedID = DList.get(DList.size()-1).getID();
		
		// Insert a new tuple in the 'anomaloustrace' table
		
		AnomalousTraceDAO.insertAnomalousTrace(Conn, LastInsertedID);
		
		// Get the last inserted ID
		ArrayList<ProcessModel> PMList = ProcessDAO.getProcessList(Conn);
		ArrayList<Activity> AList = ActivityDAO.getActivityList(PMList, Conn);
		ArrayList<ProcessInstance> PIList = ProcessInstanceDAO.getProcessInstanceList(Conn, PMList);
		ArrayList<ActivityInstance> AIList = ActivityInstanceDAO.getActivityInstanceList(AList, PIList, Conn);
		ArrayList<Trace> TList =  AnomalousTraceDAO.getAnomalousTraceList(Conn,AIList, DList);
		LastInsertedID = TList.get(TList.size()-1).getID();
		
		// Update the tuples in the 'activityinstance' table for each activity in the anomalous trace
		
		for(int i=0; i<ActID.length; i++)
			ActivityInstanceDAO.updateActivityInstanceATID(Conn,ActID[i],LastInsertedID);
	}
	
	public void insertEvent(String Timestamp, String LifeCycleStage, int CaseID, String AIAct, String PMName) throws SQLException{
		
		int ActID;
		ArrayList<ProcessModel> PMList = getProcessList();
		ArrayList<ProcessInstance> PIList = getProcessInstanceList(PMList);
		ArrayList<Activity> AList = getActivityList(PMList);
		
		insertProcessInstance(CaseID,PMName);
		ActID = insertActivityInstance(PMName,AIAct,CaseID);
		ArrayList<ActivityInstance> AIList = getActivityInstanceList(AList, PIList);
		if(ActID == -1) { // The event is not linked to any existing activity instance
			PIList = getProcessInstanceList(PMList);
			AIList = getActivityInstanceList(AList, PIList);
		    ActID = AIList.get(AIList.size()-1).getID();
		}
		EventDAO.insertEvent(Timestamp, LifeCycleStage, CaseID, ActID, Conn);
	};
	public void deleteEvent(int ActInst[], int CaseID) throws SQLException{
		
		for(int i=0; i<ActInst.length; i++)
			EventDAO.deleteEvent(ActInst[i], Conn);
		
		//ActivityInstanceDAO.nullifyActivityInstanceCaseID(Conn);
		
		//deleteProcessInstance(CaseID);
	}
	void deleteProcessInstance(int CaseID) throws SQLException{
		ProcessInstanceDAO.deleteProcessInstance(CaseID, Conn);
	}
	
	void insertProcess(String PMName) throws SQLException {
		ProcessDAO.insertProcessModel(PMName, Conn);
	};
	void insertActivity(String AName, String PMName, String Res) throws SQLException {
		ProcessModel PM = new ProcessModel(PMName);
		Activity A = new Activity(AName, PM);
		ActivityDAO.insertActivity(A, Conn);
	};
	void insertProcessInstance(int CaseID, String PM) throws SQLException {
		ArrayList<ProcessModel> PMList = getProcessList();
		ProcessInstanceDAO.insertProcessInstance(CaseID,PM,PMList, Conn);
	};
	int insertActivityInstance(String PMName, String AIAct, int CaseID) throws SQLException {
		ArrayList<ProcessModel> PMList = getProcessList();
		ArrayList<ProcessInstance> PIList = getProcessInstanceList(PMList);
		ArrayList<Activity> AList = getActivityList(PMList);
		ArrayList<ActivityInstance> AIList = getActivityInstanceList(AList, PIList);
		ArrayList<Event> EList = getEventList(PIList, AIList);
		for(int i=0;i<EList.size();i++)
		{
			if(!EList.get(i).getPI().getP().getName().equals(PMName))
				EList.remove(i);
		}
		return ActivityInstanceDAO.insertActivityInstance(AIAct, CaseID, EList, Conn);
	};
	
	
	
	
	public static void main(String[] args) {
		DBFacade DBF = new DBFacade();
		String TS;
		
		try {
			
			DBF.insertEvent("35413214.32141656","start", 0, "S0", "start_of_mission_carmine");
			DBF.insertEvent("35413214.33141656","end", 0, "S0", "start_of_mission_carmine");
			DBF.insertEvent("35413214.34141656","start", 0, "S1", "start_of_mission_carmine");
			DBF.insertEvent("35413214.35141656","end", 0, "S1", "start_of_mission_carmine");
			DBF.insertEvent("35413214.36141656","start", 0, "S2", "start_of_mission_carmine");
			DBF.insertEvent("35413214.37141656","end", 0, "S2", "start_of_mission_carmine");
			DBF.insertEvent("35413214.38141656","start", 0, "S4", "start_of_mission_carmine");
			DBF.insertEvent("35413214.39141656","end", 0, "S4", "start_of_mission_carmine");
			DBF.insertEvent("35413214.40141656","start", 0, "S5", "start_of_mission_carmine");
			DBF.insertEvent("35413214.41141656","end", 0, "S5", "start_of_mission_carmine");
			DBF.insertEvent("35413214.42141656","start", 0, "S12", "start_of_mission_carmine");
			DBF.insertEvent("35413214.43141656","end", 0, "S12", "start_of_mission_carmine");
			DBF.insertEvent("35413214.44141656","start", 0, "S13", "start_of_mission_carmine");
			DBF.insertEvent("35413214.45141656","end", 0, "S13", "start_of_mission_carmine");
			DBF.insertEvent("35413214.46141656","start", 0, "S14", "start_of_mission_carmine");
			DBF.insertEvent("35413214.47141656","end", 0, "S14", "start_of_mission_carmine");
			DBF.insertEvent("35413214.48141656","start", 0, "S24", "start_of_mission_carmine");
			DBF.insertEvent("35413214.49141656","end", 0, "S24", "start_of_mission_carmine");
			
			/*
			ArrayList<ProcessModel> PMList = DBF.getProcessList();
			System.out.println("Processes:");
			for(int i=0; i<PMList.size(); i++)
				System.out.println(PMList.get(i).getName());
			System.out.println();
			ArrayList<Activity> AList = DBF.getActivityList(PMList);
			System.out.println("Activities:");
			for(int i=0; i<AList.size(); i++)
				System.out.println(AList.get(i).getName() + " " + AList.get(i).getPM().getName());
			System.out.println();
			ArrayList<ProcessInstance> PIList = DBF.getProcessInstanceList(PMList);
			System.out.println("Process instances:");
			for(int i=0; i<PIList.size(); i++)
				System.out.println(PIList.get(i).getCaseID() + " " + PIList.get(i).getP().getName());
			System.out.println();
			ArrayList<ActivityInstance> AIList = DBF.getActivityInstanceList(AList, PIList);
			System.out.println("Activity instances:");
			for(int i=0; i<AIList.size(); i++)
				System.out.println(AIList.get(i).getID() + " " + AIList.get(i).getPI().getCaseID() + " " + AIList.get(i).getA().getName());
			System.out.println();
			
			ArrayList<Event> EList = DBF.getEventList(PIList, AIList);
			System.out.println("Events:");
			for(int i=0;i<EList.size(); i++)
				System.out.println(EList.get(i).getID() + " " + EList.get(i).getT() + " " + EList.get(i).getLifeCycleStage() + " "+ EList.get(i).getPI().getCaseID() + " "+ EList.get(i).getAI().getA().getName());
			System.out.println();
			
			/*int[] ActID = new int[3];
			ActID[0] = 1026;
			ActID[1] = 1027;
			ActID[2] = 1028;
			
			DBF.insertAnomalousTrace(ActID, 1);
			
			ArrayList<Description> D = DBF.getDescriptionList();
			ArrayList<Trace> T = DBF.getAnomalousTraceList(AIList, D);
			for(int i=0; i<T.size(); i++) {
				for(int j=0; j<T.get(i).getAI().size(); j++)
					System.out.println(T.get(i).getID() + " " + T.get(i).getAI().get(j).getA().getName()+ " " + T.get(i).getCD().getFitness());
			}*/
			DBF.closeConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
