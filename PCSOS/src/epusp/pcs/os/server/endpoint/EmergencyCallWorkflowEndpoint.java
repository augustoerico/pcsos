package epusp.pcs.os.server.endpoint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.inject.Named;
import javax.jdo.PersistenceManager;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import epusp.pcs.os.server.PMF;
import epusp.pcs.os.server.workflow.EmergencyCallWorkflow;
import epusp.pcs.os.shared.model.AgentCollection;
import epusp.pcs.os.shared.model.EmCallWithVehicles;
import epusp.pcs.os.shared.model.EmergencyCallLifecycleStatus;
import epusp.pcs.os.shared.model.MonitorAttributesMap;
import epusp.pcs.os.shared.model.VictimAttributesMap;
import epusp.pcs.os.shared.model.attribute.AttributeInfo;
import epusp.pcs.os.shared.model.attribute.IAttribute;
import epusp.pcs.os.shared.model.oncall.EmergencyCall;
import epusp.pcs.os.shared.model.oncall.Position;
import epusp.pcs.os.shared.model.person.user.agent.Agent;
import epusp.pcs.os.shared.model.person.user.monitor.Monitor;
import epusp.pcs.os.shared.model.person.user.monitor.MonitorCustomAttributes;
import epusp.pcs.os.shared.model.person.victim.Victim;
import epusp.pcs.os.shared.model.person.victim.VictimCustomAttributes;

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
		String[] latLong = position.split(";");
		if(latLong.length != 2)
			throw new Exception("Have you set a valid position?");
		Double lat = Double.valueOf(latLong[0]);
		Double longt = Double.valueOf(latLong[1]);
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
	public VictimAttributesMap getVictim(@Named("victimEmail") String victimEmail) {
		Victim v = instance.getVictim(victimEmail);
		HashMap<String, String> primaryMap = new HashMap<String, String>();
		HashMap<String, String> secondaryMap = new HashMap<String, String>();
		VictimAttributesMap attMap = new VictimAttributesMap();

		if(v != null) {
			String nome = v.getName() != null ? v.getName() + " " : "";
			nome = nome + (v.getSecondName() != null ? v.getSecondName() + " " : "");
			nome = nome + (v.getSurname() != null ? v.getSurname() : "");
			primaryMap.put("Nome", nome);
			primaryMap.put("Foto", v.getPictureURL());

			for(VictimCustomAttributes customAtt : VictimCustomAttributes.values()) {
				String name = customAtt.getAttributeName();
				AttributeInfo info = getAttributeInfo(name);
				IAttribute attribute = v.getAttribute(name);
				if(attribute != null) {
					if(info.isVisable(EmergencyCallWorkflowEndpoint.class.getSimpleName())) {
						String value = "";
						switch(info.getDataType()){
						case BOOLEAN:
							if((Boolean) attribute.getValue()){
								value = "Sim";
							}else{
								value = "Não";
							}
							break;
						case DATE:
							SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
							value = sdf.format((Date) attribute.getValue());
							break;
						default:
							value = attribute.toString();
							if(value.indexOf("/")  != -1)
								value = value.substring(0, value.indexOf("/"));
							break;
						}
						secondaryMap.put(info.getLabel("pt"), value);
					}
				}
			}
		}
		attMap.setPrimaryMap(primaryMap);
		attMap.setSecondaryMap(secondaryMap);
		return attMap;
	} 

	@ApiMethod(name="getMonitor")
	public MonitorAttributesMap getMonitor(@Named("monitorId") String monitorId) {
		Monitor m = instance.getMonitor(monitorId);
		HashMap<String, String> primaryMap = new HashMap<String, String>();
		HashMap<String, String> secondaryMap = new HashMap<String, String>();
		MonitorAttributesMap attMap = new MonitorAttributesMap();

		if(m != null) {
			String nome = m.getName() != null ? m.getName() + " " : "";
			nome = nome + (m.getSecondName() != null ? m.getSecondName() + " " : "");
			nome = nome + (m.getSurname() != null ? m.getSurname() : "");
			primaryMap.put("Nome", nome);
			primaryMap.put("Foto", m.getPictureURL());

			for(MonitorCustomAttributes customAtt : MonitorCustomAttributes.values()) {
				String name = customAtt.getAttributeName();
				AttributeInfo info = getAttributeInfo(name);
				IAttribute attribute = m.getAttribute(name);
				if(attribute != null) {
					if(info.isVisable(EmergencyCallWorkflowEndpoint.class.getSimpleName())) {
						String value = "";
						switch(info.getDataType()){
						case BOOLEAN:
							if((Boolean) attribute.getValue()){
								value = "Sim";
							}else{
								value = "Não";
							}
							break;
						case DATE:
							SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
							value = sdf.format((Date) attribute.getValue());
							break;
						default:
							value = attribute.toString();
							if(value.indexOf("/")  != -1)
								value = value.substring(0, value.indexOf("/"));
							break;
						}
						secondaryMap.put(info.getLabel("pt"), value);
					}
				}
			}
		}
		attMap.setPrimaryMap(primaryMap);
		attMap.setSecondaryMap(secondaryMap);
		return attMap;
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

	private AttributeInfo getAttributeInfo(String attributeName){
		PersistenceManager pm = PMF.get().getPersistenceManager();

		pm = PMF.get().getPersistenceManager();
		AttributeInfo attributeInfo = null, detached = null;
		try{
			pm.currentTransaction().begin();
			attributeInfo = pm.getObjectById(AttributeInfo.class, attributeName);
			detached = pm.detachCopy(attributeInfo);
			pm.currentTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			if (pm.currentTransaction().isActive())
				pm.currentTransaction().rollback();
		}finally{
			pm.close();
		}

		return detached;
	}
}
