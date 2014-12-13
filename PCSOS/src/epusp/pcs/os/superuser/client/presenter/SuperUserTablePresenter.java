package epusp.pcs.os.superuser.client.presenter;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;

import epusp.pcs.os.shared.client.SharedResources;
import epusp.pcs.os.shared.general.SelectedRowHandler;
import epusp.pcs.os.shared.model.person.user.superuser.SuperUser;
import epusp.pcs.os.shared.provider.AsyncSuperUserProvider;
import epusp.pcs.os.superuser.client.constants.SuperUserWorkspaceConstants;
import epusp.pcs.os.superuser.client.rpc.ISuperUserWorkspaceServiceAsync;

public class SuperUserTablePresenter extends UserTablePresenter{

	private final SingleSelectionModel<SuperUser> selectionModel = new SingleSelectionModel<SuperUser>(new ProvidesKey<SuperUser>() {
		@Override
		public Object getKey(SuperUser item) {
			return item == null ? null : item.getId();
		}
	});
	
	private final SelectedRowHandler<SuperUser> handler;
	
	public SuperUserTablePresenter(ISuperUserWorkspaceServiceAsync rpcService,
			SuperUserWorkspaceConstants constants, SharedResources resources, int pageSize, SelectedRowHandler<SuperUser> handler) {
		super(rpcService, constants, resources, pageSize);
		this.handler = handler;
	}
	
	@Override
	public void go(HasWidgets container){
		super.go(container);
		
		AsyncSuperUserProvider agentProvider = new AsyncSuperUserProvider(getSuperUserTable(), getPager(), getRpcService(), getPageSize());
		agentProvider.addDataDisplay(getSuperUserTable());
		
		getSuperUserTable().setSelectionModel(selectionModel);
		
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
	protected CellTable<SuperUser> getSuperUserTable() {
		return (CellTable<SuperUser>) (CellTable<?>) getTable();
	}

}
