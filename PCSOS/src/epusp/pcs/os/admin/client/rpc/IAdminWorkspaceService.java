package epusp.pcs.os.admin.client.rpc;

import java.util.Collection;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import epusp.pcs.os.shared.client.rpc.IConnectionService;
import epusp.pcs.os.shared.general.MoveCursor;
import epusp.pcs.os.shared.model.person.Victim;
import epusp.pcs.os.shared.model.person.user.Agent;

@RemoteServiceRelativePath("adminWorkspaceService")
public interface IAdminWorkspaceService extends IConnectionService {

	Collection<Victim> getVictims(MoveCursor move, int pageSize);

	Collection<Agent> getAgents(MoveCursor move, int range);
}
