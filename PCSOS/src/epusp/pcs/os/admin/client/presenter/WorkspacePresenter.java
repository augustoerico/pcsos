package epusp.pcs.os.admin.client.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

import epusp.pcs.os.admin.client.AdminResources;
import epusp.pcs.os.admin.client.VehicleTableController;
import epusp.pcs.os.admin.client.constants.AdminWorkspaceConstants;
import epusp.pcs.os.admin.client.rpc.IAdminWorkspaceServiceAsync;
import epusp.pcs.os.shared.client.event.ClosePopupEvent;
import epusp.pcs.os.shared.client.event.ClosePopupEvent.ClosePopupHandler;
import epusp.pcs.os.shared.client.event.EventBus;
import epusp.pcs.os.shared.client.presenter.CreateUpdatePresenter;
import epusp.pcs.os.shared.client.presenter.ImageTabPresenter;
import epusp.pcs.os.shared.client.presenter.Presenter;
import epusp.pcs.os.shared.client.view.CreateUpdate;
import epusp.pcs.os.shared.client.view.HeaderButton;
import epusp.pcs.os.shared.client.view.ImageTabPanel;
import epusp.pcs.os.shared.general.SelectedRowHandler;
import epusp.pcs.os.shared.model.person.Victim;
import epusp.pcs.os.shared.model.person.user.Agent;
import epusp.pcs.os.shared.model.person.user.User;
import epusp.pcs.os.shared.model.vehicle.Car;
import epusp.pcs.os.shared.model.vehicle.Helicopter;
import epusp.pcs.os.shared.model.vehicle.Vehicle;
import epusp.pcs.os.shared.model.vehicle.VehicleTypes;

public class WorkspacePresenter implements Presenter, ClosePopupHandler {

	public interface Display {
		void setUserImage(String url);
		void setUserImage(ImageResource resource);
		void setUsername(String username);
		Widget asWidget();
		Image getLogout();
		Image getPreferences();
		Boolean hasType(String type);
		HasWidgets addType(String type, Widget header);
		void addSelectionHandler(SelectionHandler<Integer> handler);
	}

	private final IAdminWorkspaceServiceAsync rpcService;
	private final Display display;
	private final AdminWorkspaceConstants constants;

	private final AdminResources resources = AdminResources.INSTANCE;

	private final int pageSize = 2;

	private final HeaderButton victimHeaderButton;
	private final HeaderButton agentHeaderButton;
	private final HeaderButton vehicleHeaderButton;

	private final PopupPanel popup = new PopupPanel(false, false);

