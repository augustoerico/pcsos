package epusp.pcs.os.shared.model.vehicle;

import java.io.Serializable;

import com.google.gwt.core.client.GWT;

import epusp.pcs.os.shared.client.constants.CommonWorkspaceConstants;

public enum VehicleTypes implements Serializable {
	Car, Helicopter, Motorcycle;
	
	public String getText(){
		CommonWorkspaceConstants constants = GWT.create(CommonWorkspaceConstants.class);
		switch (this) {
		case Car:
			return constants.car();
		case Helicopter:
			return constants.helicopter();
		case Motorcycle:
			return constants.motorcycle();
		default:
			return "";
		}
	}
	
	public Class<? extends Vehicle> getTargetClass(){
		switch (this) {
		case Car:
			return epusp.pcs.os.shared.model.vehicle.car.Car.class;
		case Helicopter:
			return epusp.pcs.os.shared.model.vehicle.helicopter.Helicopter.class;
		case Motorcycle:
			return epusp.pcs.os.shared.model.vehicle.motorcycle.Motorcycle.class;
		default:
			return null;
		}	
	}
	
	/*
	 * Seen by IsSerializable
	 */
	VehicleTypes(){
	}
}
