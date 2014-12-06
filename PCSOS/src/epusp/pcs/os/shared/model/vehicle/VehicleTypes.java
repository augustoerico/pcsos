package epusp.pcs.os.shared.model.vehicle;

import java.io.Serializable;

import com.google.gwt.core.client.GWT;

import epusp.pcs.os.shared.client.constants.CommonWorkspaceConstants;

public enum VehicleTypes implements Serializable {
	Car, Helicopter;
	
	public String getText(){
		CommonWorkspaceConstants constants = GWT.create(CommonWorkspaceConstants.class);
		switch (this) {
		case Car:
			return constants.car();
		case Helicopter:
			return constants.helicopter();
		default:
			return "";
		}
	}
	/*
	 * Seen by IsSerializable
	 */
	VehicleTypes(){
	}
}
