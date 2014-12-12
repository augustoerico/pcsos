package epusp.pcs.os.monitor.client;

import java.util.ArrayList;
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
import epusp.pcs.os.monitor.client.event.AcceptRejectCallEvent;
import epusp.pcs.os.monitor.client.event.AcceptRejectCallEvent.AcceptRejectCallHandler;
import epusp.pcs.os.monitor.client.event.FinishCallEvent;
import epusp.pcs.os.monitor.client.event.LoadedInfoEvent;
import epusp.pcs.os.monitor.client.event.FinishCallEvent.FinishCallHandler;
import epusp.pcs.os.monitor.client.event.LoadedInfoEvent.LoadedInfoHandler;
import epusp.pcs.os.monitor.client.presenter.CallInfoPresenter;
import epusp.pcs.os.monitor.client.presenter.GoogleMapsPresenter;
import epusp.pcs.os.monitor.client.presenter.NewCallPresenter;
import epusp.pcs.os.monitor.client.presenter.ReinforcementsPresenter;
import epusp.pcs.os.monitor.client.presenter.WorkspacePresenter;
import epusp.pcs.os.monitor.client.rpc.IMonitorWorkspaceServiceAsync;
import epusp.pcs.os.monitor.client.view.CallInfo;
import epusp.pcs.os.monitor.client.view.NewCall;
import epusp.pcs.os.monitor.client.view.Reinforcements;
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
import epusp.pcs.os.shared.model.person.user.agent.Agent;
import epusp.pcs.os.shared.model.person.victim.Victim;
import epusp.pcs.os.shared.model.vehicle.Vehicle;

public class WorkspaceController implements Presenter, LoadedInfoHandler, FinishCallHandler, AcceptRejectCallHandler {

	private IMonitorWorkspaceServiceAsync monitorService;
	private MonitorWorkspaceConstants constants;

	private WorkspacePresenter workspacePresenter;
	private GoogleMapsPresenter googleMapsPresenter;
	private PreferencesPresenter preferencesPresenter;
	private CallInfoPresenter callInfoPresenter;
	private ReinforcementsPresenter reinforcementsPresenter;
	private NewCallPresenter newCallPresenter;

	PopupPanel preferencesPopup = new PopupPanel(true);

	PopupPanel newCallPopup = new PopupPanel(true);
	
	private MonitorStatusLifecycle monitorStatus = MonitorStatusLifecycle.Unavailable;

	private EmergencyCallSpecs emergencyCallSpecs = new EmergencyCallSpecs();

	private RPCRequestTracker tracker;

	private final HashMap<String, Vehicle> vehicles = new HashMap<String, Vehicle>();
	
	private final HashMap<String, Agent> agents = new HashMap<String, Agent>();

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
		
		newCallPopup.setGlassEnabled(true);
		newCallPopup.setStyleName("preferencesPopupPanel");
		newCallPopup.setGlassStyleName("preferencesPopupGlassPanel");

		EventBus.get().addHandler(LoadedInfoEvent.TYPE, this);
		EventBus.get().addHandler(FinishCallEvent.TYPE, this);
		EventBus.get().addHandler(AcceptRejectCallEvent.TYPE, this);

		Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				if(preferencesPopup.isShowing()){
					preferencesPopup.center();
					newCallPopup.center();
				}
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
		
		newCallPresenter.addCloseHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				newCallPopup.hide();
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
		reinforcementsPresenter = new ReinforcementsPresenter(monitorService, new Reinforcements());
		reinforcementsPresenter.go(workspacePresenter.getReinforcementsArea());
		newCallPresenter = new NewCallPresenter(new NewCall(), constants);
		newCallPresenter.go(newCallPopup);
		timer.scheduleRepeating(2_000);
		bind();
	}

	private void updateMonitorStatus(){
		switch (monitorStatus) {
		case Unavailable:
			workspacePresenter.setOnCall(false);
			break;
		case Begin:
			workspacePresenter.setOnCall(false);
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
						workspacePresenter.setOnCall(true);
						newCallPopup.center();
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
					loadInfo(result);
					
					if(!callInfoPresenter.hasVictim()){
						loadVictim(result.getVictimEmail());
					}
					
					if(!googleMapsPresenter.hasVictim()){
						googleMapsPresenter.addVictim(result.getVictimPositions().remove(0));
						emergencyCallSpecs.setVictimLastPositionIndex(0);
					}
					
					reinforcementsPresenter.update();
					
					emergencyCallSpecs.setVictimLastPositionIndex(emergencyCallSpecs.getVictimLastPositionIndex() + result.getVictimPositionSize());
					googleMapsPresenter.updateVictimPosition(result.getVictimPositions());					
				}

				@Override
				public void onFailure(Throwable caught) {
				}
			});
			break;
		
		case FinishingCall:
			monitorService.finishCallAcknowledgment(new AsyncCallback<Void>() {
				
				@Override
				public void onSuccess(Void result) {
					monitorStatus = MonitorStatusLifecycle.Begin;
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
	
	private void loadInfo(final EmergencyCall emergencyCall){
		tracker = new RPCRequestTracker(new LoadedInfoEvent(emergencyCall));
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
			
			for(String agent : vehicle.getAgents()){
				if(!agents.containsKey(agent)){
					AsyncCallback<Agent> agentCall = new AsyncCallback<Agent>() {
						@Override
						public void onFailure(Throwable caught) {
							if(tracker.hasCall(this))
								tracker.remove(this);
						}

						@Override
						public void onSuccess(Agent result) {
							if(tracker.hasCall(this)){
								if(result != null){
									agents.put(result.getId(), result);
								}
								tracker.remove(this);
							}
						}
					};

					tracker.add(agentCall);
					
					monitorService.getFullAgent(agent, agentCall);
				}
			}
		}
		
		if(tracker.isEmpty())
			tracker.fire();
	}

	@Override
	public void onInfoLoaded(LoadedInfoEvent loadedInfoEvent) {
		EmergencyCall emergencyCall = loadedInfoEvent.getEmergencyCall();
		for(VehicleOnCall vehicle : emergencyCall.getVehicles()){
			
			if(!callInfoPresenter.hasInfo(vehicle.getVehicleIdTag())){
				List<Agent> agents = new ArrayList<Agent>();
				for(String id : vehicle.getAgents()){
					Agent agent = this.agents.get(id);
					if(agent != null)
						agents.add(agent);
				}
				callInfoPresenter.addInfo(vehicles.get(vehicle.getVehicleIdTag()), agents);
			}
			
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

	@Override
	public void onFinishCall(FinishCallEvent finishCallEvent) {
		monitorService.finishCall(new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
			}
			
			@Override
			public void onFailure(Throwable caught) {
			}
		});
		
		monitorStatus = MonitorStatusLifecycle.FinishingCall;
	}

	@Override
	public void onAcceptingRejectingCalls(
			AcceptRejectCallEvent acceptRejectCallEvent) {
		if(acceptRejectCallEvent.isAccepting()){
			monitorStatus = MonitorStatusLifecycle.Begin;
		}else{
			if(monitorStatus.equals(MonitorStatusLifecycle.Begin) || monitorStatus.equals(MonitorStatusLifecycle.WaitingCall)){
				monitorService.monitorLeaving(new AsyncCallback<Boolean>() {
					
					@Override
					public void onSuccess(Boolean result) {
						if(result){
							monitorStatus = MonitorStatusLifecycle.Unavailable;
						}
					}
					
					@Override
					public void onFailure(Throwable caught) {
					}
				});
			}
		}
		
	}

}
