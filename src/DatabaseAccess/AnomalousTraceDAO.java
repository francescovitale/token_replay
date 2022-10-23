package DatabaseAccess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import PMLogic.ActivityInstance;
import PMLogic.Description;
import PMLogic.Trace;

public class AnomalousTraceDAO {
	public static ArrayList<Trace> getAnomalousTraceList(Connection Conn, ArrayList<ActivityInstance> AIList, ArrayList<Description> D) throws SQLException {
		
		ArrayList<Trace> ATList = new ArrayList<Trace>();
		Statement stmt = Conn.createStatement();
		String query = "SELECT * FROM eventlog_ad_dt.anomaloustrace";
		ResultSet rs = stmt.executeQuery(query);
		
		ArrayList<ActivityInstance> AIListPerTrace;
		
		while(rs.next()) {
			AIListPerTrace = new ArrayList<ActivityInstance>();
			
			int ATID = rs.getInt("ID");
			int DID = rs.getInt("D");
			for(int i=0; i<AIList.size(); i++) {
				if(AIList.get(i).getT().getID() == ATID) {
					AIListPerTrace.add(AIList.get(i));
				}
			}
			for(int i=0; i<D.size(); i++)
			{
				if(D.get(i).getID() == DID) {
					ATList.add(new Trace(ATID,AIListPerTrace,D.get(i)));
				}
			}
			
			
		}
		
		return ATList;
	}
	
	public static void insertAnomalousTrace(Connection Conn, int DID) throws SQLException {
		Statement stmt = Conn.createStatement();
		String query = "INSERT INTO eventlog_ad_dt.anomaloustrace (D) VALUES ('"+DID+"')";
		stmt.executeUpdate(query);
	}
}
