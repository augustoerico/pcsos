package epusp.pcs.os.shared.provider;

import java.util.Collection;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.rpc.AsyncCallback;

import epusp.pcs.os.shared.general.MoveCursor;
import epusp.pcs.os.shared.model.person.user.monitor.Monitor;
import epusp.pcs.os.superuser.client.rpc.ISuperUserWorkspaceServiceAsync;

public class AsyncMonitorProvider extends AsyncProvider<Monitor> {

	public AsyncMonitorProvider(CellTable<Monitor> table, SimplePager pager,
			final ISuperUserWorkspaceServiceAsync rpcService, final int pageSize) {
		super(table, pager, new IServiceProvider<Monitor>(){

			@Override
			public void serviceCall(MoveCursor move,
					AsyncCallback<Collection<Monitor>> callback) {
				rpcService.getMonitors(move, pageSize, callback);
			}}, pageSize);
	}
	
}
