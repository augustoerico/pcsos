package epusp.pcs.os.model;

public class EmergencyCall {

	private String message;

	private String victim;
	
	public EmergencyCall(String message, String victim){
		this.message = message;
		this.victim = victim;
	}
	
	public String getMessage(){
		return message;
	}
	
	public String getVictim(){
		return victim;
	}
}
