package epusp.pcs.os.server.endpoint;

import javax.inject.Named;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import epusp.pcs.os.server.workflow.EmergencyCallWorkflow;
import epusp.pcs.os.shared.model.AgentCollection;
import epusp.pcs.os.shared.model.oncall.EmergencyCall;
import epusp.pcs.os.shared.model.oncall.Position;
import epusp.pcs.os.shared.model.person.user.Agent;
import epusp.pcs.os.shared.model.vehicle.Vehicle;

@Api(name = "emcallworkflowendpoint", namespace = @ApiNamespace(ownerDomain = "pcs.epusp", ownerName = "pcs.epusp", packagePath = "os.workflow"))
public class EmergencyCallWorkflowEndpoint {

	EmergencyCallWorkflow instance = EmergencyCallWorkflow.getInstance();

	@ApiMethod(name="addWaitingCall")
	public void addWaitingCall(@Named("victimEmail") String victimEmail, Position position) throws Exception {
		instance.addWaitingCall(victimEmail);
		instance.addVictimPosition(victimEmail, position);
	}

	@ApiMethod(name="addVictimPosition")
	public void addVictimPosition(@Named("victimEmail") String victimEmail, Position position) throws Exception {
		instance.addVictimPosition(victimEmail, position);
	}

	@ApiMethod(name="addFreeVehicle")
	public void addFreeVehicle(@Named("vehicleId") String vehicleId, AgentCollection agents) throws Exception {
		Key k = KeyFactory.createKey(Vehicle.class.getSimpleName(), vehicleId);
		String strK = KeyFactory.keyToString(k);
		instance.addFreeVehicle(strK, agents.getAgentCollection());
	}

	@ApiMethod(name="updatePositionAndVerifyStatus")
	public EmergencyCall updatePositionAndVerifyStatus(@Named("vehicleId") String vehicleId, Position position) {
		Key k = KeyFactory.createKey(Vehicle.class.getSimpleName(), vehicleId);
		String strK = KeyFactory.keyToString(k);
		instance.addVehiclePosition(strK, position);
		if(instance.isVehicleOnCall(strK))
			return instance.getVehicleEmergencyCall(strK);
		else
			return null;
	}

	@ApiMethod(name="ackVehicleOnCall")
	public void ackVehicleOnCall(@Named("vehicleId") String vehicleId) {
		Key k = KeyFactory.createKey(Vehicle.class.getSimpleName(), vehicleId);
		String strK = KeyFactory.keyToString(k);
		instance.vehicleOnCallAcknowledgment(strK);
	}
	
	@ApiMethod(name="ackVehicleFinishedCall")
	public void ackVehicleFinishedCall(@Named("vehicleId") String vehicleId) {
		Key k = KeyFactory.createKey(Vehicle.class.getSimpleName(), vehicleId);
		String strK = KeyFactory.keyToString(k);
		instance.vehicleFinishedCallAcknowledgment(strK);
	}
	
	@ApiMethod(name="addAgentsToVehicle")
	public void addAgentsToVehicle(@Named("vehicleId") String vehicleId, AgentCollection agents) {
		instance.addAgentsToVehicle(vehicleId, agents.getAgentCollection());
	}

	@ApiMethod(name="addAgentToVehicle")
	public void addAgentToVehicle(@Named("vehicleId") String vehicleId, Agent agent) {
		Key k = KeyFactory.createKey(Vehicle.class.getSimpleName(), vehicleId);
		String strK = KeyFactory.keyToString(k);
		instance.addAgentToVehicle(strK, agent);
	}
	
	@ApiMethod(name="removeAllAgentsFromVehicle")
	public void removeAllAgentsFromVehicle(@Named("vehicleId") String vehicleId) {
		Key k = KeyFactory.createKey(Vehicle.class.getSimpleName(), vehicleId);
		String strK = KeyFactory.keyToString(k);
		instance.removeAllAgentsFromVehicle(strK);
	}

	@ApiMethod(name="removeAgentFromVehicle")
	public void removeAgentFromVehicle(@Named("vehicleId") String vehicleId, Agent agent) {
		Key k = KeyFactory.createKey(Vehicle.class.getSimpleName(), vehicleId);
		String strK = KeyFactory.keyToString(k);
		instance.removeAgentFromVehicle(strK, agent);
	}
}
