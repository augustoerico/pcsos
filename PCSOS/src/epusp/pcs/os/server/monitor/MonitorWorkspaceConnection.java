package epusp.pcs.os.server.monitor;

import epusp.pcs.os.monitor.client.rpc.IMonitorWorkspaceService;
import epusp.pcs.os.server.Connection;
import epusp.pcs.os.shared.model.person.user.AccountTypes;
import epusp.pcs.os.shared.model.person.user.User;

public class MonitorWorkspaceConnection extends Connection implements IMonitorWorkspaceService{

	private static final long serialVersionUID = 1L;

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

}
