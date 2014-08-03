package epusp.pcs.os.model.vehicle;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import epusp.pcs.os.model.device.Device;
import epusp.pcs.os.model.person.user.Agent;

public abstract class Vehicle implements IsSerializable {
	
	private List<Agent> agents = new ArrayList<Agent>();
	
	private Boolean isAvailable;
	
	private Device device;
	
	public Vehicle(Device device){
		this.device = device;
	}

	public Device getDevice(){
		return device;
	}
	
	public void updateDevice(Device device){
		this.device = device;
	}
	
	public Boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
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
	
	public abstract VehicleTypes getType();
	
	public abstract int getMaxNumberOfOccupants();
	
	/*
	 * Seen by IsSerializable
	 */
	public Vehicle(){
		super();
	}
}
