package epusp.pcs.os.monitor.client.rpc;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import epusp.pcs.os.shared.client.rpc.IConnectionService;

@RemoteServiceRelativePath("monitorWorkspaceService")
public interface IMonitorWorkspaceService extends IConnectionService {

	String getUserPictureUrl();
	
}
