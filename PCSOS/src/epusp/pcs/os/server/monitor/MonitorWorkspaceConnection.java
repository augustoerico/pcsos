package epusp.pcs.os.server.monitor;

import epusp.pcs.os.model.person.user.AccountTypes;
import epusp.pcs.os.model.person.user.User;
import epusp.pcs.os.monitor.client.rpc.IMonitorWorkspaceService;
import epusp.pcs.os.server.Connection;

public class MonitorWorkspaceConnection extends Connection implements IMonitorWorkspaceService{

	private static final long serialVersionUID = 1L;

	@Override
	public String getUserPictureUrl(String key) {
		if(isLoggedIn(key)){
			User user = super.authenticationManager.getUser(key);
			return user.getPictureURL();
		}
		return null;
	}

	@Override
	protected Boolean isLoggedIn(String key) {
		if(super.isLoggedIn(key)){
			User user = super.authenticationManager.getUser(key);
			if(user.getType().equals(AccountTypes.Monitor)){
				return true;
			}
		}
		return false;
	}

}
