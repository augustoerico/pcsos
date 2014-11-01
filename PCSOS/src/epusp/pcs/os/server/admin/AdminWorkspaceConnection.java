package epusp.pcs.os.server.admin;

import javax.servlet.ServletException;

import epusp.pcs.os.admin.client.rpc.IAdminWorkspaceService;
import epusp.pcs.os.server.Connection;
import epusp.pcs.os.shared.model.person.user.AccountTypes;
import epusp.pcs.os.shared.model.person.user.User;

public class AdminWorkspaceConnection extends Connection implements IAdminWorkspaceService{

	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException{}

	@Override
	protected Boolean isLoggedIn() {
		if(super.isLoggedIn()){
			User user = (User) getSessionAttibute(userSessionAttribute);
			if(user.getType().equals(AccountTypes.Admin)){
				return true;
			}
		}
		return false;
	}
}
