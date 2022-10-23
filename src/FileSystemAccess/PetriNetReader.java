package FileSystemAccess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import PMLogic.*;

public class PetriNetReader {
	private String PetriNetsDirectory;
	
public	
	PetriNetReader(String Dir) {
		PetriNetsDirectory = Dir;
	};
	
	public PetriNet getPetriNet(ProcessModel PM, ArrayList<Activity> A) throws IOException {
		PetriNet PN;
		String PNDataDirectory = PetriNetsDirectory + "\\" + PM.getName();
		
		ArrayList<String> Places = getPlaces(PNDataDirectory + "\\places");
		ArrayList<Activity> Transitions = getTransitions(PNDataDirectory + "\\transitions", A);
		BiHashMap<String,String,Boolean> TP = getTP(PNDataDirectory + "\\TP.csv");
		BiHashMap<String,String,Boolean> PT = getPT(PNDataDirectory + "\\PT.csv");
		HashMap<String,Integer> M = getMarking(PM);
		String LT = getLastTransition(PNDataDirectory + "\\final_transition");
		
		PN = new PetriNet(PM,Places,Transitions,PT,TP,M, LT);
		
		return PN;
	}
	
	private String getLastTransition(String LastTransitionFilePath) throws FileNotFoundException {
		File LastTransitionFile = new File(LastTransitionFilePath);
		Scanner Reader = new Scanner(LastTransitionFile);
		String LastTransition = Reader.nextLine();
		
		Reader.close();
		return LastTransition;
	}

	ArrayList<String> getPlaces(String PlacesFilePath) throws FileNotFoundException{
		ArrayList<String> Places = new ArrayList<String>();
		File PlacesFile = new File(PlacesFilePath);
		Scanner Reader = new Scanner(PlacesFile);
		while(Reader.hasNextLine()) {
			Places.add(Reader.nextLine());
		}
		Reader.close();
		return Places;
	};
	
	
	ArrayList<Activity> getTransitions(String TransitionsFilePath, ArrayList<Activity> A) throws FileNotFoundException{
		ArrayList<Activity> Transitions = new ArrayList<Activity>();
		File TransitionsFile = new File(TransitionsFilePath);
		Scanner Reader = new Scanner(TransitionsFile);
		while(Reader.hasNextLine()) {
			String TransitionName = Reader.nextLine();
			for(int i=0; i<A.size(); i++)
				if(A.get(i).getName().equals(TransitionName))
					Transitions.add(A.get(i));
		}
		Reader.close();
		return Transitions;
	};
	
	BiHashMap<String,String,Boolean> getTP(String TPFilePath) throws IOException{
		BiHashMap<String,String,Boolean> TP = new BiHashMap<String,String,Boolean>();
		String [] FetchedPlaces;
		String [] FetchedTransitions;
		Boolean [][] FetchedArcs;
		List<List<String>> records = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(TPFilePath))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        String[] values = line.split(";");
		        records.add( Arrays.asList(values));
		    }
		}
		FetchedTransitions = new String[records.get(0).size()-1];
		FetchedPlaces = new String[records.size()-1];
		FetchedArcs = new Boolean[records.size()-1][records.get(0).size()-1];
		for(int i = 1; i<records.get(0).size(); i++) {
			FetchedTransitions[i-1] = records.get(0).get(i);
		}
		for(int i = 1; i<records.size(); i++) {
			FetchedPlaces[i-1] = records.get(i).get(0);
		}
		for(int i = 1; i<records.size(); i++) {
			for(int j = 1; j<records.get(0).size(); j++) {
				if(records.get(i).get(j).equals("0"))
					FetchedArcs[i-1][j-1] = false;
				else
					FetchedArcs[i-1][j-1] = true;
			}
		}
		for(int i = 0; i<FetchedPlaces.length; i++) {
			for(int j=0; j<FetchedTransitions.length; j++) {
				TP.put(FetchedPlaces[i], FetchedTransitions[j], FetchedArcs[i][j]);
			}
		}
		return TP;
	};
	BiHashMap<String,String,Boolean> getPT(String PTFilePath) throws FileNotFoundException, IOException{
		BiHashMap<String,String,Boolean> PT = new BiHashMap<String,String,Boolean>();
		String [] FetchedPlaces;
		String [] FetchedTransitions;
		Boolean [][] FetchedArcs;
		List<List<String>> records = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(PTFilePath))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        String[] values = line.split(";");
		        records.add(Arrays.asList(values));
		    }
		}
		FetchedTransitions = new String[records.get(0).size()-1];
		FetchedPlaces = new String[records.size()-1];
		FetchedArcs = new Boolean[records.size()-1][records.get(0).size()-1];
		for(int i = 1; i<records.get(0).size(); i++) {
			FetchedTransitions[i-1] = records.get(0).get(i);
		}
		for(int i = 1; i<records.size(); i++) {
			FetchedPlaces[i-1] = records.get(i).get(0);
		}
		for(int i = 1; i<records.size(); i++) {
			for(int j = 1; j<records.get(0).size(); j++) {
				if(records.get(i).get(j).equals("0"))
					FetchedArcs[i-1][j-1] = false;
				else
					FetchedArcs[i-1][j-1] = true;
			}
		}
		for(int i = 0; i<FetchedPlaces.length; i++) {
			for(int j=0; j<FetchedTransitions.length; j++) {
				PT.put(FetchedPlaces[i], FetchedTransitions[j], FetchedArcs[i][j]);
			}
		}
		return PT;
	};
	
	HashMap<String,Integer> getMarking(ProcessModel PM) throws FileNotFoundException{
		String PNDataDirectory = PetriNetsDirectory + "\\" + PM.getName();
		ArrayList<String> Places = new ArrayList<String>(getPlaces(PNDataDirectory + "\\places"));
		HashMap<String,Integer> M = new HashMap<String,Integer>();
		for(int i=0; i<Places.size(); i++) {
			if(i == 0)
				M.put(Places.get(i), 1);
			else
				M.put(Places.get(i), 0);
		}
		return M;
		
	}
	
}
