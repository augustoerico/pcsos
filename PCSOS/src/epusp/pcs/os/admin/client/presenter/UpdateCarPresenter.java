package epusp.pcs.os.admin.client.presenter;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.user.client.ui.HasWidgets;

import epusp.pcs.os.shared.client.constants.CommonWorkspaceConstants;
import epusp.pcs.os.shared.client.rpc.IConnectionServiceAsync;
import epusp.pcs.os.shared.model.vehicle.Car;
import epusp.pcs.os.shared.model.vehicle.VehicleTypes;

public class UpdateCarPresenter extends CreateVehiclePresenter{
	
	private Car car;

	public UpdateCarPresenter(IConnectionServiceAsync rpcService, Display view,
			CommonWorkspaceConstants constants, Car car) {
		super(rpcService, view, constants);
		this.car = car;
	}
	
	@Override
	public void go(HasWidgets container){
		super.go(container);
		getIdTagTextBox().setText(car.getIdTag());
		getIdTagTextBox().setReadOnly(true);
		
		int i = getItemIndex(getVehicleTypesListBox(), VehicleTypes.Car.name());
		getVehicleTypesListBox().setSelectedIndex(i);
		getVehicleTypesListBox().setEnabled(false);
		DomEvent.fireNativeEvent(Document.get().createChangeEvent(), getVehicleTypesListBox());
		
		i = getItemIndex(getPriorityListBox(), car.getPriority().name());
		getPriorityListBox().setSelectedIndex(i);
		
		getPlateTextBox().setText(car.getPlate());
		getPlateTextBox().setReadOnly(true);
		
		setPictureUrl(car.getImageURL());
		getView().setPictureUrl(car.getImageURL());
		getView().showPicture();
	}

}
