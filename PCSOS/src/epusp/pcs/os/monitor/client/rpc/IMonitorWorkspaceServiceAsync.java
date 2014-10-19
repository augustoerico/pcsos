package epusp.pcs.os.monitor.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

import epusp.pcs.os.monitor.shared.EmergencyCallSpecs;
import epusp.pcs.os.shared.client.rpc.IConnectionServiceAsync;
import epusp.pcs.os.shared.model.oncall.EmergencyCall;

public interface IMonitorWorkspaceServiceAsync extends IConnectionServiceAsync {

	void getEmergencyCallDetails(EmergencyCallSpecs specs,
			AsyncCallback<EmergencyCall> callback);

	void addFreeMonitor(AsyncCallback<Void> callback);

	void isMonitorOnCall(AsyncCallback<Boolean> callback);
}
