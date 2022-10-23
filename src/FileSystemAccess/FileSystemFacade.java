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
	private volatile static FileSystemFacade FSF = null;
	
	static String WorkingDirectory = System.getProperty("user.dir");
	static String PetriNetsDirectory = WorkingDirectory;
	static String EventLogsDirectory = WorkingDirectory;
	static String ConfigurationParametersDirectory = WorkingDirectory;

	private FileSystemFacade(String RelativePath) {
		PetriNetsDirectory = PetriNetsDirectory + "\\" + RelativePath + "\\PetriNets";
		EventLogsDirectory = EventLogsDirectory + "\\" + RelativePath + "\\EventLogs";
		ConfigurationParametersDirectory = ConfigurationParametersDirectory + "\\" + RelativePath + "\\Configurations";
		PNR = new PetriNetReader(PetriNetsDirectory);
		TRPR = new TRParameterReader(ConfigurationParametersDirectory);
	}
	public static FileSystemFacade getInstance(String RelativePath) {
		if(FSF==null) {
			synchronized(FileSystemFacade.class) {
				if(FSF==null) {
					FSF = new FileSystemFacade(RelativePath);
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
		ProcessModel PM = new ProcessModel("rbc_handover");
		DBFacade DBF = new DBFacade();
		ArrayList<ProcessModel> PMList = DBF.getProcessList();
		ArrayList<Activity> AList = DBF.getActivityList(PMList);
		
		FileSystemFacade MyFSF = FileSystemFacade.getInstance(args[0]);
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
					System.out.print(PN.getTP().get(PN.getPlaces().get(j), PN.getTransitions().get(i).getName()) + " ");
			}
		}
		
		/*ArrayList<TRParameter> TRPList = MyFSF.getTRParameterList(PM);
		for(int i=0; i<TRPList.size(); i++) {
			System.out.println("Resource: "+ TRPList.get(i).getResource() + " Parameter value: " + TRPList.get(i).getValue());
		}
		System.out.println();
		for(int i=0;i<PN.getPlaces().size();i++) {
			System.out.println("Place: " + PN.getPlaces().get(i) + " marking value: " + PN.getMarking().get(PN.getPlaces().get(i)));
		}*/
		
		
	}
	
}
