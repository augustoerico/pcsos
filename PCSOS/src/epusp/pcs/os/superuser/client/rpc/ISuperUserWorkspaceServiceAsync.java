package epusp.pcs.os.superuser.client.rpc;

import java.util.Collection;

import com.google.gwt.user.client.rpc.AsyncCallback;

import epusp.pcs.os.shared.client.rpc.IConnectionServiceAsync;
import epusp.pcs.os.shared.general.MoveCursor;
import epusp.pcs.os.shared.model.person.user.Admin;

public interface ISuperUserWorkspaceServiceAsync extends IConnectionServiceAsync{

	void getAdmins(MoveCursor move, int range,
			AsyncCallback<Collection<Admin>> callback);

}
