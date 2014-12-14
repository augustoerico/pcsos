package epusp.pcs.os.server.endpoint;

import javax.inject.Named;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import epusp.pcs.os.server.workflow.EmergencyCallWorkflow;
import epusp.pcs.os.shared.model.AgentCollection;
import epusp.pcs.os.shared.model.EmCallWithVehicles;
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
		instance.addWaitingCall(victimEmail, position);
	}

	@ApiMethod(name="addVictimPosition")
	public void addVictimPosition(@Named("victimEmail") String victimEmail, Position position) throws Exception {
		instance.addVictimPosition(victimEmail, position);
	}

	@ApiMethod(name="addFreeVehicle")
	public void addFreeVehicle(@Named("vehicleTag") String vehicleTag, AgentCollection agents, @Named("position") String position) throws Exception {
		Double lat = Double.parseDouble(position.substring(0, position.indexOf(';')));
		Double longt = Double.parseDouble(position.substring(position.indexOf(';') + 1));
		Position pos = new Position(lat, longt);
		instance.addFreeVehicle(vehicleTag, agents.getAgentCollection(), pos);
	}

	@ApiMethod(name="updatePositionAndVerifyStatus")
	public EmergencyCall updatePositionAndVerifyStatus(@Named("vehicleTag") String vehicleTag, Position position) {
		instance.addVehiclePosition(vehicleTag, position);
		if(instance.isVehicleOnCall(vehicleTag))
			return instance.getVehicleEmergencyCall(vehicleTag);
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
	public EmCallWithVehicles updatePositionAndVerifyCallStatus(@Named("vehicleTag") String vehicleTag, Position position, @Named("victimEmail") String victimEmail) {
		instance.addVehiclePosition(vehicleTag, position);
		EmCallWithVehicles emCall = new EmCallWithVehicles();
		emCall.setStatus(instance.getEmergencyCallLifecycle(victimEmail).name());
		emCall.setEmCall(instance.getVehicleEmergencyCall(vehicleTag));
		return emCall;
	}

	@ApiMethod(name="ackVehicleOnCall")
	public void ackVehicleOnCall(@Named("vehicleTag") String vehicleTag) {
		instance.vehicleOnCallAcknowledgment(vehicleTag);
	}
	
	@ApiMethod(name="ackVehicleFinishedCall")
	public void ackVehicleFinishedCall(@Named("vehicleTag") String vehicleTag) {
		instance.vehicleFinishedCallAcknowledgment(vehicleTag);
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
	public void addAgentsToVehicle(@Named("vehicleTag") String vehicleTag, AgentCollection agents) {
		instance.addAgentsToVehicle(vehicleTag, agents.getAgentCollection());
	}

	@ApiMethod(name="addAgentToVehicle")
	public void addAgentToVehicle(@Named("vehicleTag") String vehicleTag, Agent agent) {
		instance.addAgentToVehicle(vehicleTag, agent);
	}
	
	@ApiMethod(name="removeAllAgentsFromVehicle")
	public void removeAllAgentsFromVehicle(@Named("vehicleTag") String vehicleTag) {
		instance.removeAllAgentsFromVehicle(vehicleTag);
	}

	@ApiMethod(name="removeAgentFromVehicle")
	public void removeAgentFromVehicle(@Named("vehicleTag") String vehicleTag, Agent agent) {
		instance.removeAgentFromVehicle(vehicleTag, agent);
	}
	
	@ApiMethod(name="vehicleLeaving")
	public void vehicleLeaving(@Named("vehicleTag") String vehicleTag) {
		instance.vehicleLeaving(vehicleTag);
	}
}
