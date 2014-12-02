package epusp.pcs.os.shared.provider;

import java.util.Collection;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.rpc.AsyncCallback;

import epusp.pcs.os.admin.client.rpc.IAdminWorkspaceServiceAsync;
import epusp.pcs.os.shared.general.MoveCursor;
import epusp.pcs.os.shared.model.vehicle.helicopter.Helicopter;

public class AsyncHelicopterProvider  extends AsyncProvider<Helicopter> {

	public AsyncHelicopterProvider(CellTable<Helicopter> table,
			SimplePager pager, final IAdminWorkspaceServiceAsync rpcService,
			final int pageSize) {
		super(table, pager, new IServiceProvider<Helicopter>() {
			@Override
			public void serviceCall(MoveCursor move,
					AsyncCallback<Collection<Helicopter>> callback) {
				rpcService.getHelicopters(move, pageSize, callback);
			}
		}, pageSize);
	}

}
