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
import epusp.pcs.os.shared.model.person.user.Agent;

public class AsyncAgentProvider extends AsyncDataProvider<Agent> {
	
	private int startRange = -1;
	
	private final int pageSize;

	private final IAdminWorkspaceServiceAsync rpcService;
	
	private final CellTable<Agent> table;
	
	private SimplePager pager;
	
	public AsyncAgentProvider(CellTable<Agent> table, SimplePager pager, IAdminWorkspaceServiceAsync rpcService, int pageSize){
		this.table = table;
		this.pager = pager;
		this.rpcService = rpcService;
		this.pageSize = pageSize;
	}


	@Override
	protected void onRangeChanged(HasData<Agent> display) {
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

		rpcService.getAgents(move, pageSize, new AsyncCallback<Collection<Agent>>() {

			@Override
			public void onSuccess(Collection<Agent> result) {
				List<Agent> agents = new ArrayList<Agent>();
				agents.addAll(result);

				if(!result.isEmpty()){
					if(!table.isRowCountExact() && result.size() < range.getLength()){
						table.setRowCount(totalNumberOfRows+pageSize, true); //it has to be multiple of page size
					}
					table.setRowData(start, agents);
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
