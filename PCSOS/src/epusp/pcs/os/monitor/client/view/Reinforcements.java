package epusp.pcs.os.monitor.client.view;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

import epusp.pcs.os.monitor.client.presenter.ReinforcementsPresenter.Display;

public class Reinforcements extends Composite implements Display {

	private static ReinforcementsUiBinder uiBinder = GWT
			.create(ReinforcementsUiBinder.class);

	interface ReinforcementsUiBinder extends UiBinder<Widget, Reinforcements> {
	}
	
	@UiField
	AbsolutePanel background, area;
	
	@UiField
	Image add, remove, send;
	
	@UiField
	FlowPanel choosen;
	
	@UiField
	StackLayoutPanel options;
	
	private HashMap<String, HasWidgets> containers = new HashMap<String, HasWidgets>();
	
	public interface Sytles extends CssResource{
		String optionsPanel();
	}
	
	@UiField
	Sytles style;

	public Reinforcements() {
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
	public void addType(String type, Widget header) {
		if(!containers.containsKey(type)){
			FlowPanel flowPanel = new FlowPanel();
			flowPanel.setSize("100%", "100%");
			flowPanel.addStyleName(style.optionsPanel());
			options.add(flowPanel, header, 40);
			containers.put(type, flowPanel);
		}
	}
	
	@Override
	public Boolean hasType(String type){
		return containers.containsKey(type);
	}
	
	@Override
	public void addItem(String type, Widget widget){
		HasWidgets panel = containers.get(type);
		panel.add(widget);
	}
	
	@Override
	public void addSelectedItem(Widget widget){
		choosen.add(widget);
	}
	
	@Override
	public void addHandler(ClickHandler handler){
		add.addClickHandler(handler);
	}
	
	@Override
	public void removeHandler(ClickHandler handler){
		remove.addClickHandler(handler);
	}
	
	@Override
	public void sendHandler(ClickHandler handler){
		send.addClickHandler(handler);
	}
	
	@Override
	public void clearChoosen(){
		choosen.clear();
	}
	
	@Override
	public void clearOptions(){
		options.clear();
		containers.clear();
	}
	
	@Override
	public void removeItem(String type, Widget widget){
		HasWidgets panel = containers.get(type);
		panel.remove(widget);
		choosen.remove(widget);
	}
	
	@Override
	public Widget asWidget(){
		return this;
	}

	@Override
	public void clear() {
		clearOptions();
		clearChoosen();
	}
}
