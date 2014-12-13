package epusp.pcs.os.admin.client.presenter;

import java.util.ArrayList;
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
import epusp.pcs.os.shared.general.AttributeInfoLoader;
import epusp.pcs.os.shared.general.AttributeInfoLoader.IAttributeInfoLoaded;
import epusp.pcs.os.shared.model.ICustomAttributes;
import epusp.pcs.os.shared.model.attribute.AttributeInfo;
import epusp.pcs.os.shared.model.vehicle.Priority;
import epusp.pcs.os.shared.model.vehicle.Vehicle;
import epusp.pcs.os.shared.model.vehicle.VehicleTypes;
import epusp.pcs.os.shared.model.vehicle.car.Car;
import epusp.pcs.os.shared.model.vehicle.car.CarCustomAttributes;
import epusp.pcs.os.shared.model.vehicle.helicopter.Helicopter;
import epusp.pcs.os.shared.model.vehicle.helicopter.HelicopterCustomAttributes;
import epusp.pcs.os.shared.model.vehicle.motorcycle.Motorcycle;
import epusp.pcs.os.shared.model.vehicle.motorcycle.MotorcycleCustomAttributes;

public class CreateVehiclePresenter extends CreateUpdatePresenter{

	private final AttributeInfoLoader loader;
	
	protected CreateVehiclePresenter(IConnectionServiceAsync rpcService,
			Display view, CommonWorkspaceConstants constants, List<AttributeInfo> customAttributes, AttributeInfoLoader loader) {
		super(rpcService, view, constants, customAttributes);
		this.loader = loader;
	}
	
	public CreateVehiclePresenter(IConnectionServiceAsync rpcService,
			Display view, CommonWorkspaceConstants constants, AttributeInfoLoader loader) {
		super(rpcService, view, constants, new ArrayList<AttributeInfo>());
		this.loader = loader;
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
			vehicleTypes.addItem(type.getText(), type.name());
		}
		
		priority = new ListBox();
		for(Priority p : Priority.values()){
			priority.addItem(p.getText(), p.name());
		}
		
		idTag = new TextBox();
		view.addPrimaryAttribute(constants.tag(), true, idTag);
		view.addPrimaryAttribute(constants.vehicleType(), true, vehicleTypes);
		view.addPrimaryAttribute(constants.priority(), true, priority);

		super.go(container);
		bind();
	}
	
	private void bind(){
		final Display view = getView();
		view.setSaveEnabled(true);
		
		vehicleTypes.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				String l = vehicleTypes.getValue(vehicleTypes.getSelectedIndex());
				clearCustomAtttributes();
				view.clearSecondaryAttributes();
				view.removePrimaryAttribute(plate);
				plate = null;
				if(!l.equals("")){
					switch (VehicleTypes.valueOf(l)) {
					case Car:
						plate = new TextBox();
						view.addPrimaryAttribute(getConstants().plate(), true, plate);
						loader.loadCustomAttributes(CarCustomAttributes.values(), new IAttributeInfoLoaded() {
							@Override
							public void onCustomAttributesLoaded() {
								for(ICustomAttributes attribute : CarCustomAttributes.values()){
									addCustomAttribute(loader.getAttributeInfo(attribute.getAttributeName()));
								}
								addCustomAttributesToView();
							}
						});
						break;
					case Helicopter:
						loader.loadCustomAttributes(HelicopterCustomAttributes.values(), new IAttributeInfoLoaded() {
							@Override
							public void onCustomAttributesLoaded() {
								for(ICustomAttributes attribute : HelicopterCustomAttributes.values()){
									addCustomAttribute(loader.getAttributeInfo(attribute.getAttributeName()));
								}
								addCustomAttributesToView();
							}
						});
						break;
					case Motorcycle:
						plate = new TextBox();
						view.addPrimaryAttribute(getConstants().plate(), true, plate);
						loader.loadCustomAttributes(MotorcycleCustomAttributes.values(), new IAttributeInfoLoaded() {
							@Override
							public void onCustomAttributesLoaded() {
								for(ICustomAttributes attribute : MotorcycleCustomAttributes.values()){
									addCustomAttribute(loader.getAttributeInfo(attribute.getAttributeName()));
								}
								addCustomAttributesToView();
							}
						});
						break;
					default:
						break;
					}
				}
			}
		});
		
		view.addSaveClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				Vehicle vehicle = null;
				
				switch(VehicleTypes.valueOf(vehicleTypes.getValue(vehicleTypes.getSelectedIndex()))){
				case Car:
					vehicle = new Car(idTag.getText(), plate.getText());
					break;
				case Helicopter:
					vehicle = new Helicopter(idTag.getText());
					break;
				case Motorcycle:
					vehicle = new Motorcycle(idTag.getText(), plate.getText());
					break;
				default:
					break;
				}
				
				vehicle.setImageURL(getPictureUrl());
				
				vehicle.setPrioraty(Priority.valueOf(priority.getValue(priority.getSelectedIndex())));
				
				readValuesAndSaveOnObject(vehicle);
				
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
