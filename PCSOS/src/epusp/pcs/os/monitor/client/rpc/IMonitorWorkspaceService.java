package epusp.pcs.os.monitor.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import epusp.pcs.os.monitor.shared.EmergencyCallSpecs;
import epusp.pcs.os.shared.client.rpc.IConnectionService;
import epusp.pcs.os.shared.model.oncall.EmergencyCall;
import epusp.pcs.os.shared.model.vehicle.Vehicle;

@RemoteServiceRelativePath("monitorWorkspaceService")
public interface IMonitorWorkspaceService extends IConnectionService {

	EmergencyCall getEmergencyCallDetails(EmergencyCallSpecs specs);

	void addFreeMonitor();

	Boolean isMonitorOnCall();

	List<Vehicle> getAvailableSupportVehicles();

	void addVehiclesToCall(List<String> vehiclesIdTag);

	void finishCall();

	void finishCallAcknowledgment();

	Boolean monitorLeaving();

}
