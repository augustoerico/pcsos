package epusp.pcs.os.admin.client;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

import epusp.pcs.os.admin.client.constants.AdminWorkspaceConstants;
import epusp.pcs.os.admin.client.rpc.IAdminWorkspaceServiceAsync;
import epusp.pcs.os.shared.client.presenter.ImageTabPresenter;
import epusp.pcs.os.shared.client.presenter.Presenter;
import epusp.pcs.os.shared.client.view.ImageTabPanel;

public class VehicleTableController implements Presenter{

	private final IAdminWorkspaceServiceAsync rpcService;
	private final AdminWorkspaceConstants constants;
	private final int pageSize;
//	private final CarTablePresenter carTablePresenter;
	
	public VehicleTableController(IAdminWorkspaceServiceAsync rpcService, AdminWorkspaceConstants constants, int pageSize){
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
//		CallInfo callInfo = new CallInfo();
//		callInfo.addControl(new Label("Helicopter"));
//		HelicopterTablePresenter helicopterTablePresenter = new HelicopterTablePresenter(rpcService, constants, pageSize);
//		helicopterTablePresenter.go(callInfo.asWidget());
//		;
		
		ImageTabPresenter imageTabPresenter = new ImageTabPresenter(new ImageTabPanel());
		imageTabPresenter.go(container);
		imageTabPresenter.addInfo("TAG 001", "http://images.clipartpanda.com/potato-clip-art-potato-clipart-1.png", new Label("yeap"));
		imageTabPresenter.addInfo("TAG 002", "http://www.doodleblob.com/Doodleblob_Old/Banana_Clipart_files/Banana%20Clipart%20Doodleblob.jpg", new Label("yeap2"));
		bind();
	}
	
	private void bind(){
		
	}
	
}
