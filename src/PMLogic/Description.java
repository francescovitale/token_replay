package PMLogic;
import java.util.ArrayList;
import java.util.HashMap;

public class Description {
private
	int ID;
	float Fitness;
	ArrayList<Trace> AnomalousTraces;
	
	double TraceBCET;
	double TraceACET;
	double TraceWCET;
	HashMap<String,Double> TransitionsBCETs;
	HashMap<String,Double> TransitionsACETs;
	HashMap<String,Double> TransitionsWCETs;
	
	
	
public
	Description() {
		ID = -1;
		Fitness = (float) 0.0;
		AnomalousTraces = new ArrayList<Trace>();
		TraceBCET = (float) 0.0;
		TraceACET = (float) 0.0;
		TraceWCET = (float) 0.0;
		TransitionsBCETs = new HashMap<String,Double>();
		TransitionsACETs = new HashMap<String,Double>();
		TransitionsWCETs = new HashMap<String,Double>();
	}

	Description(float Fitness_in, ArrayList<Trace> T_in, double TraceBCET_in, double TraceACET_in, double TraceWCET_in, HashMap<String,Double> TransitionsBCETs_in,
			HashMap<String,Double> TransitionsACETs_in,HashMap<String,Double> TransitionsWCETs_in) {
		ID = -1;
		Fitness = Fitness_in;
		AnomalousTraces = new ArrayList<Trace>(T_in);
		TraceBCET = TraceBCET_in;
		TraceACET = TraceACET_in;
		TraceWCET = TraceWCET_in;
		TransitionsBCETs = new HashMap<String,Double>(TransitionsBCETs_in);
		TransitionsACETs = new HashMap<String,Double>(TransitionsACETs_in);
		TransitionsWCETs = new HashMap<String,Double>(TransitionsWCETs_in);
	}
	Description(Description CD){
		ID = CD.getID();
		Fitness = CD.getFitness();
		AnomalousTraces = new ArrayList<Trace>(CD.getAnomalousTraces());
		TraceBCET = CD.getTraceBCET();
		TraceACET = CD.getTraceACET();
		TraceWCET = CD.getTraceWCET();
		TransitionsBCETs = new HashMap<String,Double>(CD.getTransitionsBCETs());
		TransitionsACETs = new HashMap<String,Double>(CD.getTransitionsACETs());
		TransitionsWCETs = new HashMap<String,Double>(CD.getTransitionsWCETs());
	}

	public float getFitness() {
		return Fitness;
	}
	public void setFitness(float fitness) {
		Fitness = fitness;
	}
	public ArrayList<Trace> getAnomalousTraces() {
		return AnomalousTraces;
	}
	public void setT(ArrayList<Trace> t) {
		AnomalousTraces = t;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public double getTraceBCET() {
		return TraceBCET;
	}

	public void setTraceBCET(double traceBCET) {
		TraceBCET = traceBCET;
	}

	public double getTraceACET() {
		return TraceACET;
	}

	public void setTraceACET(double traceACET) {
		TraceACET = traceACET;
	}

	public double getTraceWCET() {
		return TraceWCET;
	}

	public void setTraceWCET(double traceWCET) {
		TraceWCET = traceWCET;
	}

	public HashMap<String, Double> getTransitionsBCETs() {
		return TransitionsBCETs;
	}

	public void setTransitionsBCETs(HashMap<String, Double> transitionsBCETs) {
		TransitionsBCETs = transitionsBCETs;
	}

	public HashMap<String, Double> getTransitionsACETs() {
		return TransitionsACETs;
	}

	public void setTransitionsACETs(HashMap<String, Double> transitionsACETs) {
		TransitionsACETs = transitionsACETs;
	}

	public HashMap<String, Double> getTransitionsWCETs() {
		return TransitionsWCETs;
	}

	public void setTransitionsWCETs(HashMap<String, Double> transitionsWCETs) {
		TransitionsWCETs = transitionsWCETs;
	}

	public void setAnomalousTraces(ArrayList<Trace> anomalousTraces) {
		AnomalousTraces = anomalousTraces;
	}
}
