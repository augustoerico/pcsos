package epusp.pcs.os.monitor.client.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import epusp.pcs.os.monitor.client.constants.MonitorWorkspaceConstants;
import epusp.pcs.os.monitor.client.event.FinishCallEvent;
import epusp.pcs.os.monitor.client.event.FinishCallEvent.FinishCallHandler;
import epusp.pcs.os.monitor.client.rpc.IMonitorWorkspaceServiceAsync;
import epusp.pcs.os.shared.client.event.EventBus;
import epusp.pcs.os.shared.client.presenter.Presenter;
import epusp.pcs.os.shared.client.view.PictureTagItem;
import epusp.pcs.os.shared.model.vehicle.Vehicle;

public class ReinforcementsPresenter implements Presenter, FinishCallHandler {

	public interface Display{
		void addType(String type, Widget header);
		void addItem(String type, Widget widget);
		void removeItem(String type, Widget widget);
		void addHandler(ClickHandler handler);
		void addSelectedItem(Widget widget);
		void removeHandler(ClickHandler handler);
		void sendHandler(ClickHandler handler);
		void clearChoosen();
		void clearOptions();
		Boolean hasType(String type);
		Widget asWidget();
		void clear();
	}

	private IMonitorWorkspaceServiceAsync monitorService;

	private Display view;

	private MonitorWorkspaceConstants constants;

	private HashMap<String, Widget> items = new HashMap<String, Widget>();
	private HashMap<String, Boolean> selectedItems = new HashMap<String, Boolean>();
	private HashMap<String, Vehicle> vehicles = new HashMap<String, Vehicle>();
	private List<Vehicle> choosenVehicles = new ArrayList<Vehicle>();

	public ReinforcementsPresenter(IMonitorWorkspaceServiceAsync monitorService, Display view, MonitorWorkspaceConstants constants){
		this.monitorService = monitorService;
		this.view = view;
		this.constants = constants;
		
		EventBus.get().addHandler(FinishCallEvent.TYPE, this);
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
		update();
		bind();
	}

	private void bind(){
		view.addHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				for(Entry<String, Boolean> entry : selectedItems.entrySet()){
					if(entry.getValue()){
						Widget item = items.get(entry.getKey());
						item.removeStyleDependentName("selected");
						selectedItems.put(entry.getKey(), false);
						choosenVehicles.add(vehicles.get(entry.getKey()));
						view.addSelectedItem(item);
					}
				}
			}
		});

		view.removeHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				for(Entry<String, Boolean> entry : selectedItems.entrySet()){
					if(entry.getValue()){
						Widget item = items.get(entry.getKey());
						Vehicle vehicle = vehicles.get(entry.getKey());
						item.removeStyleDependentName("selected");
						selectedItems.put(entry.getKey(), false);
						choosenVehicles.remove(vehicles.get(entry.getKey()));
						view.addItem(vehicle.getType().name(), item);
					}
				}
			}
		});

		view.sendHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				List<String> vehiclesIdTag = new ArrayList<String>();

				for(Vehicle vehicle : choosenVehicles){
					items.remove(vehicle.getIdTag());
					selectedItems.remove(vehicle.getIdTag());
					vehicles.remove(vehicle.getIdTag());

					view.clearChoosen();

					vehiclesIdTag.add(vehicle.getIdTag());
				}

				monitorService.addVehiclesToCall(vehiclesIdTag, new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
					}

					@Override
					public void onFailure(Throwable caught) {
					}
				});

				choosenVehicles.clear();
			}
		});
	}

	public void update(){
		monitorService.getAvailableSupportVehicles(new AsyncCallback<List<Vehicle>>() {

			@Override
			public void onSuccess(List<Vehicle> result) {
				List<Vehicle> vehiclesToAdd = new ArrayList<Vehicle>();
				List<Vehicle> vehiclesToRemove = new ArrayList<Vehicle>();
				vehiclesToRemove.addAll(vehicles.values());
				
				for(Vehicle vehicle : result){
					if(!vehicles.containsKey(vehicle.getIdTag()))
						vehiclesToAdd.add(vehicle);
					else{
						vehiclesToRemove.remove(vehicle);
					}
				}
				
				for(Vehicle vehicle : vehiclesToRemove){
					view.removeItem(vehicle.getType().name(), items.get(vehicle.getIdTag()));
					items.remove(vehicle.getIdTag());
					selectedItems.remove(vehicle.getIdTag());
					vehicles.remove(vehicle.getIdTag());
					choosenVehicles.remove(vehicle.getIdTag());
				}

				for(final Vehicle vehicle : vehiclesToAdd){
					switch(vehicle.getType()){
					case Car:
						if(!view.hasType(vehicle.getType().name()))
							view.addType(vehicle.getType().name(), new Label(constants.car()));
						break;
					case Helicopter:
						if(!view.hasType(vehicle.getType().name()))
							view.addType(vehicle.getType().name(), new Label(constants.helicopter()));						
						break;
					default:
						break;
					}

					vehicles.put(vehicle.getIdTag(), vehicle);

					final PictureTagItem item = new PictureTagItem();
					item.setText(vehicle.getIdTag());
					item.setImage(vehicle.getImageURL());
					item.addPanelStyleName("reinforcementsItem");
					item.addImageStyleName("controlPicture");
					item.addLabelStyleName("controlIdText");
					view.addItem(vehicle.getType().name(), item);

					selectedItems.put(vehicle.getIdTag(), false);
					items.put(vehicle.getIdTag(), item);


					item.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							if(selectedItems.containsKey(vehicle.getIdTag())){
								Boolean selected = !selectedItems.get(vehicle.getIdTag());
								selectedItems.put(vehicle.getIdTag(), selected);
								if(selected)
									item.addStyleDependentName("selected");
								else
									item.removeStyleDependentName("selected");
							}
						}
					});
				}
			}

			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}

	@Override
	public void onFinishCall(FinishCallEvent finishCallEvent) {
		view.clear();
	}

}
