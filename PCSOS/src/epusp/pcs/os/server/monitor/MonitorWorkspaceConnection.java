package epusp.pcs.os.server.monitor;

import javax.servlet.ServletException;

import epusp.pcs.os.monitor.client.rpc.IMonitorWorkspaceService;
import epusp.pcs.os.monitor.shared.EmergencyCallSpecs;
import epusp.pcs.os.server.Connection;
import epusp.pcs.os.shared.model.oncall.EmergencyCall;
import epusp.pcs.os.shared.model.person.user.AccountTypes;
import epusp.pcs.os.shared.model.person.user.Monitor;
import epusp.pcs.os.shared.model.person.user.User;

public class MonitorWorkspaceConnection extends Connection implements IMonitorWorkspaceService{

	private static final long serialVersionUID = 1L;
	
	@Override
	public void init() throws ServletException{}

	@Override
	protected Boolean isLoggedIn() {
		if(super.isLoggedIn()){
			User user = (User) getSessionAttibute(userSessionAttribute);
			if(user.getType().equals(AccountTypes.Monitor)){
				return true;
			}
		}
		return false;
	}

	@Override
	public EmergencyCall getEmergencyCallDetails(EmergencyCallSpecs specs){
		if(isLoggedIn()){
			Monitor monitor = (Monitor) getSessionAttibute(userSessionAttribute);
			if(specs.getVictimLastPositionIndex() == -1 && specs.getVehiclesLastPositionsIndex().isEmpty())
				return workflow.getMonitorEmergencyCall(monitor.getId());
			else
				return workflow.getMonitorEmergencyCall(monitor.getId(), specs.getVehiclesLastPositionsIndex(), specs.getVictimLastPositionIndex());
		}
		return null;
	}

	@Override
	public void addFreeMonitor(){
		if(isLoggedIn()){
			Monitor monitor = (Monitor) getSessionAttibute(userSessionAttribute);
			workflow.addFreeMonitor(monitor.getId());
		}
	}

	@Override
	public Boolean isMonitorOnCall(){
		if(isLoggedIn()){
			Monitor monitor = (Monitor) getSessionAttibute(userSessionAttribute);
			Boolean onCall = workflow.isMonitorOnCall(monitor.getId());
			if(onCall)
				workflow.monitorOnCallAcknowledgment(monitor.getId());
			return onCall;
		}
		return null;
	}
}
