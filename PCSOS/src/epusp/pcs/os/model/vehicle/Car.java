package epusp.pcs.os.model.vehicle;

import java.io.Serializable;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import epusp.pcs.os.model.person.user.Agent;

@PersistenceCapable(identityType=IdentityType.APPLICATION, detachable="true")
public class Car extends Vehicle implements Serializable{
	
	@NotPersistent
	private static final long serialVersionUID = 1L;

	@NotPersistent
	public static int maxCarOccupants = 5;
	
	@NotPersistent
	private Agent driver;
	
	@Persistent
	private String plate;
	
	public Car(String plate){
		this.plate = plate;
	}
	
	public Agent getDriver() {
		return driver;
	}

	public void setDriver(Agent driver) {
		this.driver = driver;
	}
	
	public String getPlate(){
		return plate;
	}
	
	@Override
	public VehicleTypes getType() {
		return VehicleTypes.Car;
	}

	@Override
	public int getMaxNumberOfOccupants() {
		return maxCarOccupants;
	}
	
	/*
	 * Seen by Serializable
	 */
	public Car() {
		super();
	}
}
