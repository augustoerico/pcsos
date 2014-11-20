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

public class AsyncProvider<T> extends AsyncDataProvider<T> {	
	private int startRange = -1;
	
	private final int pageSize;
	
	private final CellTable<T> table;
	
	private final IServiceProvider<T> serviceProvider;
	
	private SimplePager pager;
	
	public AsyncProvider(CellTable<T> table, SimplePager pager, IServiceProvider<T> serviceProvider, int pageSize){
		this.table = table;
		this.pager = pager;
		this.pageSize = pageSize;
		this.serviceProvider = serviceProvider;
	}


	@Override
	protected void onRangeChanged(HasData<T> display) {
		final Range range = display.getVisibleRange();
		final int start = range.getStart();
		
		MoveCursor move = null;
		
		if(start == 0){
			move = MoveCursor.FIRST;
		}else if(start > startRange){
			move = MoveCursor.FORWARD;
		}else{
			move = MoveCursor.BACKWARD;
		}
		
		final MoveCursor moved = move;
		
		startRange = start;
		
		AsyncCallback<Collection<T>> callback = new AsyncCallback<Collection<T>>() {

			@Override
			public void onSuccess(Collection<T> result) {
				List<T> data = new ArrayList<T>();
				data.addAll(result);
				
				int totalNumberOfRows = table.getRowCount();
				
				if(result.isEmpty()){
					if(table.isRowCountExact())
						table.setRowCount(totalNumberOfRows-pageSize, true);
					else
						table.setRowCount(totalNumberOfRows, true);
					if(table.getRowCount() > 0)
						pager.lastPage();
				}else{
					table.setRowData(start, data);					
					if(moved.equals(MoveCursor.FORWARD)){
						if(pager.hasNextPage() == result.size() < range.getLength()){
							table.setRowCount(totalNumberOfRows+pageSize, true);
						}
					}
				}
			}

			@Override
			public void onFailure(Throwable caught) {
			}
		};
		
		serviceProvider.serviceCall(moved, callback);
	}


}
