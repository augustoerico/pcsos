package epusp.pcs.os.monitor.client;

import java.util.HashMap;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.PopupPanel;

import epusp.pcs.os.monitor.client.constants.MonitorWorkspaceConstants;
import epusp.pcs.os.monitor.client.event.LoadedVehiclesEvent;
import epusp.pcs.os.monitor.client.event.LoadedVehiclesEvent.LoadedVehiclesHandler;
import epusp.pcs.os.monitor.client.presenter.CallInfoPresenter;
import epusp.pcs.os.monitor.client.presenter.GoogleMapsPresenter;
import epusp.pcs.os.monitor.client.presenter.WorkspacePresenter;
import epusp.pcs.os.monitor.client.rpc.IMonitorWorkspaceServiceAsync;
import epusp.pcs.os.monitor.client.view.CallInfo;
import epusp.pcs.os.monitor.client.view.Workspace;
import epusp.pcs.os.monitor.shared.EmergencyCallSpecs;
import epusp.pcs.os.shared.client.event.EventBus;
import epusp.pcs.os.shared.client.presenter.PreferencesPresenter;
import epusp.pcs.os.shared.client.presenter.Presenter;
import epusp.pcs.os.shared.client.view.Preferences;
import epusp.pcs.os.shared.general.RPCRequestTracker;
import epusp.pcs.os.shared.model.oncall.EmergencyCall;
import epusp.pcs.os.shared.model.oncall.Position;
import epusp.pcs.os.shared.model.oncall.VehicleOnCall;
import epusp.pcs.os.shared.model.person.Victim;
import epusp.pcs.os.shared.model.vehicle.Vehicle;

public class WorkspaceController implements Presenter, LoadedVehiclesHandler {

	private IMonitorWorkspaceServiceAsync monitorService;
	private MonitorWorkspaceConstants constants;

	private WorkspacePresenter workspacePresenter;
	private GoogleMapsPresenter googleMapsPresenter;
	private PreferencesPresenter preferencesPresenter;
	private CallInfoPresenter callInfoPresenter;

	PopupPanel preferencesPopup = new PopupPanel(true);

	private MonitorStatusLifecycle monitorStatus = MonitorStatusLifecycle.Begin;

	private EmergencyCallSpecs emergencyCallSpecs = new EmergencyCallSpecs();

	private RPCRequestTracker tracker;

	private final HashMap<String, Vehicle> vehicles = new HashMap<String, Vehicle>();

	private Timer timer = new Timer() {
		@Override
		public void run() {
			updateMonitorStatus();
		}
	};

