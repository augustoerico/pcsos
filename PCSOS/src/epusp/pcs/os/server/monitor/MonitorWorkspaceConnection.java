package epusp.pcs.os.server.monitor;

import epusp.pcs.os.model.person.user.AccountTypes;
import epusp.pcs.os.model.person.user.User;
import epusp.pcs.os.monitor.client.rpc.IMonitorWorkspaceService;
import epusp.pcs.os.server.Connection;

public class MonitorWorkspaceConnection extends Connection implements IMonitorWorkspaceService{

	private static final long serialVersionUID = 1L;

	@Override
	public String getUserPictureUrl() {
		if(isLoggedIn()){
			User user = (User) getThreadLocalRequest().getSession().getAttribute(userInfo);

			if(user.getType().equals(AccountTypes.Admin)){
				return user.getPictureURL();
			}
		}
		return null;
	}

}
