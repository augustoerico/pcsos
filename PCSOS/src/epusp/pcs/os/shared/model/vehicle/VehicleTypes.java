package epusp.pcs.os.shared.model.vehicle;

import java.io.Serializable;

public enum VehicleTypes implements Serializable {
	Car, Helicopter;
	
	/*
	 * Seen by IsSerializable
	 */
	VehicleTypes(){
	}
}
