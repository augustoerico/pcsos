package epusp.pcs.os.admin.client.presenter;

import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.user.client.ui.HasWidgets;

import epusp.pcs.os.shared.client.constants.CommonWorkspaceConstants;
import epusp.pcs.os.shared.client.rpc.IConnectionServiceAsync;
import epusp.pcs.os.shared.general.AttributeInfoLoader;
import epusp.pcs.os.shared.model.attribute.AttributeInfo;
import epusp.pcs.os.shared.model.vehicle.VehicleTypes;
import epusp.pcs.os.shared.model.vehicle.motorcycle.Motorcycle;

public class UpdateMotorcyclePresenter  extends CreateVehiclePresenter{
	
	private Motorcycle motorcycle;

	public UpdateMotorcyclePresenter(IConnectionServiceAsync rpcService, Display view,
			CommonWorkspaceConstants constants, List<AttributeInfo> customAttributes, AttributeInfoLoader loader, Motorcycle motorcycle) {
		super(rpcService, view, constants, customAttributes, loader);
		this.motorcycle = motorcycle;
	}
	
	@Override
	public void go(HasWidgets container){
		super.go(container);
		getIdTagTextBox().setText(motorcycle.getIdTag());
		getIdTagTextBox().setReadOnly(true);
		
		int i = getItemIndex(getVehicleTypesListBox(), VehicleTypes.Motorcycle.name());
		getVehicleTypesListBox().setSelectedIndex(i);
		getVehicleTypesListBox().setEnabled(false);
		DomEvent.fireNativeEvent(Document.get().createChangeEvent(), getVehicleTypesListBox());
		
		i = getItemIndex(getPriorityListBox(), motorcycle.getPriority().name());
		getPriorityListBox().setSelectedIndex(i);
		
		getPlateTextBox().setText(motorcycle.getPlate());
		getPlateTextBox().setReadOnly(true);
		
		setPictureUrl(motorcycle.getImageURL());
		getView().setPictureUrl(motorcycle.getImageURL());
		getView().showPicture();
		
		getIsActiveCheckBox().setValue(motorcycle.isActive());
		
		addValuesToCustomWidgets(motorcycle);
	}

}