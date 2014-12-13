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
import epusp.pcs.os.shared.model.vehicle.helicopter.Helicopter;

public class UpdateHelicopterPresenter extends CreateVehiclePresenter{

	private Helicopter helicopter;
	
	public UpdateHelicopterPresenter(IConnectionServiceAsync rpcService,
			Display view, CommonWorkspaceConstants constants, List<AttributeInfo> customAttributes, AttributeInfoLoader loader, Helicopter helicopter) {
		super(rpcService, view, constants, customAttributes, loader);
		this.helicopter = helicopter;
	}
	
	@Override
	public void go(HasWidgets container){
		super.go(container);
		getIdTagTextBox().setText(helicopter.getIdTag());
		getIdTagTextBox().setReadOnly(true);
		
		int i = getItemIndex(getVehicleTypesListBox(), VehicleTypes.Helicopter.name());
		getVehicleTypesListBox().setSelectedIndex(i);
		getVehicleTypesListBox().setEnabled(false);
		DomEvent.fireNativeEvent(Document.get().createChangeEvent(), getVehicleTypesListBox());
		
		i = getItemIndex(getPriorityListBox(), helicopter.getPriority().name());
		getPriorityListBox().setSelectedIndex(i);
		
		setPictureUrl(helicopter.getImageURL());
		getView().setPictureUrl(helicopter.getImageURL());
		getView().showPicture();
		
		getIsActiveCheckBox().setValue(helicopter.isActive());
		
		addValuesToCustomWidgets(helicopter);
	}

}
