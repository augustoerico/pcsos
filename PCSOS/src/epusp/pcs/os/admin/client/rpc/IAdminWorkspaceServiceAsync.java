package epusp.pcs.os.admin.client.rpc;

import java.util.Collection;

import com.google.gwt.user.client.rpc.AsyncCallback;

import epusp.pcs.os.shared.client.rpc.IConnectionServiceAsync;
import epusp.pcs.os.shared.general.MoveCursor;
import epusp.pcs.os.shared.model.person.Victim;
import epusp.pcs.os.shared.model.person.user.Agent;

public interface IAdminWorkspaceServiceAsync extends IConnectionServiceAsync {

	void getVictims(AsyncCallback<Collection<Victim>> callback);

	void getAgents(MoveCursor move, AsyncCallback<Collection<Agent>> callback);

}
