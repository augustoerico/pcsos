package epusp.pcs.os.monitor.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

import epusp.pcs.os.shared.client.rpc.IConnectionServiceAsync;

public interface IMonitorWorkspaceServiceAsync extends IConnectionServiceAsync {

	void getUserPictureUrl(String key, AsyncCallback<String> callback);

}
