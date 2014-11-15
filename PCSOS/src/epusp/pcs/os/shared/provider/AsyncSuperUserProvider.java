package epusp.pcs.os.shared.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

import epusp.pcs.os.shared.general.MoveCursor;
import epusp.pcs.os.shared.model.person.user.SuperUser;
import epusp.pcs.os.superuser.client.rpc.ISuperUserWorkspaceServiceAsync;

public class AsyncSuperUserProvider extends AsyncDataProvider<SuperUser> {
	private int startRange = -1;
	
	private final int pageSize;

	private final ISuperUserWorkspaceServiceAsync rpcService;
	
	private final CellTable<SuperUser> table;
	
	private SimplePager pager;
	
	public AsyncSuperUserProvider(CellTable<SuperUser> table, SimplePager pager, ISuperUserWorkspaceServiceAsync rpcService, int pageSize){
		this.table = table;
		this.pager = pager;
		this.rpcService = rpcService;
		this.pageSize = pageSize;
	}


	@Override
	protected void onRangeChanged(HasData<SuperUser> display) {
		final Range range = display.getVisibleRange();
		final int start = range.getStart();
		final int totalNumberOfRows = display.getRowCount();
		
		MoveCursor move = null;
		
		if(start == 0){
			move = MoveCursor.FIRST;
		}else if(start > startRange){
			move = MoveCursor.FORWARD;
		}else{
			move = MoveCursor.BACKWARD;
		}
		
		startRange = start;

		rpcService.getSuperUsers(move, pageSize, new AsyncCallback<Collection<SuperUser>>() {

			@Override
			public void onSuccess(Collection<SuperUser> result) {
				List<SuperUser> superusers = new ArrayList<SuperUser>();
				superusers.addAll(result);

				if(!result.isEmpty()){
					if(!table.isRowCountExact() && result.size() < range.getLength()){
						table.setRowCount(totalNumberOfRows+pageSize, true); //it has to be multiple of page size
					}
					table.setRowData(start, superusers);
				}else{
					if(!table.isRowCountExact())
						table.setRowCount(totalNumberOfRows, true);
					pager.lastPage();
				}
			}

			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}
}
