package FileSystemAccess;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import DatabaseAccess.DBFacade;
import PMLogic.*;

public class FileSystemFacade {
	private PetriNetReader PNR;
	private TRParameterReader TRPR;
	private DiagnosticsWriter DW;
	private volatile static FileSystemFacade FSF = null;
	
	static String WorkingDirectory = System.getProperty("user.dir");
	static String PetriNetsDirectory = WorkingDirectory + "\\PetriNets";
	static String ConfigurationParametersDirectory = WorkingDirectory + "\\Configurations";
	static String DiagnosticsDirectory = WorkingDirectory + "\\Diagnostics";

	private FileSystemFacade() {
		PNR = new PetriNetReader(PetriNetsDirectory);
		TRPR = new TRParameterReader(ConfigurationParametersDirectory);
		DW = new DiagnosticsWriter(DiagnosticsDirectory);
	}
	public static FileSystemFacade getInstance() {
		if(FSF==null) {
			synchronized(FileSystemFacade.class) {
				if(FSF==null) {
					FSF = new FileSystemFacade();
				}
			}
		}
		return FSF;
	}
	
	public PetriNet getPetriNet(ProcessModel PM, ArrayList<Activity> A) {
		
		PetriNet PN = null;
		try {
			PN = PNR.getPetriNet(PM, A);
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return PN;
	}
	
	public void writeDiagnostics(Description D, ArrayList<String> TransitionsList) {
		try {
			DW.writeDiagnostics(D, TransitionsList);
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<TRParameter> getTRParameterList(ProcessModel PM){
		ArrayList<TRParameter> TRPList = null;
		
		try {
			TRPList = TRPR.getTRParameterList(PM);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return TRPList;
		
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException, SQLException {
		ProcessModel PM = new ProcessModel("sensor_controlflow");
		DBFacade DBF = new DBFacade();
		ArrayList<ProcessModel> PMList = DBF.getProcessList();
		ArrayList<Activity> AList = DBF.getActivityList(PMList);
		
		FileSystemFacade MyFSF = FileSystemFacade.getInstance();
		PetriNet PN = MyFSF.getPetriNet(PM, AList);
		System.out.println("Places:");
		for(int i=0;i<PN.getPlaces().size(); i++) {
			System.out.print(PN.getPlaces().get(i)+" ");
		}
		System.out.println();
		
		System.out.println("Transitions:");
		for(int i=0;i<PN.getTransitions().size();i++) {
			System.out.print(PN.getTransitions().get(i).getName()+" ");
		}
		System.out.println();
		
		System.out.println("Default (initial) marking: ");
		for(int i=0;i<PN.getPlaces().size();i++) {
			System.out.print(PN.getPlaces().get(i) + ": " + PN.getDefaultMarking().get(PN.getPlaces().get(i))+ " ");
		}
		System.out.println();
		
		System.out.println("Final transition: ");
		System.out.println(PN.getTerminatingEvent());
		
		System.out.println();
		for(int i=0;i<PN.getPlaces().size();i++)
		{
			//System.out.println();
			for(int j=0; j<PN.getTransitions().size();j++) {
				//if(PN.getTP().get(PN.getPlaces().get(j), PN.getTransitions().get(i).getName()) == null)
					System.out.print("Transition: " + PN.getTransitions().get(j).getName() + " Place: " + PN.getPlaces().get(i) + " value: ");
					System.out.println(PN.getPT().get(PN.getPlaces().get(i), PN.getTransitions().get(j).getName()) + " ");
			}
		}
		/*
		ArrayList<TRParameter> TRPList = MyFSF.getTRParameterList(PM);
		for(int i=0; i<TRPList.size(); i++) {
			System.out.println("Resource: "+ TRPList.get(i).getResource() + " Parameter value: " + TRPList.get(i).getValue());
		}*/
	}
	
}
