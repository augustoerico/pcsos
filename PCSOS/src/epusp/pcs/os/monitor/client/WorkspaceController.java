package epusp.pcs.os.monitor.client;

import java.util.HashMap;

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
import epusp.pcs.os.monitor.client.presenter.GoogleMapsPresenter;
import epusp.pcs.os.monitor.client.presenter.WorkspacePresenter;
import epusp.pcs.os.monitor.client.rpc.IMonitorWorkspaceServiceAsync;
import epusp.pcs.os.monitor.client.view.Workspace;
import epusp.pcs.os.monitor.shared.EmergencyCallSpecs;
import epusp.pcs.os.shared.client.event.EventBus;
import epusp.pcs.os.shared.client.presenter.PreferencesPresenter;
import epusp.pcs.os.shared.client.presenter.Presenter;
import epusp.pcs.os.shared.client.view.Preferences;
import epusp.pcs.os.shared.general.RPCRequestTracker;
import epusp.pcs.os.shared.model.oncall.EmergencyCall;
import epusp.pcs.os.shared.model.oncall.VehicleOnCall;
import epusp.pcs.os.shared.model.vehicle.Vehicle;

public class WorkspaceController implements Presenter, LoadedVehiclesHandler {

	private IMonitorWorkspaceServiceAsync monitorService;
	private MonitorWorkspaceConstants constants;

	private WorkspacePresenter workspacePresenter;
	private GoogleMapsPresenter googleMapsPresenter;
	private PreferencesPresenter preferencesPresenter;

	PopupPanel preferencesPopup = new PopupPanel(true);

	private MonitorStatusLifecycle monitorStatus = MonitorStatusLifecycle.Begin;

	private EmergencyCallSpecs emergencyCallSpecs = new EmergencyCallSpecs();

	private RPCRequestTracker tracker = new RPCRequestTracker(new LoadedVehiclesEvent());

	private HashMap<String, Vehicle> vehicles = new HashMap<String, Vehicle>();

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
						monitorStatus = MonitorStatusLifecycle.StartingCall;
					}
				}

				@Override
				public void onFailure(Throwable caught) {
				}
			});			
			break;
		case StartingCall:
			monitorService.getEmergencyCallDetails(emergencyCallSpecs, new AsyncCallback<EmergencyCall>() {

				@Override
				public void onSuccess(EmergencyCall result) {
					emergencyCallSpecs.setVictimLastPositionIndex(result.getVictimPositionSize()-1);
					tracker.clear();
					timer.cancel();
					for(VehicleOnCall vehicle : result.getVehicles()){
						AsyncCallback<Vehicle> vehicleCall = new AsyncCallback<Vehicle>() {

							@Override
							public void onFailure(Throwable caught) {
							}

							@Override
							public void onSuccess(Vehicle result) {
								if(tracker.hasCall(this)){
									if(result != null){
										System.out.println(result.getId());
										vehicles.put(result.getId(), result);
									}
									tracker.remove(this);
								}
							}
						};

						tracker.add(vehicleCall);

						monitorService.getVehicle(vehicle.getId(), vehicleCall);
						emergencyCallSpecs.putVehiclesLastPositionIndex(vehicle.getId(), vehicle.getPositions().size()-1);
					}

					googleMapsPresenter.addVictim(result.getVictimPositions().get(0));

					monitorStatus = MonitorStatusLifecycle.OnCall;
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
					emergencyCallSpecs.setVictimLastPositionIndex(result.getVictimPositionSize()-1);
					for(VehicleOnCall vehicle : result.getVehicles()){
						emergencyCallSpecs.putVehiclesLastPositionIndex(vehicle.getId(), vehicle.getPositions().size()-1);
					}
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

	@Override
	public void onVehiclesLoaded(LoadedVehiclesEvent loadedVehiclesEvent) {
		timer.scheduleRepeating(2*1000);
	}

}
