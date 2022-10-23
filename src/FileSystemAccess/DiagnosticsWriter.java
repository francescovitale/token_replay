package FileSystemAccess;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import PMLogic.Description;

public class DiagnosticsWriter {
	private String DiagnosticsDirectory;
	
	public DiagnosticsWriter(String Dir) {
		DiagnosticsDirectory = Dir;
	}
	
	public void WriteDiagnostics(Description D) throws IOException {
		String DiagnosticsFilePath = DiagnosticsDirectory + "//cf_diagnostics.txt";
		BufferedWriter writer = new BufferedWriter(new FileWriter(DiagnosticsFilePath));
	    writer.write("Fitness="+Float.toString(D.getFitness()) + "\n");
	    HashMap<String, Integer> UnfiredActivitiesDiagnostics = D.getAnomalyCount();

	    for (String key : UnfiredActivitiesDiagnostics.keySet()) {
	    	writer.write(key + "=" + Integer.toString(UnfiredActivitiesDiagnostics.get(key)) + "\n");
	    }
	    
	    writer.close();
		
	}
}
