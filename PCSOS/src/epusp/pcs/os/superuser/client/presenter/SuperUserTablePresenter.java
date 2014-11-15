package epusp.pcs.os.superuser.client.presenter;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.HasWidgets;

import epusp.pcs.os.shared.model.person.user.SuperUser;
import epusp.pcs.os.shared.provider.AsyncSuperUserProvider;
import epusp.pcs.os.superuser.client.constants.SuperUserWorkspaceConstants;
import epusp.pcs.os.superuser.client.rpc.ISuperUserWorkspaceServiceAsync;

public class SuperUserTablePresenter extends UserTablePresenter{

	public SuperUserTablePresenter(ISuperUserWorkspaceServiceAsync rpcService,
			SuperUserWorkspaceConstants constants, int pageSize) {
		super(rpcService, constants, pageSize);
	}
	
	@Override
	public void go(HasWidgets container){
		super.go(container);
		
		AsyncSuperUserProvider agentProvider = new AsyncSuperUserProvider(getSuperUserTable(), getPager(), getRpcService(), getPageSize());
		agentProvider.addDataDisplay(getSuperUserTable());
		
		bind();
	}

	private void bind(){
		
	}
	
	@SuppressWarnings("unchecked")
	protected CellTable<SuperUser> getSuperUserTable() {
		return (CellTable<SuperUser>) (CellTable<?>) getTable();
	}

}
