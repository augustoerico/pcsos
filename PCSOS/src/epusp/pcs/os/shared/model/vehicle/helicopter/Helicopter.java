package epusp.pcs.os.shared.model.vehicle.helicopter;

import java.io.Serializable;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;

import epusp.pcs.os.shared.model.person.user.agent.Agent;
import epusp.pcs.os.shared.model.vehicle.Vehicle;
import epusp.pcs.os.shared.model.vehicle.VehicleTypes;

@PersistenceCapable(identityType=IdentityType.APPLICATION, detachable="true")
public class Helicopter extends Vehicle implements Serializable{
	
	@NotPersistent
	private static final long serialVersionUID = 1L;
	
	@NotPersistent
	public static int maxCarOccupants = 5;
	
	@NotPersistent
	private Agent pilot;
	
	public Helicopter(String idTag){
		super(idTag);
	}
	
	public Agent getPilot() {
		return pilot;
	}

	public void setPilot(Agent pilot) {
		this.pilot = pilot;
	}

	@Override
	public VehicleTypes getType() {
		return VehicleTypes.Helicopter;
	}

	@Override
	public int getMaxNumberOfOccupants() {
		return 0;
	}

	/*
	 * Seen by Serializable
	 */
	public Helicopter(){
		super();
	}
}
