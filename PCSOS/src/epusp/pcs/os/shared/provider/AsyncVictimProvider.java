package epusp.pcs.os.shared.provider;

import java.util.Collection;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.rpc.AsyncCallback;

import epusp.pcs.os.admin.client.rpc.IAdminWorkspaceServiceAsync;
import epusp.pcs.os.shared.general.MoveCursor;
import epusp.pcs.os.shared.model.person.Victim;

public class AsyncVictimProvider extends AsyncProvider<Victim> {

	public AsyncVictimProvider(CellTable<Victim> table, SimplePager pager,
			final IAdminWorkspaceServiceAsync rpcService, final int pageSize) {
		super(table, pager, new IServiceProvider<Victim>(){

			@Override
			public void serviceCall(MoveCursor move,
					AsyncCallback<Collection<Victim>> callback) {
				rpcService.getVictims(move, pageSize, callback);
			}}, pageSize);
	}
	
}
