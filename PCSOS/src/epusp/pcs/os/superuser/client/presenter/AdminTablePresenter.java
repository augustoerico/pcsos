package epusp.pcs.os.superuser.client.presenter;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;

import epusp.pcs.os.shared.general.SelectedRowHandler;
import epusp.pcs.os.shared.model.person.user.Admin;
import epusp.pcs.os.shared.provider.AsyncAdminProvider;
import epusp.pcs.os.superuser.client.constants.SuperUserWorkspaceConstants;
import epusp.pcs.os.superuser.client.rpc.ISuperUserWorkspaceServiceAsync;

public class AdminTablePresenter extends UserTablePresenter{
	
	private final SingleSelectionModel<Admin> selectionModel = new SingleSelectionModel<Admin>(new ProvidesKey<Admin>() {
		@Override
		public Object getKey(Admin item) {
			return item == null ? null : item.getId();
		}
	});
	
	private final SelectedRowHandler<Admin> handler;

	public AdminTablePresenter(ISuperUserWorkspaceServiceAsync rpcService,
			SuperUserWorkspaceConstants constants, int pageSize, SelectedRowHandler<Admin> handler) {
		super(rpcService, constants, pageSize);
		this.handler = handler;
	}
	
	@Override
	public void go(HasWidgets container){
		super.go(container);
		
		AsyncAdminProvider agentProvider = new AsyncAdminProvider(getAdminTable(), getPager(), getRpcService(), getPageSize());
		agentProvider.addDataDisplay(getAdminTable());
		
		getAdminTable().setSelectionModel(selectionModel);
		
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
	protected CellTable<Admin> getAdminTable() {
		return (CellTable<Admin>) (CellTable<?>) getTable();
	}
}
