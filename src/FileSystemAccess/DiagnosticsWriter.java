package FileSystemAccess;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import PMLogic.Activity;
import PMLogic.Description;
import PMLogic.Trace;

public class DiagnosticsWriter {
	private String DiagnosticsPath;
	
	public DiagnosticsWriter(String dir) {
		DiagnosticsPath = dir;
		
	}
	
	public void writeDiagnostics(Description Statistics, ArrayList<String> TransitionsList) throws IOException {
		
		
		BufferedWriter writer = null;
		File ControlFlowDiagnosticsFile = new File(DiagnosticsPath + "\\cf_diagnostics.txt");
		File PerformanceDiagnosticsFile = new File(DiagnosticsPath + "\\perf_diagnostics.txt");
		
		writer = new BufferedWriter(new FileWriter(ControlFlowDiagnosticsFile));
		writer.write("Log fitness: " + Statistics.getFitness() + "\n");
		writer.write("Anomalous traces:\n");
		for(int i=0;i<Statistics.getAnomalousTraces().size();i++) {
			Trace AnomalousTrace = Statistics.getAnomalousTraces().get(i);
			
			for(int j=0;j<AnomalousTrace.getAI().size();j++) {
				writer.write(AnomalousTrace.getAI().get(j).getA().getName() + " ");
			}
			writer.write("\n");
		}
		writer.close();
			
		writer = new BufferedWriter(new FileWriter(PerformanceDiagnosticsFile));
		writer.write("Trace ACET: " + String.format("%.7f", Statistics.getTraceACET()) + "\n");
		writer.write("Trace BCET: " + String.format("%.7f", Statistics.getTraceBCET()) + "\n");
		writer.write("Trace WCET: " + String.format("%.7f", Statistics.getTraceWCET()) + "\n");
		for(int i=0;i<TransitionsList.size();i++) {
			String TransitionName = TransitionsList.get(i);
			writer.write("Statistics for transition: " + TransitionName + "\nTransition ACET: " + String.format("%.7f", Statistics.getTransitionsACETs().get(TransitionName)) +
					"\nTransition BCET: " + String.format("%.7f", Statistics.getTransitionsBCETs().get(TransitionName)) + 
					"\nTransition WCET: " + String.format("%.7f", Statistics.getTransitionsWCETs().get(TransitionName)) + "\n");
		}
		writer.close();
		
	
	}
}
