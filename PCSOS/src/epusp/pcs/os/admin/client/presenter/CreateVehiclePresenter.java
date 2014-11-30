package epusp.pcs.os.admin.client.presenter;

import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

import epusp.pcs.os.admin.client.rpc.IAdminWorkspaceServiceAsync;
import epusp.pcs.os.shared.client.constants.CommonWorkspaceConstants;
import epusp.pcs.os.shared.client.event.ClosePopupEvent;
import epusp.pcs.os.shared.client.event.EventBus;
import epusp.pcs.os.shared.client.presenter.CreateUpdatePresenter;
import epusp.pcs.os.shared.client.rpc.IConnectionServiceAsync;
import epusp.pcs.os.shared.model.attribute.AttributeInfo;
import epusp.pcs.os.shared.model.vehicle.Car;
import epusp.pcs.os.shared.model.vehicle.Helicopter;
import epusp.pcs.os.shared.model.vehicle.Priority;
import epusp.pcs.os.shared.model.vehicle.Vehicle;
import epusp.pcs.os.shared.model.vehicle.VehicleTypes;

public class CreateVehiclePresenter extends CreateUpdatePresenter{

	public CreateVehiclePresenter(IConnectionServiceAsync rpcService,
			Display view, CommonWorkspaceConstants constants, List<AttributeInfo> customAttributes) {
		super(rpcService, view, constants, customAttributes);
	}
	
	private TextBox idTag, plate;
	private ListBox vehicleTypes, priority;

	@Override
	public void go(HasWidgets container){
		final Display view = getView();
		final CommonWorkspaceConstants constants = getConstants();
		vehicleTypes = new ListBox();
		vehicleTypes.addItem("");
		for(VehicleTypes type : VehicleTypes.values()){
			vehicleTypes.addItem(type.name());
		}
		
		priority = new ListBox();
		for(Priority p : Priority.values()){
			priority.addItem(p.name());
		}
		
		idTag = new TextBox();
		view.addPrimaryAttribute(constants.tag(), true, idTag);
		view.addPrimaryAttribute(constants.vehicleType(), true, vehicleTypes);
		view.addPrimaryAttribute(constants.priority(), true, priority);
		vehicleTypes.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				String l = vehicleTypes.getItemText(vehicleTypes.getSelectedIndex());
				if(!l.equals(VehicleTypes.Car.name())){
					view.removeSecondaryAttribute(plate);
				}else{
					switch (VehicleTypes.valueOf(l)) {
					case Car:
						plate = new TextBox();
						view.addSecondaryAttribute(constants.plate(), true, plate);
						break;
					default:
						break;
					}
				}
			}
		});
		super.go(container);
		bind();
	}
	
	private void bind(){
		final Display view = getView();
		view.setSaveEnabled(true);
		view.addSaveClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				Vehicle vehicle = null;
				
				switch(VehicleTypes.valueOf(vehicleTypes.getItemText(vehicleTypes.getSelectedIndex()))){
				case Car:
					vehicle = new Car(idTag.getText(), plate.getText());
					break;
				case Helicopter:
					vehicle = new Helicopter(idTag.getText());
					break;
				default:
					break;
				}
				
				vehicle.setImageURL(getPictureUrl());
				
				vehicle.setPrioraty(Priority.valueOf(priority.getItemText(priority.getSelectedIndex())));
				
				getRpcService().createVehicle(vehicle, new AsyncCallback<Void>() {
					
					@Override
					public void onSuccess(Void result) {
						EventBus.get().fireEvent(new ClosePopupEvent());
					}
					
					@Override
					public void onFailure(Throwable caught) {				
					}
				});
				
			}
				
		});
	}
	
	protected TextBox getIdTagTextBox(){
		return idTag;
	}
	
	protected TextBox getPlateTextBox(){
		return plate;
	}
	
	protected ListBox getVehicleTypesListBox(){
		return vehicleTypes;
	}
	
	protected ListBox getPriorityListBox(){
		return priority;
	}
	
	@Override
	protected IAdminWorkspaceServiceAsync getRpcService(){
		return (IAdminWorkspaceServiceAsync) super.getRpcService();
	}
}
