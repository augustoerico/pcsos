package epusp.pcs.os.shared.provider;

import java.util.Collection;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.rpc.AsyncCallback;

import epusp.pcs.os.admin.client.rpc.IAdminWorkspaceServiceAsync;
import epusp.pcs.os.shared.general.MoveCursor;
import epusp.pcs.os.shared.model.vehicle.motorcycle.Motorcycle;

public class AsyncMotorcycleProvider  extends AsyncProvider<Motorcycle> {

	public AsyncMotorcycleProvider(CellTable<Motorcycle> table, SimplePager pager,
			 final IAdminWorkspaceServiceAsync rpcService, final int pageSize) {
		super(table, pager, new IServiceProvider<Motorcycle>(){

			@Override
			public void serviceCall(MoveCursor move,
					AsyncCallback<Collection<Motorcycle>> callback) {
				rpcService.getMotorcycles(move, pageSize, callback);
			}}, pageSize);
	}
	
}
