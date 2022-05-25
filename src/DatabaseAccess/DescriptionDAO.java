package DatabaseAccess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import PMLogic.ActivityInstance;
import PMLogic.Description;
import PMLogic.Trace;

public class DescriptionDAO {
	public static ArrayList<Description> getDescriptionList(Connection Conn) throws SQLException{
		
		ArrayList<Description> DList = new ArrayList<Description>();
		Statement stmt = Conn.createStatement();
		String query = "SELECT * FROM eventlog_rtis.description";
		ResultSet rs = stmt.executeQuery(query);
		
		while(rs.next()) {
			Description D = new Description();
			D.setFitness(rs.getFloat("Fitness"));
			D.setID(rs.getInt("ID"));
			DList.add(D);
		}
		
		return DList;
	}
	
	public static void insertDescription(Connection Conn, float Fitness) throws SQLException{
		Statement stmt = Conn.createStatement();
		String query = "INSERT INTO eventlog_rtis.description (Fitness) VALUES ('"+Fitness+"')";
		stmt.executeUpdate(query);
		
	}
}
