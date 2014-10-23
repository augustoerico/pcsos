package epusp.pcs.os.monitor.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

import epusp.pcs.os.monitor.client.MonitorResources;
import epusp.pcs.os.monitor.client.presenter.WorkspacePresenter.Display;

public class Workspace extends Composite implements Display{

	private static WorkspaceUiBinder uiBinder = GWT
			.create(WorkspaceUiBinder.class);
	
	@UiField
	DeckLayoutPanel deckPanel;
	
	@UiField
	Image logo, info, map, reinforcements, picture, preferences, logout;
	
	@UiField
	RadioButton mapRadio, infoRadio, reinforcementsRadio;
	
	@UiField
	AbsolutePanel mapsArea, infoArea;

	@UiField
	ToggleButton trafficButton, phoneButton;
	
	@UiField
	Label username;

	public interface TrafficSytle extends CssResource {
		String showTraffic();
		String hideTraffic();
		String phoneOn();
		String phoneOff();
	}

	@UiField 
	TrafficSytle style;
	
	private MonitorResources resources = MonitorResources.INSTANCE;

	interface WorkspaceUiBinder extends UiBinder<Widget, Workspace> {
	}

	public Workspace() {
		initWidget(uiBinder.createAndBindUi(this));
		logo.setResource(resources.logo());
		info.setResource(resources.info());
		map.setResource(resources.map());
		preferences.setResource(resources.preferences());
		logout.setResource(resources.logout());
		reinforcements.setResource(resources.reinforcements());
		deckPanel.showWidget(0);
		
		int height = Window.getClientHeight();
		int width = Window.getClientWidth();
		
		mapsArea.setWidth(width-40 + "px");
		mapsArea.setHeight(0.9*height-20 + "px");
		
		Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				int height = Window.getClientHeight();
				int width = Window.getClientWidth();
				
				mapsArea.setWidth(width-40 + "px");
				mapsArea.setHeight(0.9*height-20 + "px");
			}
		});
		
		trafficButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(trafficButton.isDown()){
					trafficButton.removeStyleName(style.hideTraffic());
					trafficButton.addStyleName(style.showTraffic());
				}else{
					trafficButton.removeStyleName(style.showTraffic());
					trafficButton.addStyleName(style.hideTraffic());
				}
			}
		});
		
		phoneButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {

				if(phoneButton.isDown()){
					phoneButton.removeStyleName(style.phoneOff());
					phoneButton.addStyleName(style.phoneOn());
				}else{
					phoneButton.removeStyleName(style.phoneOn());
					phoneButton.addStyleName(style.phoneOff());
				}
			}
		});
	}
	
	@Override
	public void showMap(){
		deckPanel.showWidget(0);
	}
	
	@Override
	public void showCallInfo(){
		deckPanel.showWidget(1);
	}
	
	@Override
	public void showAvailableReinforcements(){
		deckPanel.showWidget(2);
	}
	
	@Override
	public RadioButton getMapButton() {
		return mapRadio;
	}

	@Override
	public RadioButton getInfoButton() {
		return infoRadio;
	}

	@Override
	public RadioButton getReinforcementsButton() {
		return reinforcementsRadio;
	}
	
	@Override
	public ToggleButton getTrafficButton() {
		return trafficButton;
	}

	@Override
	public Widget asWidget(){
		return this;
	}

	@Override
	public void setUserImage(String url) {
		picture.setUrl(url);
	}

	@Override
	public void setUserImage(ImageResource resource) {
		picture.setResource(resource);
	}

	@Override
	public HasWidgets getMapsArea() {
		return mapsArea;
	}
	
	@Override
	public HasWidgets getInfoArea() {
		return infoArea;
	}

	@Override
	public void setMapTitle(String title) {
		map.setTitle(title);
	}

	@Override
	public void setInfoTitle(String title) {
		info.setTitle(title);
	}

	@Override
	public void setReinforcements(String title) {
		reinforcements.setTitle(title);
	}

	@Override
	public void setUsername(String username) {
		this.username.setText(username);
	}

	@Override
	public Image getLogout() {
		return logout;
	}

	@Override
	public Image getPreferences() {
		return preferences;
	}
	
	
}
