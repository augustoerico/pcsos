package epusp.pcs.os.monitor.client.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import epusp.pcs.os.monitor.client.constants.MonitorWorkspaceConstants;
import epusp.pcs.os.monitor.client.event.FinishCallEvent;
import epusp.pcs.os.monitor.client.event.FinishCallEvent.FinishCallHandler;
import epusp.pcs.os.monitor.client.rpc.IMonitorWorkspaceServiceAsync;
import epusp.pcs.os.monitor.client.view.Details;
import epusp.pcs.os.shared.client.event.EventBus;
import epusp.pcs.os.shared.client.presenter.Presenter;
import epusp.pcs.os.shared.client.view.PictureTagItem;
import epusp.pcs.os.shared.model.person.user.agent.Agent;
import epusp.pcs.os.shared.model.person.victim.Victim;
import epusp.pcs.os.shared.model.vehicle.Vehicle;

public class CallInfoPresenter implements Presenter, FinishCallHandler{

	public interface Display{
		HasWidgets getVictimPanel();
		FlowPanel getControlPanel();
		DeckLayoutPanel getInfoPanel();
		void addInfo(Widget w);
		void addControl(Widget w);
		Widget asWidget();
		void clear();
		void setCaptionText(String text);
	}
	
	private IMonitorWorkspaceServiceAsync rpcService; 
	private Display view; 
	private MonitorWorkspaceConstants constants;
	private Boolean hasVictim = false;
	
	private DetailsPresenter victim;
	private HashMap<String, Vehicle> vehicles = new HashMap<String, Vehicle>();
	
	private HashMap<Integer, Widget> controlPanel = new HashMap<Integer, Widget>();
	
	private List<DetailsPresenter> agentsDetailsList = new ArrayList<DetailsPresenter>();

	public CallInfoPresenter(IMonitorWorkspaceServiceAsync rpcService, Display view, MonitorWorkspaceConstants constants) {
		this.constants = constants;
		this.rpcService = rpcService;
		this.view = view;
		
		EventBus.get().addHandler(FinishCallEvent.TYPE, this);
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
		bind();
	}

	private void bind(){

	}
	
	public Boolean hasInfo(String vehicleIdTag){
		return vehicles.containsKey(vehicleIdTag);
	}

	public void addInfo(Vehicle vehicle, List<Agent> agents){
		vehicles.put(vehicle.getIdTag(), vehicle);
		AbsolutePanel container = new AbsolutePanel();
		
		FlowPanel infoPanel = new FlowPanel();
		infoPanel.addStyleName("inlineFp");
		
		CaptionPanel vehiclePanel = new CaptionPanel(constants.vehicle());
		infoPanel.add(vehiclePanel);
		vehiclePanel.addStyleName("captionPanelStyle");
		final DetailsPresenter details = new DetailsPresenter(vehicle, new Details(), rpcService, constants);
		details.go(vehiclePanel);
		
		CaptionPanel agentsPanel = new CaptionPanel(constants.agents());
		infoPanel.add(agentsPanel);
		agentsPanel.addStyleName("captionPanelStyle");
		
		FlowPanel agentsList = new FlowPanel();
		agentsList.addStyleName("inlineFp");
		agentsPanel.add(agentsList);
		
		for(Agent agent : agents){
			DetailsPresenter agentDetails = new DetailsPresenter(agent, new Details(), rpcService, constants);
			AbsolutePanel ap = new AbsolutePanel();
			agentsList.add(ap);
			agentsDetailsList.add(agentDetails);
			agentDetails.go(ap);
		}
		
		container.add(infoPanel);
		
		PictureTagItem controlItem = new PictureTagItem();
		controlItem.setText(vehicle.getIdTag());
		controlItem.setImage(vehicle.getImageURL());
		controlItem.addPanelStyleName("controlPanel");
		controlItem.addImageStyleName("controlPicture");
		controlItem.addLabelStyleName("controlIdText");
		
		view.addControl(controlItem);
		
		view.addInfo(container);
		
		final int i = view.getControlPanel().getWidgetCount()-1;
		
		controlPanel.put(i, controlItem);
		
		controlItem.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				view.getInfoPanel().showWidget(i);
				Widget pti = controlPanel.get(i);
				for(Widget p : controlPanel.values()){
					p.removeStyleDependentName("view");
				}
				pti.addStyleDependentName("view");
				
				details.doLayout();
				for(DetailsPresenter agentDetails : agentsDetailsList){
					agentDetails.doLayout();
				}
			}
		});
		
		view.setCaptionText(constants.victim());
	}
	
	public void showVictim(Victim victim){
		hasVictim = true;		
		this.victim = new DetailsPresenter(victim, new Details(), rpcService, constants);
		this.victim.go(view.getVictimPanel());
	}

	public Boolean hasVictim(){
		return hasVictim;
	}

	@Override
	public void onFinishCall(FinishCallEvent finishCallEvent) {
		view.clear();
	}
	
	public void doLayout(){
		victim.doLayout();
		for(DetailsPresenter agentDetails : agentsDetailsList){
			agentDetails.doLayout();
		}
	}
}
