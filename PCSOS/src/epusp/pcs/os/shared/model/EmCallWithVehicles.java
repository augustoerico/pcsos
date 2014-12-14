package epusp.pcs.os.shared.model;

import epusp.pcs.os.shared.model.oncall.EmergencyCall;

public class EmCallWithVehicles {
	String status;
	EmergencyCall emCall;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public EmergencyCall getEmCall() {
		return emCall;
	}

	public void setEmCall(EmergencyCall emCall) {
		this.emCall = emCall;
	}
	
}
