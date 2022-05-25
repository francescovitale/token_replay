package FileSystemAccess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import DatabaseAccess.DBFacade;
import PMLogic.Event;

public class LogExtractor {
	private ArrayList<String> EList;
	
	static String WorkingDirectory = System.getProperty("user.dir");
	static String EventLogsDirectory = WorkingDirectory + "\\EventLogs";
	
	public LogExtractor(){
	}
	
	
	public ArrayList<Event> extractEvents(String LogFile) throws FileNotFoundException, IOException, ParseException, SQLException{
		
		readCSV(LogFile);
		DBFacade DBF = new DBFacade();
		
		for(int i=0; i<EList.size(); i++)
			extractEvent(EList.get(i), DBF);
		
		return null;
		
		
	}
	
	private void readCSV(String LogFile) throws FileNotFoundException, IOException{
		File F = new File(EventLogsDirectory + "\\" + LogFile);
		EList = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(F))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       EList.add(line);
		    }
		}
		
	}
	
	private void extractEvent(String StringEvent, DBFacade DBF) throws ParseException, SQLException {
		
		int Case_ID;
		String RegisteredActivity = "";
		String Timestamp = "";
		String LifeCycleStage = "";
		
		String [] Parts = StringEvent.split("\\,");
		
		Case_ID = Integer.parseInt(Parts[0]);
		RegisteredActivity = Parts[1];
		Timestamp = Parts[2];
		LifeCycleStage = Parts[3];
		
		
	    DBF.insertEvent(Timestamp, LifeCycleStage, Case_ID, RegisteredActivity, "sensor_controlflow");
		
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException, SQLException {
		LogExtractor LE = new LogExtractor();
		LE.extractEvents("sensor_monitor_logs");
	}
}





