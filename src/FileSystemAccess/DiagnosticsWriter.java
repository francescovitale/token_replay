package FileSystemAccess;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import PMLogic.Activity;
import PMLogic.Description;
import PMLogic.Trace;

public class DiagnosticsWriter {
	private String DiagnosticsPath;
	
	public DiagnosticsWriter(String dir) {
		DiagnosticsPath = dir;
		
	}
	
	public void writeDiagnostics(Description Statistics) throws IOException {
		
		HashMap<String, Integer> MissedActivitiesCount = Statistics.getMissedActivities();
		
		BufferedWriter writer = null;
		File ControlFlowDiagnosticsFile = new File(DiagnosticsPath + "\\cf_diagnostics.txt");
		
		writer = new BufferedWriter(new FileWriter(ControlFlowDiagnosticsFile));
		writer.write("Log fitness:" + Statistics.getFitness() + "\n");
        for (Map.Entry<String, Integer> set :
        	MissedActivitiesCount.entrySet()) {
        	writer.write(set.getKey()+ ":"+ set.getValue()+"\n");
        }
		writer.close();
	
	}
}
