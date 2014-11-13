package epusp.pcs.os.admin.client;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasWidgets;

import epusp.pcs.os.admin.client.constants.AdminWorkspaceConstants;
import epusp.pcs.os.admin.client.presenter.CarTablePresenter;
import epusp.pcs.os.admin.client.presenter.HelicopterTablePresenter;
import epusp.pcs.os.admin.client.rpc.IAdminWorkspaceServiceAsync;
import epusp.pcs.os.shared.client.presenter.ImageTabPresenter;
import epusp.pcs.os.shared.client.presenter.Presenter;
import epusp.pcs.os.shared.client.view.ImageTabPanel;
import epusp.pcs.os.shared.general.SelectedRowHandler;
import epusp.pcs.os.shared.model.vehicle.Car;
import epusp.pcs.os.shared.model.vehicle.Helicopter;
import epusp.pcs.os.shared.model.vehicle.VehicleTypes;

public class VehicleTableController implements Presenter{

	private final IAdminWorkspaceServiceAsync rpcService;
	private final AdminWorkspaceConstants constants;
	private final int pageSize;
	
	private final AdminResources resources = AdminResources.INSTANCE;
	
	public VehicleTableController(IAdminWorkspaceServiceAsync rpcService, AdminWorkspaceConstants constants, int pageSize){
		this.rpcService = rpcService;
		this.constants = constants;
		this.pageSize = pageSize;
	}
	
	@Override
	public void go(HasWidgets container) {		
		ImageTabPresenter imageTabPresenter = new ImageTabPresenter(new ImageTabPanel());
		imageTabPresenter.go(container);
		
		for(VehicleTypes type : VehicleTypes.values()){
			AbsolutePanel panel = new AbsolutePanel();
			panel.setSize("100%", "100%");
			switch (type) {
			case Car:
				CarTablePresenter carTablePresenter = new CarTablePresenter(rpcService, constants, pageSize, new SelectedRowHandler<Car>() {
					
					@Override
					public void onSelectedRow(Car objectSelected) {
						// TODO Auto-generated method stub
						System.out.println("todo");
					}
				});
				carTablePresenter.go(panel);
				imageTabPresenter.addInfo(constants.car(), resources.car().getSafeUri().asString(), panel);
				break;
			case Helicopter:
				HelicopterTablePresenter helicopterTablePresenter = new HelicopterTablePresenter(rpcService, constants, pageSize, new SelectedRowHandler<Helicopter>() {
					
					@Override
					public void onSelectedRow(Helicopter objectSelected) {
						// TODO Auto-generated method stub
						System.out.println("todo");
					}
				});
				helicopterTablePresenter.go(panel);
				imageTabPresenter.addInfo(constants.helicopter(), resources.helicopter().getSafeUri().asString(), panel);
				break;
			default:
				break;
			}
		}
		
		bind();
	}
	
	private void bind(){
		
	}
	
}
