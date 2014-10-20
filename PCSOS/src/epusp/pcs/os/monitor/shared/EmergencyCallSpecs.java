package epusp.pcs.os.monitor.shared;

import java.io.Serializable;
import java.util.HashMap;

public class EmergencyCallSpecs implements Serializable{

	private static final long serialVersionUID = 1L;

	private int victimLastPosition = -1;
	private HashMap<String, Integer> vehiclesLastPosition = new HashMap<String, Integer>();

	public int getVictimLastPositionIndex() {
		return victimLastPosition;
	}

	public void setVictimLastPositionIndex(int victimLastPosition) {
		this.victimLastPosition = victimLastPosition;
	}

	public void putVehiclesLastPositionIndex(String vehicleId, int i){
		vehiclesLastPosition.put(vehicleId, i);
	}

	public HashMap<String, Integer> getVehiclesLastPositionsIndex(){
		return vehiclesLastPosition;
	}

	public Integer getVehicleLastPositionIndex(String id){
		return vehiclesLastPosition.get(id);
	}

	public EmergencyCallSpecs(){
		super();
	}
}
