package epusp.pcs.os.shared.provider;

import java.util.Collection;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.rpc.AsyncCallback;

import epusp.pcs.os.admin.client.rpc.IAdminWorkspaceServiceAsync;
import epusp.pcs.os.shared.general.MoveCursor;
import epusp.pcs.os.shared.model.person.user.Agent;

public class AsyncAgentProvider extends AsyncProvider<Agent>{

	public AsyncAgentProvider(CellTable<Agent> table, SimplePager pager,
			final IAdminWorkspaceServiceAsync rpcService, final int pageSize) {
		super(table, pager, new IServiceProvider<Agent>() {
			@Override
			public void serviceCall(MoveCursor move,
					AsyncCallback<Collection<Agent>> callback) {
				rpcService.getAgents(move, pageSize, callback);
			}
		}, pageSize);
	}
}
