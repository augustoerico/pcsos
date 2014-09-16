package epusp.pcs.os.model.attribute;

import java.io.Serializable;

public enum Category implements Serializable{
	
	PhysicalProfile, MedicalData, PersonalVehicle;
	
	/*
	 * Seen by IsSerializable
	 */
	Category(){
	}
}
