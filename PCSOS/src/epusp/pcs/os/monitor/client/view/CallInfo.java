package epusp.pcs.os.monitor.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import epusp.pcs.os.monitor.client.presenter.CallInfoPresenter.Display;

public class CallInfo extends Composite implements Display {

	private static CallInfoUiBinder uiBinder = GWT
			.create(CallInfoUiBinder.class);

	interface CallInfoUiBinder extends UiBinder<Widget, CallInfo> {
	}
	
	@UiField
	AbsolutePanel victimPanel, background, area;
	
	@UiField
	FlowPanel control;
	
	@UiField
	DeckLayoutPanel infoPanel;
	
	public CallInfo(){
		initWidget(uiBinder.createAndBindUi(this));
		
		int height = Window.getClientHeight();
		int width = Window.getClientWidth();
				
		if(width-40 > 1200){
			background.setWidth(width-40 + "px");
			area.setWidth(width-40 + "px");
		}
		
		if(height-125 > 800){
			background.setHeight(height-125 + "px");
			area.setHeight(height-125 + "px");
		}
		
		Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				int height = Window.getClientHeight();
				int width = Window.getClientWidth();
				
				if(width-40 > 1200){
					background.setWidth(width-40 + "px");
					area.setWidth(width-40 + "px");
				}
				
				if(height-125 > 800){
					background.setHeight(height-125 + "px");
					area.setHeight(height-125 + "px");
				}
			}
		});
	}
	
	@Override
	public DeckLayoutPanel getInfoPanel() {
		return infoPanel;
	}
	
	@Override
	public FlowPanel getControlPanel() {
		return control;
	}
	
	@Override
	public HasWidgets getVictimPanel(){
		return victimPanel;
	}

	@Override
	public void addControl(Widget w) {
		AbsolutePanel p = new AbsolutePanel();
		p.add(w);
		p.addStyleName("controlItem");
		control.add(p);
	}

	@Override
	public void addInfo(Widget w) {
		AbsolutePanel p = new AbsolutePanel();
		p.setSize("100%", "500px");
		p.addStyleName("detailsPanel");
		p.add(w);
		infoPanel.add(p);
	}
	
	@Override
	public Widget asWidget(){
		return this;
	}

	@Override
	public void clear() {
		victimPanel.clear();
		control.clear();
		infoPanel.clear();
	}

}
