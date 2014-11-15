package epusp.pcs.os.superuser.client.presenter;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.HasWidgets;

import epusp.pcs.os.shared.model.person.user.Monitor;
import epusp.pcs.os.shared.provider.AsyncMonitorProvider;
import epusp.pcs.os.superuser.client.constants.SuperUserWorkspaceConstants;
import epusp.pcs.os.superuser.client.rpc.ISuperUserWorkspaceServiceAsync;

public class MonitorTablePresenter extends UserTablePresenter{

	public MonitorTablePresenter(ISuperUserWorkspaceServiceAsync rpcService,
			SuperUserWorkspaceConstants constants, int pageSize) {
		super(rpcService, constants, pageSize);
	}
	
	@Override
	public void go(HasWidgets container){
		super.go(container);
		
		AsyncMonitorProvider agentProvider = new AsyncMonitorProvider(getMonitorTable(), getPager(), getRpcService(), getPageSize());
		agentProvider.addDataDisplay(getMonitorTable());
		
		bind();
	}

	private void bind(){
		
	}
	
	@SuppressWarnings("unchecked")
	protected CellTable<Monitor> getMonitorTable() {
		return (CellTable<Monitor>) (CellTable<?>) getTable();
	}

}
