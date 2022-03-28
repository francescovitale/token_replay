package FileSystemAccess;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import DatabaseAccess.DBFacade;
import PMLogic.*;

public class FileSystemFacade {
	private PetriNetReader PNR;
	private DiagnosticsWriter DW;
	private volatile static FileSystemFacade FSF = null;
	
	static String WorkingDirectory = System.getProperty("user.dir");
	static String PetriNetsDirectory = WorkingDirectory + "\\PetriNets";
	static String EventLogsDirectory = WorkingDirectory + "\\EventLogs";
	static String ConfigurationParametersDirectory = WorkingDirectory + "\\Configurations";
	static String DiagnosticsDirectory = WorkingDirectory + "\\Diagnostics";

	private FileSystemFacade() {
		PNR = new PetriNetReader(PetriNetsDirectory);
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
	
	public void writeDiagnostics(Description D) {
		try {
			DW.writeDiagnostics(D);
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException, SQLException {
		ProcessModel PM = new ProcessModel("sample_pn");
		DBFacade DBF = new DBFacade();
		ArrayList<ProcessModel> PMList = DBF.getProcessList();
		ArrayList<Activity> AList = DBF.getActivityList(PMList);
		
		FileSystemFacade MyFSF = FileSystemFacade.getInstance();
		PetriNet PN = MyFSF.getPetriNet(PM, AList);
		for(int i=0;i<PN.getPlaces().size(); i++)
			System.out.print(PN.getPlaces().get(i)+" ");
		System.out.println();
		for(int i=0;i<PN.getTransitions().size();i++)
			System.out.print(PN.getTransitions().get(i).getName()+" ");
		System.out.println();
		for(int i=0;i<PN.getTransitions().size();i++)
		{
			System.out.println();
			for(int j=0; j<PN.getPlaces().size();j++) {
				//if(PN.getTP().get(PN.getPlaces().get(j), PN.getTransitions().get(i).getName()) == null)
					System.out.print(PN.getPT().get(PN.getPlaces().get(j), PN.getTransitions().get(i).getName()) + " ");
			}
		}
		System.out.println();
		for(int i=0;i<PN.getPlaces().size();i++)
			System.out.println("Place: " + PN.getPlaces().get(i) + " has marking: " + PN.getMarking().get(PN.getPlaces().get(i)) + " ");
		/*
		ArrayList<TRParameter> TRPList = MyFSF.getTRParameterList(PM);
		for(int i=0; i<TRPList.size(); i++) {
			System.out.println("Resource: "+ TRPList.get(i).getResource() + " Parameter value: " + TRPList.get(i).getValue());
		}
		*/
	}
	
}
