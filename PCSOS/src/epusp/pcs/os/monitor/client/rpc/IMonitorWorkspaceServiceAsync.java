package epusp.pcs.os.monitor.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import epusp.pcs.os.monitor.shared.EmergencyCallSpecs;
import epusp.pcs.os.shared.client.rpc.IConnectionServiceAsync;
import epusp.pcs.os.shared.model.oncall.EmergencyCall;
import epusp.pcs.os.shared.model.vehicle.Vehicle;

public interface IMonitorWorkspaceServiceAsync extends IConnectionServiceAsync {

	void getEmergencyCallDetails(EmergencyCallSpecs specs,
			AsyncCallback<EmergencyCall> callback);

	void addFreeMonitor(AsyncCallback<Void> callback);

	void isMonitorOnCall(AsyncCallback<Boolean> callback);

	void getAvailableSupportVehicles(AsyncCallback<List<Vehicle>> callback);

	void addVehiclesToCall(List<String> vehiclesIdTag,
			AsyncCallback<Void> callback);

	void finishCall(AsyncCallback<Void> callback);

	void finishCallAcknowledgment(AsyncCallback<Void> callback);

	void monitorLeaving(AsyncCallback<Boolean> callback);
}
