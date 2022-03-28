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
	private String FilePath;
	private ArrayList<String> EList;
	
	public LogExtractor(String P){
		FilePath = P;
	}
	
	
	public ArrayList<Event> extractEvents() throws FileNotFoundException, IOException, ParseException, SQLException{
		
		readCSV();
		DBFacade DBF = new DBFacade();
		
		for(int i=0; i<EList.size(); i++)
			extractEvent(EList.get(i), DBF);
		boolean end = false;
		
		return null;
		
		
	}
	
	private void readCSV() throws FileNotFoundException, IOException{
		File F = new File(FilePath);
		EList = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(F))) {
		    String line;
		    br.readLine();
		    while ((line = br.readLine()) != null) {
		       EList.add(line);
		    }
		}
		
	}
	
	private Event extractEvent(String StringEvent, DBFacade DBF) throws ParseException, SQLException {
		
		int CID;
		String Resource = "";
		String RegisteredActivity;
		String T = "";
		
		String [] Parts = StringEvent.split("\\,");
		
		Parts[0] = Parts[0].replaceAll("\"", "");
		String Parts_ID[] = Parts[0].split("\\_");
		CID = Integer.parseInt(Parts_ID[1]);
		// System.out.println(CID);
		
		Parts[1] = Parts[1].replaceAll("\"", "");
		RegisteredActivity = Parts[1];
		// System.out.println(RegisteredActivity);
		
		Parts[2] = Parts[2].replaceAll("\"","");
		Parts[2] = Parts[2].replaceAll("1970","2000");
		String sDate = Parts[2];  
	    Date date1=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS").parse(sDate);  
	    DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	    String formattedDate = targetFormat.format(date1); 
	    // System.out.println(formattedDate);
	    T = formattedDate;
		
	    DBF.insertEvent(T, Resource, CID, RegisteredActivity, "sample_pn");
		
		return null;
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException, SQLException {
		LogExtractor LE = new LogExtractor(args[0]);
		
		DBFacade DBF = new DBFacade();
		DBF.clearDatabase();
		LE.readCSV();
		LE.extractEvents();
	}
}





