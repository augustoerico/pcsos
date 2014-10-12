package epusp.pcs.os.monitor.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

import epusp.pcs.os.monitor.client.MonitorResources;
import epusp.pcs.os.monitor.client.presenter.WorkspacePresenter.Display;

public class Workspace extends Composite implements Display{

	private static WorkspaceUiBinder uiBinder = GWT
			.create(WorkspaceUiBinder.class);
	
	@UiField
	DeckLayoutPanel deckPanel;
	
	@UiField
	Image logo, info, map, reinforcements, picture;
	
	@UiField
	RadioButton mapRadio, infoRadio, reinforcementsRadio;
	
	private MonitorResources resources = MonitorResources.INSTANCE;

	interface WorkspaceUiBinder extends UiBinder<Widget, Workspace> {
	}

	public Workspace() {
		initWidget(uiBinder.createAndBindUi(this));
		logo.setResource(resources.logo());
		info.setResource(resources.info());
		map.setResource(resources.map());
		reinforcements.setResource(resources.reinforcements());
		deckPanel.showWidget(0);
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
	public Widget asWidget(){
		return this;
	}

	@Override
	public void setUserImage(String url) {
		picture.setUrl(url);
	}
}
