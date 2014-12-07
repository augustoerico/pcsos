package epusp.pcs.os.server.endpoint;

import javax.inject.Named;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import epusp.pcs.os.server.workflow.EmergencyCallWorkflow;
import epusp.pcs.os.shared.model.AgentCollection;
import epusp.pcs.os.shared.model.EmergencyCallLifecycleStatus;
import epusp.pcs.os.shared.model.oncall.EmergencyCall;
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
	public void addFreeVehicle(@Named("vehicleIdTag") String vehicleIdTag, AgentCollection agents) throws Exception {
		instance.addFreeVehicle(vehicleIdTag, agents.getAgentCollection());
	}

	@ApiMethod(name="updatePositionAndVerifyStatus")
	public EmergencyCall updatePositionAndVerifyStatus(@Named("vehicleIdTag") String vehicleIdTag, Position position) {
		instance.addVehiclePosition(vehicleIdTag, position);
		if(instance.isVehicleOnCall(vehicleIdTag))
			return instance.getVehicleEmergencyCall(vehicleIdTag);
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
	public EmergencyCallLifecycleStatus updatePositionAndVerifyCallStatus(@Named("vehicleIdTag") String vehicleIdTag, Position position, @Named("victimEmail") String victimEmail) {
		instance.addVehiclePosition(vehicleIdTag, position);
		EmergencyCallLifecycleStatus emCallStatus = new EmergencyCallLifecycleStatus();
		emCallStatus.setStatus(instance.getEmergencyCallLifecycle(victimEmail).name());
		return emCallStatus;
	}

	@ApiMethod(name="ackVehicleOnCall")
	public void ackVehicleOnCall(@Named("vehicleIdTag") String vehicleIdTag) {
		instance.vehicleOnCallAcknowledgment(vehicleIdTag);
	}
	
	@ApiMethod(name="ackVehicleFinishedCall")
	public void ackVehicleFinishedCall(@Named("vehicleIdTag") String vehicleIdTag) {
		instance.vehicleFinishedCallAcknowledgment(vehicleIdTag);
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
	public void addAgentsToVehicle(@Named("vehicleIdTag") String vehicleIdTag, AgentCollection agents) {
		instance.addAgentsToVehicle(vehicleIdTag, agents.getAgentCollection());
	}

	@ApiMethod(name="addAgentToVehicle")
	public void addAgentToVehicle(@Named("vehicleIdTag") String vehicleIdTag, Agent agent) {
		instance.addAgentToVehicle(vehicleIdTag, agent);
	}
	
	@ApiMethod(name="removeAllAgentsFromVehicle")
	public void removeAllAgentsFromVehicle(@Named("vehicleIdTag") String vehicleIdTag) {
		instance.removeAllAgentsFromVehicle(vehicleIdTag);
	}

	@ApiMethod(name="removeAgentFromVehicle")
	public void removeAgentFromVehicle(@Named("vehicleIdTag") String vehicleIdTag, Agent agent) {
		instance.removeAgentFromVehicle(vehicleIdTag, agent);
	}
	
	@ApiMethod(name="vehicleLeaving")
	public void vehicleLeaving(@Named("vehicleIdTag") String vehicleIdTag) {
		instance.vehicleLeaving(vehicleIdTag);
	}
	
}
