package epusp.pcs.os.shared.provider;

import java.util.Collection;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.rpc.AsyncCallback;

import epusp.pcs.os.shared.general.MoveCursor;
import epusp.pcs.os.shared.model.person.user.Admin;
import epusp.pcs.os.superuser.client.rpc.ISuperUserWorkspaceServiceAsync;

public class AsyncAdminProvider extends AsyncProvider<Admin> {

	public AsyncAdminProvider(CellTable<Admin> table, SimplePager pager,
			final ISuperUserWorkspaceServiceAsync rpcService, final int pageSize) {
		super(table, pager, new IServiceProvider<Admin>() {

			@Override
			public void serviceCall(MoveCursor move,
					AsyncCallback<Collection<Admin>> callback) {
				rpcService.getAdmins(move, pageSize, callback);
			}
		}, pageSize);
	}

	

}
