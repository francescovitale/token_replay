package PMLogic;
import java.util.ArrayList;

public class ProcessModel {
private
	String Name;
	ArrayList<Activity> AL;
	ArrayList<ProcessInstance> PI;
	PetriNet P;
public
	ProcessModel() {
		Name = "";
		AL = null;
		PI = null;
		P = null;
	}

	public ProcessModel(String Name_in) {
		Name = Name_in;
	}
	ProcessModel(ProcessModel PM_in){
		Name = PM_in.getName();
		AL = new ArrayList<Activity>();
		PI = new ArrayList<ProcessInstance>();
		P = new PetriNet();
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public ArrayList<Activity> getAL() {
		return AL;
	}
	public void setAL(ArrayList<Activity> aL) {
		AL = aL;
	}
	public ArrayList<ProcessInstance> getPI() {
		return PI;
	}
	public void setPI(ArrayList<ProcessInstance> pI) {
		PI = pI;
	}
	public PetriNet getP() {
		return P;
	}
	public void setP(PetriNet p) {
		P = p;
	}

}
