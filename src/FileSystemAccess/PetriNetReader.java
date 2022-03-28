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
		HashMap<String,Integer> DefaultMarking = getDefaultMarking(PNDataDirectory + "\\default_marking", Places);
		String FinalTransition = getFinalTransition(PNDataDirectory + "\\final_transition");
		
		PN = new PetriNet(PM,Places,Transitions,PT,TP,DefaultMarking,FinalTransition);
		
		return PN;
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
				//System.out.print("Fetched Place: " + FetchedPlaces[i] + " Fetched Transition: " + FetchedTransitions[j] + 
				//		" Fetched Arc: " + FetchedArcs[i][j]);
				PT.put(FetchedPlaces[i], FetchedTransitions[j], FetchedArcs[i][j]);
			}
		}
		return PT;
	};
	
	HashMap<String,Integer> getDefaultMarking(String DefaultMarkingFilePath, ArrayList<String> Places) throws IOException{
		HashMap<String, Integer> Marking = new HashMap<String, Integer>();
		
		for(int i=0;i<Places.size();i++)
			Marking.put(Places.get(i),0);
		
		try (BufferedReader br = new BufferedReader(new FileReader(DefaultMarkingFilePath))) {
		    String ReadPlace;
		    while ((ReadPlace = br.readLine()) != null) {
		    	String[] Tokens = ReadPlace.split(":");
		    	if(Tokens[1].equals("1")) {
		        	Marking.put(Tokens[0], 1);
		    	}
		    }
		}
		
		return Marking;
		
	}
	
	String getFinalTransition(String FinalTransitionFilePath) throws FileNotFoundException, IOException {
		String FinalTransition = null;
		
		try (BufferedReader br = new BufferedReader(new FileReader(FinalTransitionFilePath))) {
		    String ReadFinalTransition;
		    while ((ReadFinalTransition = br.readLine()) != null) {
		       FinalTransition = ReadFinalTransition;
		    }
		}
		
		return FinalTransition;
	}
	
}