	public WorkspacePresenter(IAdminWorkspaceServiceAsync rpcService, Display view, AdminWorkspaceConstants constants) {
		this.rpcService = rpcService;
		this.display = view;
		this.constants = constants;

		victimHeaderButton = new HeaderButton(constants.client(), resources.newClient().getSafeUri());
		agentHeaderButton = new HeaderButton(constants.agent(), resources.newPolice().getSafeUri());
		vehicleHeaderButton = new HeaderButton(constants.vehicle(), resources.newVehicle().getSafeUri());

		EventBus.get().addHandler(ClosePopupEvent.TYPE, this);
		
		Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				if(popup.isShowing())
					popup.center();
			}
		});
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
		
		final VictimTablePresenter victimTablePresenter = new VictimTablePresenter(rpcService, constants, pageSize, new SelectedRowHandler<Victim>() {
			
			@Override
			public void onSelectedRow(Victim objectSelected) {
				popup.setSize("800px", "500px");
				CreateUpdatePresenter createUpdatePresenter = new UpdateVictimPresenter(rpcService, new CreateUpdate(), constants, objectSelected);
				createUpdatePresenter.go(popup);
				popup.center();
			}
		});
		
		HasWidgets victimContainer = display.addType(Victim.class.getName(), victimHeaderButton);
		victimTablePresenter.go(victimContainer);

		AgentTablePresenter agentTablePresenter = new AgentTablePresenter(rpcService, constants, pageSize, new SelectedRowHandler<Agent>() {
			
			@Override
			public void onSelectedRow(Agent objectSelected) {
				popup.setSize("800px", "500px");
				CreateUpdatePresenter createUpdatePresenter = new UpdateAgentPresenter(rpcService, new CreateUpdate(), constants, objectSelected);
				createUpdatePresenter.go(popup);
				popup.center();
			}
		});
		HasWidgets agentContainer = display.addType(Agent.class.getName(), agentHeaderButton);
		agentTablePresenter.go(agentContainer);

		
		HasWidgets vehicleContainer = display.addType(Vehicle.class.getName(), vehicleHeaderButton);
		ImageTabPresenter imageTabPresenter = new ImageTabPresenter(new ImageTabPanel());
		imageTabPresenter.go(vehicleContainer);
		
		for(VehicleTypes type : VehicleTypes.values()){
			AbsolutePanel panel = new AbsolutePanel();
			panel.setSize("100%", "100%");
			switch (type) {
			case Car:
				CarTablePresenter carTablePresenter = new CarTablePresenter(rpcService, constants, pageSize, new SelectedRowHandler<Car>() {
					
					@Override
					public void onSelectedRow(Car objectSelected) {
						popup.setSize("800px", "500px");
						CreateUpdatePresenter createUpdatePresenter = new UpdateCarPresenter(rpcService, new CreateUpdate(), constants, objectSelected);
						createUpdatePresenter.go(popup);
						popup.center();
					}
				});
				carTablePresenter.go(panel);
				imageTabPresenter.addInfo(constants.car(), resources.car().getSafeUri().asString(), panel);
				break;
			case Helicopter:
				HelicopterTablePresenter helicopterTablePresenter = new HelicopterTablePresenter(rpcService, constants, pageSize, new SelectedRowHandler<Helicopter>() {
					
					@Override
					public void onSelectedRow(Helicopter objectSelected) {
						// TODO Auto-generated method stub
						System.out.println("todo");
					}
				});
				helicopterTablePresenter.go(panel);
				imageTabPresenter.addInfo(constants.helicopter(), resources.helicopter().getSafeUri().asString(), panel);
				break;
			default:
				break;
			}
		}

		victimHeaderButton.enable();
		agentHeaderButton.disable();
		vehicleHeaderButton.disable();

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

		victimHeaderButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(victimHeaderButton.isEnabled()){
					popup.setSize("800px", "500px");
					CreateUpdatePresenter createUpdatePresenter = new CreateVictimPresenter(rpcService, new CreateUpdate(), constants);
					createUpdatePresenter.go(popup);
					popup.center();
				}
			}
		});

		agentHeaderButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(agentHeaderButton.isEnabled()){
					popup.setSize("800px", "500px");
					CreateUpdatePresenter createUpdatePresenter = new CreateAgentPresenter(rpcService, new CreateUpdate(), constants);
					createUpdatePresenter.go(popup);
					popup.center();
				}
			}
		});

		vehicleHeaderButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(vehicleHeaderButton.isEnabled()){
					popup.setSize("800px", "500px");
					CreateUpdatePresenter createUpdatePresenter = new CreateVehiclePresenter(rpcService, new CreateUpdate(), constants);
					createUpdatePresenter.go(popup);
					popup.center();
				}
			}
		});

		display.addSelectionHandler(new SelectionHandler<Integer>() {

			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				Integer i = event.getSelectedItem();

				victimHeaderButton.disable();
				agentHeaderButton.disable();
				vehicleHeaderButton.disable();

				if(i == 0)
					victimHeaderButton.enable();
				else if(i == 1)
					agentHeaderButton.enable();
				else if(i == 2)
					vehicleHeaderButton.enable();
			}
		});

	}

	public Image getPreferencesButton() {
		return display.getPreferences();
	}

	@Override
	public void onClosingPopup(ClosePopupEvent closePopupEvent) {
		popup.clear();
		popup.hide();
	}
}
