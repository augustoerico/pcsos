package epusp.pcs.os.monitor.client.presenter;

import java.util.HashMap;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import epusp.pcs.os.monitor.client.constants.MonitorWorkspaceConstants;
import epusp.pcs.os.monitor.client.rpc.IMonitorWorkspaceServiceAsync;
import epusp.pcs.os.monitor.client.view.Details;
import epusp.pcs.os.shared.client.presenter.Presenter;
import epusp.pcs.os.shared.model.person.Victim;
import epusp.pcs.os.shared.model.person.user.Agent;
import epusp.pcs.os.shared.model.vehicle.Vehicle;

public class CallInfoPresenter implements Presenter{

	public interface Display{
		HasWidgets getVictimPanel();
		FlowPanel getControlPanel();
		DeckLayoutPanel getInfoPanel();
		void addInfo(Widget w);
		void addControl(Widget w);
		Widget asWidget();
	}
	
	private IMonitorWorkspaceServiceAsync rpcService; 
	private Display view; 
	private MonitorWorkspaceConstants constants;
	private Boolean hasVictim = false;
	
	private DetailsPresenter victim;
	private HashMap<String, Vehicle> vehicles = new HashMap<String, Vehicle>();
	
	private HashMap<Integer, FlowPanel> controlPanel = new HashMap<Integer, FlowPanel>();

	public CallInfoPresenter(IMonitorWorkspaceServiceAsync rpcService, Display view, MonitorWorkspaceConstants constants) {
		this.constants = constants;
		this.rpcService = rpcService;
		this.view = view;
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
		
		DetailsPresenter details = new DetailsPresenter(vehicle, new Details(), rpcService, constants);
		details.go(infoPanel);
		
		for(Agent agent : agents){
			DetailsPresenter agentDetails = new DetailsPresenter(agent, new Details(), rpcService, constants);
			AbsolutePanel ap = new AbsolutePanel();
			ap.setStyleName("infoItem");
			infoPanel.add(ap);
			agentDetails.go(ap);
		}
		
		container.add(infoPanel);
		
		FlowPanel fp = new FlowPanel();
		fp.addStyleName("controlPanel");
		Image picture = new Image(vehicle.getImageURL());
		picture.addStyleName("controlPicture");
		fp.add(picture);
		Label control = new Label(vehicle.getIdTag());
		control.addStyleName("controlIdText");
		fp.add(control);
		view.addControl(fp);
		
		view.addInfo(container);
		
		final int i = view.getControlPanel().getWidgetCount()-1;
		
		controlPanel.put(i, fp);
		
		control.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				view.getInfoPanel().showWidget(i);
				FlowPanel fp = controlPanel.get(i);
				for(FlowPanel p : controlPanel.values()){
					p.removeStyleDependentName("view");
				}
				fp.addStyleDependentName("view");
			}
		});
		
		picture.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				view.getInfoPanel().showWidget(i);
				FlowPanel fp = controlPanel.get(i);
				for(FlowPanel p : controlPanel.values()){
					p.removeStyleDependentName("view");
				}
				fp.addStyleDependentName("view");
			}
		});
	}
	
	public void showVictim(Victim victim){
		hasVictim = true;		
		this.victim = new DetailsPresenter(victim, new Details(), rpcService, constants);
		this.victim.go(view.getVictimPanel());
	}

	public Boolean hasVictim(){
		return hasVictim;
	}
}
