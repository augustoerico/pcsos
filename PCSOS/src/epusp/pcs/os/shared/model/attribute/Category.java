package epusp.pcs.os.shared.model.attribute;

import java.io.Serializable;

public enum Category implements Serializable{
	
	PhysicalProfile, MedicalData, PersonalVehicle;
	
	/*
	 * Seen by IsSerializable
	 */
	Category(){
	}
}