package epusp.pcs.os.superuser.client.presenter;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.HasWidgets;

import epusp.pcs.os.shared.model.person.user.Admin;
import epusp.pcs.os.shared.provider.AsyncAdminProvider;
import epusp.pcs.os.superuser.client.constants.SuperUserWorkspaceConstants;
import epusp.pcs.os.superuser.client.rpc.ISuperUserWorkspaceServiceAsync;

public class AdminTablePresenter extends UserTablePresenter{

	public AdminTablePresenter(ISuperUserWorkspaceServiceAsync rpcService,
			SuperUserWorkspaceConstants constants, int pageSize) {
		super(rpcService, constants, pageSize);
	}
	
	@Override
	public void go(HasWidgets container){
		super.go(container);
		
		AsyncAdminProvider agentProvider = new AsyncAdminProvider(getAdminTable(), getPager(), getRpcService(), getPageSize());
		agentProvider.addDataDisplay(getAdminTable());
		
		bind();
	}

	private void bind(){
		
	}
	
	@SuppressWarnings("unchecked")
	protected CellTable<Admin> getAdminTable() {
		return (CellTable<Admin>) (CellTable<?>) getTable();
	}
}
