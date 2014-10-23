package epusp.pcs.os.monitor.client.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

import epusp.pcs.os.monitor.client.MonitorResources;
import epusp.pcs.os.monitor.client.constants.MonitorWorkspaceConstants;
import epusp.pcs.os.monitor.client.event.HideShowTrafficEvent;
import epusp.pcs.os.monitor.client.rpc.IMonitorWorkspaceServiceAsync;
import epusp.pcs.os.shared.client.event.EventBus;
import epusp.pcs.os.shared.client.presenter.Presenter;
import epusp.pcs.os.shared.model.person.user.User;

public class WorkspacePresenter implements Presenter {

	public interface Display {
		void showMap();
		void showCallInfo();
		void showAvailableReinforcements();
		void setUserImage(String url);
		void setUserImage(ImageResource resource);
		void setMapTitle(String title);
		void setInfoTitle(String title);
		void setReinforcements(String title);
		void setUsername(String username);
		RadioButton getMapButton();
		RadioButton getInfoButton();
		RadioButton getReinforcementsButton();
		Widget asWidget();
		HasWidgets getMapsArea();
		HasWidgets getInfoArea();
		ToggleButton getTrafficButton();
		Image getLogout();
		Image getPreferences();
	}

	private final IMonitorWorkspaceServiceAsync rpcService;
	private final Display display;
	private final MonitorWorkspaceConstants constants;
	
	private final MonitorResources resources = MonitorResources.INSTANCE;

	public WorkspacePresenter(IMonitorWorkspaceServiceAsync rpcService, Display view, MonitorWorkspaceConstants constants) {
		this.constants = constants;
		this.rpcService = rpcService;
		this.display = view;
	}

	@Override
	public void go(HasWidgets container) {
	    container.clear();
	    container.add(display.asWidget());
	    rpcService.getUserInfo(new AsyncCallback<User>() {
			
			@Override
			public void onSuccess(User result) {
				if(result != null){
					display.setUserImage(result.getPictureURL());
					display.setUsername(result.getName());
				}else{
					display.setUserImage(resources.user());
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				System.out.println(caught.getMessage());
			}
		});
	    bind();
	}

	private void bind() {
		display.getMapButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				display.showMap();
				display.getTrafficButton().setVisible(true);
			}
		});
		
		display.getInfoButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				display.showCallInfo();
				display.getTrafficButton().setVisible(false);
			}
		});
		
		display.getReinforcementsButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				display.showAvailableReinforcements();
				display.getTrafficButton().setVisible(false);
			}
		});
		
		display.getTrafficButton().addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				EventBus.get().fireEvent(new HideShowTrafficEvent(event.getValue()));
			}
		});
		
		display.getLogout().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				rpcService.logout(new AsyncCallback<Void>() {
					
					@Override
					public void onSuccess(Void result) {
						String path = Window.Location.getProtocol().concat("//").concat(Window.Location.getHost()).concat("/")
								.concat("PCSOS.html").concat(Window.Location.getQueryString());
						Window.Location.replace(path);
					}
					
					@Override
					public void onFailure(Throwable caught) {	
					}
				});
			}
		});
		
		display.getTrafficButton().setTitle(constants.showHideTraffic());
		display.setInfoTitle(constants.info());
		display.setMapTitle(constants.map());
		display.setReinforcements(constants.reinforcements());
	}

	public HasWidgets getMapsArea() {
		return display.getMapsArea();
	}

	public HasWidgets getInfoArea(){
		return display.getInfoArea();
	}
	
	public Image getPreferencesButton() {
		return display.getPreferences();
	}

}
