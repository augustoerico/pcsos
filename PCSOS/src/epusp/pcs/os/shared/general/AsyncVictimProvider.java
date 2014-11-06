package epusp.pcs.os.shared.general;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

import epusp.pcs.os.admin.client.rpc.IAdminWorkspaceServiceAsync;
import epusp.pcs.os.shared.model.person.Victim;

public class AsyncVictimProvider extends AsyncDataProvider<Victim> {

	private int startRange = -1;
	
	private final int pageSize;
	
	private IAdminWorkspaceServiceAsync rpcService;
	
	private final CellTable<Victim> table;
	
	private SimplePager pager;
	
	public AsyncVictimProvider(CellTable<Victim> table, SimplePager pager, IAdminWorkspaceServiceAsync rpcService, int pageSize){
		this.table = table;
		this.pager = pager;
		this.rpcService = rpcService;
		this.pageSize = pageSize;		
	}
	
	@Override
	protected void onRangeChanged(HasData<Victim> display) {
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
		
		rpcService.getVictims(move, pageSize, new AsyncCallback<Collection<Victim>>() {

			@Override
			public void onSuccess(Collection<Victim> result) {
				List<Victim> victims = new ArrayList<Victim>();
				victims.addAll(result);

				if(!result.isEmpty()){
					if(!table.isRowCountExact() && result.size() < range.getLength()){
						table.setRowCount(totalNumberOfRows+pageSize, true);
					}
					table.setRowData(start, victims);
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
