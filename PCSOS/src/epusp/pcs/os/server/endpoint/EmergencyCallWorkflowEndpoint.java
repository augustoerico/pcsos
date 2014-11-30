package epusp.pcs.os.server.endpoint;

import javax.inject.Named;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import epusp.pcs.os.server.workflow.EmergencyCallWorkflow;
import epusp.pcs.os.shared.model.AgentCollection;
import epusp.pcs.os.shared.model.EmergencyCallLifecycleStatus;
import epusp.pcs.os.shared.model.oncall.EmergencyCall;
import epusp.pcs.os.shared.model.oncall.EmergencyCallLifecycle;
import epusp.pcs.os.shared.model.oncall.Position;
import epusp.pcs.os.shared.model.person.user.agent.Agent;
import epusp.pcs.os.shared.model.person.user.monitor.Monitor;
import epusp.pcs.os.shared.model.person.victim.Victim;

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
		instance.addFreeVehicle(vehicleId, agents.getAgentCollection());
	}

	@ApiMethod(name="updatePositionAndVerifyStatus")
	public EmergencyCall updatePositionAndVerifyStatus(@Named("vehicleId") String vehicleId, Position position) {
		instance.addVehiclePosition(vehicleId, position);
		if(instance.isVehicleOnCall(vehicleId))
			return instance.getVehicleEmergencyCall(vehicleId);
		else
			return null;
	}
	
	@ApiMethod(name="updateVictimPositionAndVerifyStatus")
	public EmergencyCallLifecycleStatus updateVictimPositionAndVerifyStatus(@Named("victimEmail") String victimEmail, Position position) {
		instance.addVictimPosition(victimEmail, position);
		EmergencyCallLifecycleStatus emCallStatus = new EmergencyCallLifecycleStatus();
		emCallStatus.setStatus(instance.getEmergencyCallLifecycle(victimEmail).name());
		return emCallStatus;
	}
	
	@ApiMethod(name="updatePositionAndVerifyCallStatus")
	public EmergencyCallLifecycleStatus updatePositionAndVerifyCallStatus(@Named("vehicleId") String vehicleId, Position position, @Named("victimEmail") String victimEmail) {
		instance.addVehiclePosition(vehicleId, position);
		EmergencyCallLifecycleStatus emCallStatus = new EmergencyCallLifecycleStatus();
		emCallStatus.setStatus(instance.getEmergencyCallLifecycle(victimEmail).name());
		return emCallStatus;
	}

	@ApiMethod(name="ackVehicleOnCall")
	public void ackVehicleOnCall(@Named("vehicleId") String vehicleId) {
		instance.vehicleOnCallAcknowledgment(vehicleId);
	}
	
	@ApiMethod(name="ackVehicleFinishedCall")
	public void ackVehicleFinishedCall(@Named("vehicleId") String vehicleId) {
		instance.vehicleFinishedCallAcknowledgment(vehicleId);
	}
	
	@ApiMethod(name="getVictim")
	public Victim getVictim(@Named("victimEmail") String victimEmail) {
		return instance.getVictim(victimEmail);
	}
	
	@ApiMethod(name="getMonitor")
	public Monitor getMonitor(@Named("monitorId") String monitorId) {
		return instance.getMonitor(monitorId);
	}
	
	@ApiMethod(name="addAgentsToVehicle")
	public void addAgentsToVehicle(@Named("vehicleId") String vehicleId, AgentCollection agents) {
		instance.addAgentsToVehicle(vehicleId, agents.getAgentCollection());
	}

	@ApiMethod(name="addAgentToVehicle")
	public void addAgentToVehicle(@Named("vehicleId") String vehicleId, Agent agent) {
		instance.addAgentToVehicle(vehicleId, agent);
	}
	
	@ApiMethod(name="removeAllAgentsFromVehicle")
	public void removeAllAgentsFromVehicle(@Named("vehicleId") String vehicleId) {
		instance.removeAllAgentsFromVehicle(vehicleId);
	}

	@ApiMethod(name="removeAgentFromVehicle")
	public void removeAgentFromVehicle(@Named("vehicleId") String vehicleId, Agent agent) {
		instance.removeAgentFromVehicle(vehicleId, agent);
	}
}
