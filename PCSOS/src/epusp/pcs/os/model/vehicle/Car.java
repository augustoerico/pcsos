package epusp.pcs.os.model.vehicle;

import com.google.gwt.user.client.rpc.IsSerializable;

import epusp.pcs.os.model.person.user.Agent;


public class Car extends Vehicle implements IsSerializable{
	
	public static final VehicleTypes vehicleType = VehicleTypes.Car;

	public static int maxCarOccupants = 5;
	
	private Agent driver;
	
	public Agent getDriver() {
		return driver;
	}

	public void setDriver(Agent driver) {
		this.driver = driver;
	}
	
	@Override
	public VehicleTypes getType() {
		return vehicleType;
	}

	@Override
	public int getMaxNumberOfOccupants() {
		return maxCarOccupants;
	}
	
	/*
	 * Seen by IsSerializable
	 */
	public Car() {
		super();
	}
}
