package epusp.pcs.os.monitor.client.rpc;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import epusp.pcs.os.monitor.shared.EmergencyCallSpecs;
import epusp.pcs.os.shared.client.rpc.IConnectionService;
import epusp.pcs.os.shared.model.oncall.EmergencyCall;

@RemoteServiceRelativePath("monitorWorkspaceService")
public interface IMonitorWorkspaceService extends IConnectionService {

	EmergencyCall getEmergencyCallDetails(EmergencyCallSpecs specs);

	void addFreeMonitor();

	Boolean isMonitorOnCall();

}
