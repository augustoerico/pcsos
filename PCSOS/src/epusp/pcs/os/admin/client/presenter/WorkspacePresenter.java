package epusp.pcs.os.admin.client.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import epusp.pcs.os.admin.client.AdminResources;
import epusp.pcs.os.admin.client.VehicleTableController;
import epusp.pcs.os.admin.client.constants.AdminWorkspaceConstants;
import epusp.pcs.os.admin.client.rpc.IAdminWorkspaceServiceAsync;
import epusp.pcs.os.shared.client.presenter.Presenter;
import epusp.pcs.os.shared.model.person.Victim;
import epusp.pcs.os.shared.model.person.user.Agent;
import epusp.pcs.os.shared.model.person.user.User;
import epusp.pcs.os.shared.model.vehicle.Vehicle;

public class WorkspacePresenter implements Presenter {

	public interface Display {
		void setUserImage(String url);
		void setUserImage(ImageResource resource);
		void setUsername(String username);
		Widget asWidget();
		Image getLogout();
		Image getPreferences();
		Boolean hasType(String type);
		HasWidgets addType(String type, Widget header);
	}

	private final IAdminWorkspaceServiceAsync rpcService;
	private final Display display;
	private final AdminWorkspaceConstants constants;

	private final AdminResources resources = AdminResources.INSTANCE;

	private final int pageSize = 2;

	public WorkspacePresenter(IAdminWorkspaceServiceAsync rpcService, Display view, AdminWorkspaceConstants constants) {
		this.rpcService = rpcService;
		this.display = view;
		this.constants = constants;
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
		
		VictimTablePresenter victimTablePresenter = new VictimTablePresenter(rpcService, constants, pageSize);
		HasWidgets victimContainer = display.addType(Victim.class.getName(), new Label(constants.client()));
		victimTablePresenter.go(victimContainer);

		AgentTablePresenter agentTablePresenter = new AgentTablePresenter(rpcService, constants, pageSize);
		HasWidgets agentContainer = display.addType(Agent.class.getName(), new Label(constants.agent()));
		agentTablePresenter.go(agentContainer);
		
		VehicleTableController vehicleTablePresenter = new VehicleTableController(rpcService, constants, pageSize);
		HasWidgets vehicleContainer = display.addType(Vehicle.class.getName(), new Label(constants.vehicle()));
		vehicleTablePresenter.go(vehicleContainer);

		bind();
	}

	private void bind() {		
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

	}

	public Image getPreferencesButton() {
		return display.getPreferences();
	}


}
