package epusp.pcs.os.shared.model.vehicle.motorcycle;

import java.io.Serializable;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import epusp.pcs.os.shared.model.person.user.agent.Agent;
import epusp.pcs.os.shared.model.vehicle.Vehicle;
import epusp.pcs.os.shared.model.vehicle.VehicleTypes;

@PersistenceCapable(identityType=IdentityType.APPLICATION, detachable="true")
public class Motorcycle extends Vehicle implements Serializable{

	@NotPersistent
	private static final long serialVersionUID = 1L;
			
	@NotPersistent
	public static int maxMotercycleOccupants = 1;

	@NotPersistent
	private Agent driver;
	
	@Persistent
	private String plate;
	
	public Motorcycle(String idTag, String plate){
		super(idTag);
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
		return VehicleTypes.Motorcycle;
	}

	@Override
	public int getMaxNumberOfOccupants() {
		return maxMotercycleOccupants;
	}
	

	/*
	 * Seen by Serializable
	 */
	public Motorcycle(){
		super();
	}

}
