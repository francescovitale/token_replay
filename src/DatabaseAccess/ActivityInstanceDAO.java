package DatabaseAccess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import PMLogic.Activity;
import PMLogic.ActivityInstance;
import PMLogic.Event;
import PMLogic.ProcessInstance;
import PMLogic.Trace;

public class ActivityInstanceDAO {

	public static ArrayList<ActivityInstance> getActivityInstanceList(ArrayList<Activity> AList,
		ArrayList<ProcessInstance> PIList, Connection Conn) throws SQLException {
		ArrayList<ActivityInstance> AIList = new ArrayList<ActivityInstance>();
		Statement stmt = Conn.createStatement();
		String query = "SELECT * FROM eventlog_rtis.activityinstance";
		ResultSet rs = stmt.executeQuery(query);
		
		
		while(rs.next()) {
			int AICase = rs.getInt("CaseID");
			String AIActivity = rs.getString("Act");
			
			int TraceID = rs.getInt("ATrace");
			Trace T = new Trace(TraceID); // A trace is created with only the ID.
			
			for(int i=0; i<AList.size(); i++) {
				if(AList.get(i).getName().equals(AIActivity)) {
					
					for(int j=0; j<PIList.size(); j++) {
						if(PIList.get(j).getCaseID() == AICase) {
							AIList.add(new ActivityInstance(rs.getInt("ID"), PIList.get(j),AList.get(i),T));
						}
					}
				}
			}
		}
		
		return AIList;
	}

	public static int insertActivityInstance(String AIAct, int CaseID, ArrayList<Event> EList, Connection Conn) throws SQLException {
		int instantiatedActivityInstanceID = -1;
		Statement stmt = Conn.createStatement();
		
		Event LastRecordedInstance = null;
		
		for(int i=0;i<EList.size();i++) {
			if(EList.get(i).getAI().getA().getName().equals(AIAct))
				LastRecordedInstance = EList.get(i);
		}
		
		if(LastRecordedInstance != null && !LastRecordedInstance.getLifeCycleStage().equals("end"))
			instantiatedActivityInstanceID = LastRecordedInstance.getAI().getID();
		else {
			// A Trace ID of -1 is inserted.
			String query = "INSERT INTO eventlog_rtis.activityinstance (CaseID,Act,ATrace) VALUES ('"+ CaseID  +"','"+AIAct+"',null)";                 
			stmt.executeUpdate(query);
		}
		return instantiatedActivityInstanceID;
	}
	
	public static void updateActivityInstanceATID(Connection Conn, int ActID, int ATID) throws SQLException {
		Statement stmt = Conn.createStatement();
		// A Trace ID of -1 is inserted.
		
		// UPDATE table_name SET field1 = new-value1, field2 = new-value2
		//		[WHERE Clause]
		String query = "UPDATE eventlog_rtis.activityinstance SET ATrace = '"+ATID+"' WHERE ID = '"+ActID+"'";                 
		stmt.executeUpdate(query);
	}

	public static void nullifyActivityInstanceCaseID(Connection Conn) throws SQLException {
		Statement stmt = Conn.createStatement();
		// A Trace ID of -1 is inserted.
		
		// UPDATE table_name SET field1 = new-value1, field2 = new-value2
		//		[WHERE Clause]
		String query = "UPDATE eventlog_rtis.activityinstance SET CaseID = null";                 
		stmt.executeUpdate(query);
		
	}

}
