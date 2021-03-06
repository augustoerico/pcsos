package epusp.pcs.os.superuser.client.rpc;

import java.util.Collection;

import com.google.gwt.user.client.rpc.AsyncCallback;

import epusp.pcs.os.shared.client.rpc.IConnectionServiceAsync;
import epusp.pcs.os.shared.general.MoveCursor;
import epusp.pcs.os.shared.model.person.user.admin.Admin;
import epusp.pcs.os.shared.model.person.user.monitor.Monitor;
import epusp.pcs.os.shared.model.person.user.superuser.SuperUser;

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

	void getFullMonitor(String id, AsyncCallback<Monitor> asyncCallback);

	void getFullAdmin(String id, AsyncCallback<Admin> asyncCallback);

	void getFullSuperUser(String id, AsyncCallback<SuperUser> asyncCallback);

}
