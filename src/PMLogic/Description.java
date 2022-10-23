package PMLogic;
import java.util.ArrayList;
import java.util.HashMap;

public class Description {
private
	int ID;
	float Fitness;
	ArrayList<Trace> T;
	HashMap<String, Integer> AnomalyCount;
	
public
	Description() {
		ID = -1;
		Fitness = (float) 0.0;
		T = new ArrayList<Trace>();
		AnomalyCount = new HashMap<String, Integer>();
	}

	Description(float Fitness_in, ArrayList<Trace> T_in, HashMap<String, Integer> AnomalyCount_in) {
		ID = -1;
		Fitness = Fitness_in;
		T = new ArrayList<Trace>(T_in);
		AnomalyCount = new HashMap<String, Integer>(AnomalyCount_in);
	}
	Description(Description CD){
		ID = CD.getID();
		Fitness = CD.getFitness();
		T = CD.getT();
		AnomalyCount = CD.getAnomalyCount();
	}

	public float getFitness() {
		return Fitness;
	}
	public void setFitness(float fitness) {
		Fitness = fitness;
	}
	public ArrayList<Trace> getT() {
		return T;
	}
	public void setT(ArrayList<Trace> t) {
		T = t;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public HashMap<String, Integer> getAnomalyCount() {
		return AnomalyCount;
	}

	public void setAnomalyCount(HashMap<String, Integer> anomalyCount) {
		AnomalyCount = anomalyCount;
	}
}