	public WorkspaceController(IMonitorWorkspaceServiceAsync monitorService, MonitorWorkspaceConstants constants){
		this.monitorService = monitorService;
		this.constants = constants;
		preferencesPopup.setGlassEnabled(true);
		preferencesPopup.setStyleName("preferencesPopupPanel");
		preferencesPopup.setGlassStyleName("preferencesPopupGlassPanel");

		EventBus.get().addHandler(LoadedVehiclesEvent.TYPE, this);

		Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				if(preferencesPopup.isShowing())
					preferencesPopup.center();
			}
		});
	}

	private void bind(){
		workspacePresenter.getPreferencesButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				preferencesPopup.center();
			}
		});

		preferencesPresenter.getCancelButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				preferencesPopup.hide();
			}
		});
	}

	@Override
	public void go(final HasWidgets container) {
		workspacePresenter = new WorkspacePresenter(monitorService, new Workspace(), constants);
		workspacePresenter.go(container);
		googleMapsPresenter = new GoogleMapsPresenter(monitorService, constants);
		googleMapsPresenter.go(workspacePresenter.getMapsArea());
		callInfoPresenter = new CallInfoPresenter(monitorService, new CallInfo(), constants);
		callInfoPresenter.go(workspacePresenter.getInfoArea());
		preferencesPresenter = new PreferencesPresenter(monitorService, new Preferences(), constants);
		preferencesPresenter.go(preferencesPopup);
		timer.scheduleRepeating(2*1000);
		bind();
	}

	private void updateMonitorStatus(){
		switch (monitorStatus) {
		case Begin:
			monitorService.addFreeMonitor(new AsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					monitorStatus = MonitorStatusLifecycle.WaitingCall;
				}

				@Override
				public void onFailure(Throwable caught) {
				}
			});
			break;
		case WaitingCall:
			monitorService.isMonitorOnCall(new AsyncCallback<Boolean>() {

				@Override
				public void onSuccess(Boolean result) {
					if(result){
						monitorStatus = MonitorStatusLifecycle.OnCall;
					}
				}

				@Override
				public void onFailure(Throwable caught) {
				}
			});			
			break;
		case OnCall:			
			monitorService.getEmergencyCallDetails(emergencyCallSpecs, new AsyncCallback<EmergencyCall>() {

				@Override
				public void onSuccess(EmergencyCall result) {					
					loadVehicles(result);
					
					if(!callInfoPresenter.hasVictim()){
						loadVictim(result.getVictimEmail());
					}
					
					if(!googleMapsPresenter.hasVictim()){
						googleMapsPresenter.addVictim(result.getVictimPositions().remove(0));
						emergencyCallSpecs.setVictimLastPositionIndex(0);
					}
					
					emergencyCallSpecs.setVictimLastPositionIndex(emergencyCallSpecs.getVictimLastPositionIndex() + result.getVictimPositionSize());
					googleMapsPresenter.updateVictimPosition(result.getVictimPositions());					
				}

				@Override
				public void onFailure(Throwable caught) {
				}
			});
			break;
		default:
			break;
		}
	}
	
	private void loadVictim(final String victimEmail){
		monitorService.getFullVictim(victimEmail, new AsyncCallback<Victim>() {

			@Override
			public void onSuccess(Victim result) {
				callInfoPresenter.showVictim(result);
			}

			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}
	
	private void loadVehicles(final EmergencyCall emergencyCall){
		tracker = new RPCRequestTracker(new LoadedVehiclesEvent(emergencyCall));
		for(VehicleOnCall vehicle : emergencyCall.getVehicles()){
			if(!vehicles.containsKey(vehicle.getVehicleIdTag())){
				
				AsyncCallback<Vehicle> vehicleCall = new AsyncCallback<Vehicle>() {
					@Override
					public void onFailure(Throwable caught) {
						if(tracker.hasCall(this))
							tracker.remove(this);
					}

					@Override
					public void onSuccess(Vehicle result) {
						if(tracker.hasCall(this)){
							if(result != null){
								vehicles.put(result.getIdTag(), result);
							}
							tracker.remove(this);
						}
					}
				};

				tracker.add(vehicleCall);
				
				monitorService.getVehicle(vehicle.getVehicleIdTag(), vehicleCall);
			}			
		}
		
		if(tracker.isEmpty())
			tracker.fire();
	}

	@Override
	public void onVehiclesLoaded(LoadedVehiclesEvent loadedVehiclesEvent) {
		EmergencyCall emergencyCall = loadedVehiclesEvent.getEmergencyCall();
		for(VehicleOnCall vehicle : emergencyCall.getVehicles()){
			
			List<Position> vehiclePositions = emergencyCall.getVehiclePositions(vehicle.getVehicleIdTag());
			if(!vehiclePositions.isEmpty()){				
				if(!googleMapsPresenter.hasVehicle(vehicle.getVehicleIdTag())){
					googleMapsPresenter.addVehicle(vehicles.get(vehicle.getVehicleIdTag()), vehiclePositions.remove(0));
					emergencyCallSpecs.putVehiclesLastPositionIndex(vehicle.getVehicleIdTag(), 0);
				}
				
				emergencyCallSpecs.putVehiclesLastPositionIndex(vehicle.getVehicleIdTag(), emergencyCallSpecs.getVehicleLastPositionIndex(vehicle.getVehicleIdTag())
						+ vehicle.getPositions().size());
			}
			
			googleMapsPresenter.updateVehiclePosition(vehicle.getVehicleIdTag(), vehiclePositions);
		}
	}

}
