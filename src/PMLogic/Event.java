package PMLogic;

public class Event {
private
	int ID;
	String T;
	String LifeCycleStage;
	ProcessInstance PI;
	ActivityInstance AI;
public
	Event() {
		ID = -1;
		T = "";
		LifeCycleStage = "";
		PI = null;
		AI = null;
	}

	public Event(int ID_in, String T_in, String LifeCycleStage_in, ProcessInstance P_in, ActivityInstance A_in) {
		ID = ID_in;
		T = T_in;
		LifeCycleStage = LifeCycleStage_in;
		PI = P_in;
		AI = A_in;
	}
	Event(Event E){
		ID = E.getID();
		T = E.getT();
		LifeCycleStage = E.getLifeCycleStage();
		PI = new ProcessInstance(E.getPI());
		AI = new ActivityInstance(E.getAI());
		
	}
public int getID() {
	return ID;
}
public void setID(int iD) {
	ID = iD;
}
public String getT() {
	return T;
}
public void setT(String i) {
	T = i;
}
public String getLifeCycleStage() {
	return LifeCycleStage;
}
public void setLifeCycleStage(String lifecyclestage) {
	LifeCycleStage = lifecyclestage;
}

public ProcessInstance getPI() {
	return PI;
}

public void setPI(ProcessInstance pI) {
	PI = pI;
}

public ActivityInstance getAI() {
	return AI;
}

public void setAI(ActivityInstance aI) {
	AI = aI;
}
	
}
