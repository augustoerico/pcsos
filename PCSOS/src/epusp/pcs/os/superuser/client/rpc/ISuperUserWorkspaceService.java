package epusp.pcs.os.superuser.client.rpc;

import java.util.Collection;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import epusp.pcs.os.shared.client.rpc.IConnectionService;
import epusp.pcs.os.shared.general.MoveCursor;
import epusp.pcs.os.shared.model.person.user.Admin;
import epusp.pcs.os.shared.model.person.user.Monitor;
import epusp.pcs.os.shared.model.person.user.SuperUser;

@RemoteServiceRelativePath("superUserWorkspaceService")
public interface ISuperUserWorkspaceService extends IConnectionService{

	Collection<Admin> getAdmins(MoveCursor move, int range);

	Collection<Monitor> getMonitors(MoveCursor move, int range);

	Collection<SuperUser> getSuperUsers(MoveCursor move, int range);

	void createMonitor(Monitor monitor);

	void createAdmin(Admin admin);

	void createSuperUser(SuperUser superUser);

}
