package epusp.pcs.os.shared.client.view;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

import epusp.pcs.os.shared.client.SharedResources;
import epusp.pcs.os.shared.general.Display;

public class Workspace extends Composite implements Display{

	private static WorkspaceUiBinder uiBinder = GWT
			.create(WorkspaceUiBinder.class);
	
	@UiField
	AbsolutePanel panel, desk, background;
	
	@UiField
	Image logo, picture, preferences, logout;
		
	@UiField
	Label username;
	
	@UiField
	StackLayoutPanel options;
	
	public interface Sytles extends CssResource{
		String optionsPanel();
	}
	
	@UiField
	Sytles style;
	
	private SharedResources resources = SharedResources.INSTANCE;
	
	private HashMap<String, HasWidgets> containers = new HashMap<String, HasWidgets>(); 

	interface WorkspaceUiBinder extends UiBinder<Widget, Workspace> {
	}
	
	public Workspace() {
		initWidget(uiBinder.createAndBindUi(this));
		logo.setResource(resources.logo());
		preferences.setResource(resources.preferences());
		logout.setResource(resources.logout());
		
		int height = Window.getClientHeight();
		int width = Window.getClientWidth();
		
		if(width-40 > 1200){
			background.setWidth(width-40 + "px");
			options.setWidth(width-40 + "px");
		}
		
		if(height-80 > 800){
			background.setHeight(height-80 + "px");
			options.setHeight(height-80 + "px");
		}
		Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				int height = Window.getClientHeight();
				int width = Window.getClientWidth();
				
				if(width-40 > 1200){
					background.setWidth(width-40 + "px");
					options.setWidth(width-40 + "px");
				}
				if(height-80 > 800){
					background.setHeight(height-80 + "px");
					options.setHeight(height-80 + "px");
				}
			}
		});
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
	
	@Override
	public HasWidgets addType(String type, Widget header) {
		if(!hasType(type)){			
			AbsolutePanel absolutePanel = new AbsolutePanel();
			absolutePanel.setSize("100%", "100%");
			absolutePanel.addStyleName(style.optionsPanel());
			options.add(absolutePanel, header, 40);
			
			containers.put(type, absolutePanel);
			
			return absolutePanel;
		}
		
		return null;
	}
	
	@Override
	public Boolean hasType(String type){
		return containers.containsKey(type);
	}
	
	@Override
	public void addSelectionHandler(SelectionHandler<Integer> handler){
		options.addSelectionHandler(handler);
	}
	
	@Override
	public void setBackgroundStyleName(String style){
		panel.setStyleName(style);
	}
}
