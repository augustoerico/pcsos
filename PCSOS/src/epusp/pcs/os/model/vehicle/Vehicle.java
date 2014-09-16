package epusp.pcs.os.model.vehicle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import epusp.pcs.os.model.SystemObject;
import epusp.pcs.os.model.person.user.Agent;

@PersistenceCapable(identityType=IdentityType.APPLICATION, detachable="true")
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class Vehicle extends SystemObject implements Serializable {
	
	@PrimaryKey
	@Persistent(valueStrategy=IdGeneratorStrategy.INCREMENT)
	private Long id;
	
	@NotPersistent
	private static final long serialVersionUID = 1L;

	@NotPersistent
	private List<Agent> agents = new ArrayList<Agent>();
	
	@NotPersistent
	private Boolean status;
	
	@NotPersistent
	private double latitude;
	
	@NotPersistent
	private double longitude;
	
	public Long getId(){
		return id;
	}
	
	public Boolean isAvailable() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}
	
	public List<Agent> getAgents() {
		return agents;
	}

	public void addAgent(Agent agent) {
		agents.add(agent);
	}
	
	public void removeAgents() {
		agents.clear();
	}
	
	public double getLatitude(){
		return latitude;
	}
	
	public double getLongitude(){
		return longitude;
	}
	
	public void setLatitude(double latitude){
		this.latitude =latitude;
	}
	
	public void setLongitude(double longitude){
		this.longitude = longitude;
	}
	
	public abstract VehicleTypes getType();
	
	public abstract int getMaxNumberOfOccupants();
	
	/*
	 * Seen by IsSerializable
	 */
	public Vehicle(){
		super();
	}
}
