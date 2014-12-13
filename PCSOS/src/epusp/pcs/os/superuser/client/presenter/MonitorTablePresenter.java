package epusp.pcs.os.superuser.client.presenter;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;

import epusp.pcs.os.shared.client.SharedResources;
import epusp.pcs.os.shared.general.SelectedRowHandler;
import epusp.pcs.os.shared.model.person.user.monitor.Monitor;
import epusp.pcs.os.shared.provider.AsyncMonitorProvider;
import epusp.pcs.os.superuser.client.constants.SuperUserWorkspaceConstants;
import epusp.pcs.os.superuser.client.rpc.ISuperUserWorkspaceServiceAsync;

public class MonitorTablePresenter extends UserTablePresenter{

	private final SingleSelectionModel<Monitor> selectionModel = new SingleSelectionModel<Monitor>(new ProvidesKey<Monitor>() {
		@Override
		public Object getKey(Monitor item) {
			return item == null ? null : item.getId();
		}
	});
	
	private final SelectedRowHandler<Monitor> handler;
	
	public MonitorTablePresenter(ISuperUserWorkspaceServiceAsync rpcService,
			SuperUserWorkspaceConstants constants, SharedResources resources, int pageSize, SelectedRowHandler<Monitor> handler) {
		super(rpcService, constants, resources, pageSize);
		this.handler = handler;
	}
	
	@Override
	public void go(HasWidgets container){
		super.go(container);
		
		AsyncMonitorProvider agentProvider = new AsyncMonitorProvider(getMonitorTable(), getPager(), getRpcService(), getPageSize());
		agentProvider.addDataDisplay(getMonitorTable());
		
		getMonitorTable().setSelectionModel(selectionModel);
		
		bind();
	}

	private void bind(){
		selectionModel.addSelectionChangeHandler(new Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				handler.onSelectedRow(selectionModel.getSelectedObject());
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	protected CellTable<Monitor> getMonitorTable() {
		return (CellTable<Monitor>) (CellTable<?>) getTable();
	}

}
