package epusp.pcs.os.monitor.client.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

import epusp.pcs.os.model.person.user.User;
import epusp.pcs.os.monitor.client.MonitorResources;
import epusp.pcs.os.monitor.client.rpc.IMonitorWorkspaceServiceAsync;
import epusp.pcs.os.server.login.UserLogin;
import epusp.pcs.os.shared.client.presenter.Presenter;

public class WorkspacePresenter implements Presenter{

	public interface Display {
		void showMap();
		void showCallInfo();
		void showAvailableReinforcements();
		void setUserImage(String url);
		void setUserImage(ImageResource resource);
		RadioButton getMapButton();
		RadioButton getInfoButton();
		RadioButton getReinforcementsButton();
		Widget asWidget();
	}

	private final IMonitorWorkspaceServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final Display display;
	
	private final MonitorResources resources = MonitorResources.INSTANCE;

	public WorkspacePresenter(IMonitorWorkspaceServiceAsync rpcService, HandlerManager eventBus, Display view) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.display = view;
	}

	@Override
	public void go(HasWidgets container) {
	    bind();
	    container.clear();
	    container.add(display.asWidget());
	    rpcService.getUserInfo(new AsyncCallback<User>() {
			
			@Override
			public void onSuccess(User result) {
				if(result != null){
					display.setUserImage(result.getPictureURL());
				}else{
					display.setUserImage(resources.user());
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				System.out.println(caught.getMessage());
			}
		});
	}

	public void bind() {
		display.getMapButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				display.showMap();
			}
		});
		
		display.getInfoButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				display.showCallInfo();
			}
		});
		
		display.getReinforcementsButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				display.showAvailableReinforcements();
			}
		});
	}

}
