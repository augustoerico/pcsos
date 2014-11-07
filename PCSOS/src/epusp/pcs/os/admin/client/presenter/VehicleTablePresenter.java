package epusp.pcs.os.admin.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;

import epusp.pcs.os.admin.client.constants.AdminWorkspaceConstants;
import epusp.pcs.os.admin.client.rpc.IAdminWorkspaceServiceAsync;
import epusp.pcs.os.shared.client.presenter.Presenter;

public class VehicleTablePresenter implements Presenter{

	private final IAdminWorkspaceServiceAsync rpcService;
	private final AdminWorkspaceConstants constants;
	private final int pageSize;
//	private final CarTablePresenter carTablePresenter;
	
	public VehicleTablePresenter(IAdminWorkspaceServiceAsync rpcService, AdminWorkspaceConstants constants, int pageSize){
		this.rpcService = rpcService;
		this.constants = constants;
		this.pageSize = pageSize;
//		carTablePresenter = new CarTablePresenter(rpcService, constants, pageSize);
	}
	
	@Override
	public void go(HasWidgets container) {
//		carTablePresenter.go(container);
//		HelicopterTablePresenter helicopterTablePresenter = new HelicopterTablePresenter(rpcService, constants, pageSize);
//		helicopterTablePresenter.go(container);
		//TODO: build ui to switch between tables.
		bind();
	}
	
	private void bind(){
		
	}
	
}
