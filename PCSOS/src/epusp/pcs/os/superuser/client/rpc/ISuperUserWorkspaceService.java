package epusp.pcs.os.superuser.client.rpc;

import java.util.Collection;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import epusp.pcs.os.shared.client.rpc.IConnectionService;
import epusp.pcs.os.shared.general.MoveCursor;
import epusp.pcs.os.shared.model.person.user.Admin;

@RemoteServiceRelativePath("superUserWorkspaceService")
public interface ISuperUserWorkspaceService extends IConnectionService{

	Collection<Admin> getAdmins(MoveCursor move, int range);

}
