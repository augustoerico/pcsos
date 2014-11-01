package epusp.pcs.os.admin.client;

import com.google.gwt.user.client.ui.HasWidgets;

import epusp.pcs.os.admin.client.constants.AdminWorkspaceConstants;
import epusp.pcs.os.admin.client.presenter.WorkspacePresenter;
import epusp.pcs.os.admin.client.rpc.IAdminWorkspaceServiceAsync;
import epusp.pcs.os.admin.client.view.Workspace;
import epusp.pcs.os.shared.client.presenter.Presenter;

public class WorkspaceController implements Presenter{

	private final IAdminWorkspaceServiceAsync rpcService;
	
	private final AdminWorkspaceConstants constants;
	
	public WorkspaceController(IAdminWorkspaceServiceAsync rpcService, AdminWorkspaceConstants constants){
		this.rpcService = rpcService;
		this.constants = constants;
	}
	
	@Override
	public void go(HasWidgets container) {
		container.clear();
		WorkspacePresenter workspacePresenter = new WorkspacePresenter(rpcService, new Workspace(), constants);
		workspacePresenter.go(container);
	}

}
