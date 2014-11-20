package epusp.pcs.os.shared.provider;

import java.util.Collection;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.rpc.AsyncCallback;

import epusp.pcs.os.shared.general.MoveCursor;
import epusp.pcs.os.shared.model.person.user.SuperUser;
import epusp.pcs.os.superuser.client.rpc.ISuperUserWorkspaceServiceAsync;

public class AsyncSuperUserProvider extends AsyncProvider<SuperUser> {

	public AsyncSuperUserProvider(CellTable<SuperUser> table,
			SimplePager pager, final ISuperUserWorkspaceServiceAsync rpcService,
			final int pageSize) {
		super(table, pager, new IServiceProvider<SuperUser>(){

			@Override
			public void serviceCall(MoveCursor move,
					AsyncCallback<Collection<SuperUser>> callback) {
				rpcService.getSuperUsers(move, pageSize, callback);
			}}, pageSize);
	}
}
