package epusp.pcs.os.shared.model.attribute;

import java.io.Serializable;

import com.google.gwt.core.client.GWT;

import epusp.pcs.os.shared.client.constants.CommonWorkspaceConstants;

public enum Category implements Serializable{
	
	GeneralInfo, PhysicalProfile, MedicalData, PersonalVehicle;
	
	public String getText(){
		CommonWorkspaceConstants constants = GWT.create(CommonWorkspaceConstants.class);
		switch (this) {
		case GeneralInfo:
			return constants.generalInfo();
		case PhysicalProfile:
			return constants.physicalProfile();
		case MedicalData:
			return constants.medicalData();
		case PersonalVehicle:
			return constants.personalVehicle();
		default:
			return "";
		}
	}
	
	/*
	 * Seen by IsSerializable
	 */
	Category(){
	}
}
