package epusp.pcs.os.superuser.client.rpc;

import java.util.Collection;

import com.google.gwt.user.client.rpc.AsyncCallback;

import epusp.pcs.os.shared.client.rpc.IConnectionServiceAsync;
import epusp.pcs.os.shared.general.MoveCursor;
import epusp.pcs.os.shared.model.person.user.Admin;
import epusp.pcs.os.shared.model.person.user.Monitor;
import epusp.pcs.os.shared.model.person.user.SuperUser;

public interface ISuperUserWorkspaceServiceAsync extends IConnectionServiceAsync{

	void getAdmins(MoveCursor move, int range,
			AsyncCallback<Collection<Admin>> callback);

	void getMonitors(MoveCursor move, int range,
			AsyncCallback<Collection<Monitor>> callback);

	void getSuperUsers(MoveCursor move, int range,
			AsyncCallback<Collection<SuperUser>> callback);

	void createMonitor(Monitor monitor, AsyncCallback<Void> callback);

	void createAdmin(Admin admin, AsyncCallback<Void> callback);

	void createSuperUser(SuperUser superUser, AsyncCallback<Void> callback);

}
