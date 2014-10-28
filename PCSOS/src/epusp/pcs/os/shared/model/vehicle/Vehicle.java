package epusp.pcs.os.shared.model.vehicle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import epusp.pcs.os.shared.model.SystemObject;
import epusp.pcs.os.shared.model.oncall.Position;
import epusp.pcs.os.shared.model.person.user.Agent;

@PersistenceCapable(identityType=IdentityType.APPLICATION, detachable="true")
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class Vehicle extends SystemObject implements Serializable {
	
	@Persistent
	private Priority prioraty;
	
	@NotPersistent
	private static final long serialVersionUID = 1L;
	
	@Persistent
    @Extension(vendorName="datanucleus", key="gae.pk-name", value="true")
	private String idTag;
	
	@Persistent
	private String imageURL;
	
	@NotPersistent
	private List<Agent> agents = new ArrayList<Agent>();
	
	@NotPersistent
	private Boolean status;
	
	@NotPersistent
	private Double latitude;
	
	@NotPersistent
	private Double longitude;
	
	public Vehicle(String idTag){
		this.idTag = idTag;
	}
	
	public Priority getPriority(){
		return prioraty;
	}
	
	public void setPrioraty(Priority prioraty){
		this.prioraty = prioraty;
	}
	
	public String getIdTag() {
		return idTag;
	}
	
	public Boolean isAvailable() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}
	
	public void setImageURL(String imageURL){
		this.imageURL = imageURL;
	}
	
	public String getImageURL(){
		return imageURL;
	}
	
	public List<Agent> getAgents() {
		return agents;
	}

	public void addAgent(Agent agent) {
		agents.add(agent);
	}
	
	public void removeAgent(Agent agent){
		agents.remove(agent);
	}
	
	public void addAgents(List<Agent> agents){
		this.agents.addAll(agents);
	}
	
	public void removeAgents() {
		agents.clear();
	}
	
	public Position getPosition(){
		return new Position(latitude, longitude);
	}
	
	public void setPosition(Position position){
		this.latitude = position.getLatitude();
		this.longitude = position.getLongitude();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Vehicle){
			Vehicle vehicle = (Vehicle) obj;
			return getId().equals(vehicle.getId());
		}
		return false;
	}
	
	@Override
	public String toString() {
		return getId();
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
