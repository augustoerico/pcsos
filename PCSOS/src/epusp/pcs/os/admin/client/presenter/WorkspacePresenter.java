package epusp.pcs.os.admin.client.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;

import epusp.pcs.os.admin.client.AdminResources;
import epusp.pcs.os.admin.client.constants.AdminWorkspaceConstants;
import epusp.pcs.os.admin.client.rpc.IAdminWorkspaceServiceAsync;
import epusp.pcs.os.shared.client.WorkspaceStyles;
import epusp.pcs.os.shared.client.event.ClosePopupEvent;
import epusp.pcs.os.shared.client.event.ClosePopupEvent.ClosePopupHandler;
import epusp.pcs.os.shared.client.event.EventBus;
import epusp.pcs.os.shared.client.presenter.CreateUpdatePresenter;
import epusp.pcs.os.shared.client.presenter.ImageTabPresenter;
import epusp.pcs.os.shared.client.presenter.Presenter;
import epusp.pcs.os.shared.client.view.CreateUpdate;
import epusp.pcs.os.shared.client.view.HeaderButton;
import epusp.pcs.os.shared.client.view.ImageTabPanel;
import epusp.pcs.os.shared.general.AttributeInfoLoader;
import epusp.pcs.os.shared.general.AttributeInfoLoader.IAttributeInfoLoaded;
import epusp.pcs.os.shared.general.Display;
import epusp.pcs.os.shared.general.SelectedRowHandler;
import epusp.pcs.os.shared.model.ICustomAttributes;
import epusp.pcs.os.shared.model.attribute.AttributeInfo;
import epusp.pcs.os.shared.model.person.user.User;
import epusp.pcs.os.shared.model.person.user.agent.Agent;
import epusp.pcs.os.shared.model.person.user.agent.AgentCustomAttributes;
import epusp.pcs.os.shared.model.person.victim.Victim;
import epusp.pcs.os.shared.model.person.victim.VictimCustomAttributes;
import epusp.pcs.os.shared.model.vehicle.Vehicle;
import epusp.pcs.os.shared.model.vehicle.VehicleTypes;
import epusp.pcs.os.shared.model.vehicle.car.Car;
import epusp.pcs.os.shared.model.vehicle.car.CarCustomAttributes;
import epusp.pcs.os.shared.model.vehicle.helicopter.Helicopter;
import epusp.pcs.os.shared.model.vehicle.helicopter.HelicopterCustomAttributes;
import epusp.pcs.os.shared.model.vehicle.motorcycle.Motorcycle;
import epusp.pcs.os.shared.model.vehicle.motorcycle.MotorcycleCustomAttributes;

public class WorkspacePresenter implements Presenter, ClosePopupHandler {

	private final IAdminWorkspaceServiceAsync rpcService;
	private final Display display;
	private final AdminWorkspaceConstants constants;

	private final AdminResources resources = AdminResources.INSTANCE;

	private final int pageSize = 9;

	private final HeaderButton victimHeaderButton;
	private final HeaderButton agentHeaderButton;
	private final HeaderButton vehicleHeaderButton;
	
	private WorkspaceStyles backgroundResources = WorkspaceStyles.INSTANCE;

	private final PopupPanel popup = new PopupPanel(false, false);
	
	private final HashMap<String, AttributeInfo> attributesInfo = new HashMap<String, AttributeInfo>();
	
	private final AttributeInfoLoader loader;

	public WorkspacePresenter(IAdminWorkspaceServiceAsync rpcService, Display view, AdminWorkspaceConstants constants) {
		this.rpcService = rpcService;
		this.display = view;
		this.constants = constants;
		
		popup.setGlassEnabled(true);
		popup.setStyleName("preferencesPopupPanel");
		popup.setGlassStyleName("preferencesPopupGlassPanel");
		
		popup.setSize("1800px", "900px");

		victimHeaderButton = new HeaderButton(constants.client(), resources.newClient().getSafeUri());
		agentHeaderButton = new HeaderButton(constants.agent(), resources.newPolice().getSafeUri());
		vehicleHeaderButton = new HeaderButton(constants.vehicle(), resources.newVehicle().getSafeUri());
		
		loader = new AttributeInfoLoader(attributesInfo, rpcService);

		EventBus.get().addHandler(ClosePopupEvent.TYPE, this);
		
		Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				if(popup.isShowing())
					popup.center();
			}
		});
		
		GWT.<WorkspaceStyles> create(WorkspaceStyles.class).backgroundStyles().ensureInjected();
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
		
		display.setBackgroundStyleName(backgroundResources.backgroundStyles().adminBackground());
		
		VictimTablePresenter victimTablePresenter = new VictimTablePresenter(rpcService, constants, resources, pageSize, new SelectedRowHandler<Victim>() {
			
			@Override
			public void onSelectedRow(final Victim objectSelected) {
				loader.loadCustomAttributes(VictimCustomAttributes.values(), new IAttributeInfoLoaded() {
					@Override
					public void onCustomAttributesLoaded() {
						rpcService.getFullVictim(objectSelected.getEmail(), new AsyncCallback<Victim>() {
							
							@Override
							public void onSuccess(Victim result) {
								List<AttributeInfo> attributes = new ArrayList<AttributeInfo>();
								for(ICustomAttributes attribute : VictimCustomAttributes.values()){
									attributes.add(attributesInfo.get(attribute.getAttributeName()));
								}
								CreateUpdatePresenter createUpdatePresenter = new UpdateVictimPresenter(rpcService, new CreateUpdate(), constants, attributes, result);
								createUpdatePresenter.go(popup);
								popup.center();
							}
							
							@Override
							public void onFailure(Throwable caught) {
							}
						});
					}
				});
			}
		});
		
		HasWidgets victimContainer = display.addType(Victim.class.getName(), victimHeaderButton);
		victimTablePresenter.go(victimContainer);

		AgentTablePresenter agentTablePresenter = new AgentTablePresenter(rpcService, constants, resources, pageSize, new SelectedRowHandler<Agent>() {
			@Override
			public void onSelectedRow(final Agent objectSelected) {
				loader.loadCustomAttributes(AgentCustomAttributes.values(), new IAttributeInfoLoaded() {
					@Override
					public void onCustomAttributesLoaded() {
						rpcService.getFullAgent(objectSelected.getId(), new AsyncCallback<Agent>() {
							
							@Override
							public void onSuccess(Agent result) {
								List<AttributeInfo> attributes = new ArrayList<AttributeInfo>();
								for(ICustomAttributes attribute : AgentCustomAttributes.values()){
									attributes.add(attributesInfo.get(attribute.getAttributeName()));
								}
								CreateUpdatePresenter createUpdatePresenter = new UpdateAgentPresenter(rpcService, new CreateUpdate(), constants, attributes, result);
								createUpdatePresenter.go(popup);
								popup.center();
							}
							
							@Override
							public void onFailure(Throwable caught) {
							}
						});
					}
				});
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
				CarTablePresenter carTablePresenter = new CarTablePresenter(rpcService, constants, resources, pageSize, new SelectedRowHandler<Car>() {
					
					@Override
					public void onSelectedRow(final Car objectSelected) {
						loader.loadCustomAttributes(CarCustomAttributes.values(), new IAttributeInfoLoaded() {
							@Override
							public void onCustomAttributesLoaded() {
								rpcService.getFullCar(objectSelected.getId(), new AsyncCallback<Car>() {
									
									@Override
									public void onSuccess(Car result) {
										List<AttributeInfo> attributes = new ArrayList<AttributeInfo>();
										for(ICustomAttributes attribute : CarCustomAttributes.values()){
											attributes.add(attributesInfo.get(attribute.getAttributeName()));
										}
										CreateUpdatePresenter createUpdatePresenter = new UpdateCarPresenter(rpcService, new CreateUpdate(), constants, attributes, loader, result);
										createUpdatePresenter.go(popup);
										popup.center();
									}
									
									@Override
									public void onFailure(Throwable caught) {
									}
								});
							}
						});
					}
				});
				carTablePresenter.go(panel);
				imageTabPresenter.addInfo(constants.car(), resources.car().getSafeUri().asString(), panel);
				break;
			case Helicopter:
				HelicopterTablePresenter helicopterTablePresenter = new HelicopterTablePresenter(rpcService, constants, resources, pageSize, new SelectedRowHandler<Helicopter>() {
					
					@Override
					public void onSelectedRow(final Helicopter objectSelected) {
						loader.loadCustomAttributes(HelicopterCustomAttributes.values(), new IAttributeInfoLoaded() {
							@Override
							public void onCustomAttributesLoaded() {
								rpcService.getFullHelicopter(objectSelected.getId(), new AsyncCallback<Helicopter>() {
									
									@Override
									public void onSuccess(Helicopter result) {
										List<AttributeInfo> attributes = new ArrayList<AttributeInfo>();
										for(ICustomAttributes attribute : HelicopterCustomAttributes.values()){
											attributes.add(attributesInfo.get(attribute.getAttributeName()));
										}
										CreateUpdatePresenter createUpdatePresenter = new UpdateHelicopterPresenter(rpcService, new CreateUpdate(), constants, attributes, loader, result);
										createUpdatePresenter.go(popup);
										popup.center();
									}
									
									@Override
									public void onFailure(Throwable caught) {
									}
								});
							}
						});
					}
				});
				helicopterTablePresenter.go(panel);
				imageTabPresenter.addInfo(constants.helicopter(), resources.helicopter().getSafeUri().asString(), panel);
				break;
			case Motorcycle:
				MotorcycleTablePresenter motorcycleTablePresenter = new MotorcycleTablePresenter(rpcService, constants, resources, pageSize, new SelectedRowHandler<Motorcycle>() {

					@Override
					public void onSelectedRow(final Motorcycle objectSelected) {
						loader.loadCustomAttributes(MotorcycleCustomAttributes.values(), new IAttributeInfoLoaded() {
							@Override
							public void onCustomAttributesLoaded() {
								rpcService.getFullMotorcycle(objectSelected.getId(), new AsyncCallback<Motorcycle>() {

									@Override
									public void onSuccess(Motorcycle result) {
										List<AttributeInfo> attributes = new ArrayList<AttributeInfo>();
										for(ICustomAttributes attribute : MotorcycleCustomAttributes.values()){
											attributes.add(attributesInfo.get(attribute.getAttributeName()));
										}
										CreateUpdatePresenter createUpdatePresenter = new UpdateMotorcyclePresenter(rpcService, new CreateUpdate(), constants, attributes, loader, result);
										createUpdatePresenter.go(popup);
										popup.center();
									}

									@Override
									public void onFailure(Throwable caught) {
									}
								});
							}
						});
					}
				});
				motorcycleTablePresenter.go(panel);
				imageTabPresenter.addInfo(constants.motorcycle(), resources.motorcycle().getSafeUri().asString(), panel);
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
						String query = Window.Location.getQueryString();
						query = query.replaceAll("&?locale=[^&]+", "");
						String path = Window.Location.getProtocol().concat("//").concat(Window.Location.getHost()).concat("/")
								.concat("PCSOS.html").concat(query);
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
					loader.loadCustomAttributes(VictimCustomAttributes.values(), new IAttributeInfoLoaded() {
						@Override
						public void onCustomAttributesLoaded() {
							List<AttributeInfo> attributes = new ArrayList<AttributeInfo>();
							for(ICustomAttributes attribute : VictimCustomAttributes.values()){
								attributes.add(attributesInfo.get(attribute.getAttributeName()));
							}
							CreateUpdatePresenter createUpdatePresenter = new CreateVictimPresenter(rpcService, new CreateUpdate(), constants, attributes);
							createUpdatePresenter.go(popup);
							popup.center();
						}
					});
				}
			}
		});

		agentHeaderButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(agentHeaderButton.isEnabled()){
					loader.loadCustomAttributes(AgentCustomAttributes.values(), new IAttributeInfoLoaded() {
						@Override
						public void onCustomAttributesLoaded() {
							List<AttributeInfo> attributes = new ArrayList<AttributeInfo>();
							for(ICustomAttributes attribute : AgentCustomAttributes.values()){
								attributes.add(attributesInfo.get(attribute.getAttributeName()));
							}
							CreateUpdatePresenter createUpdatePresenter = new CreateAgentPresenter(rpcService, new CreateUpdate(), constants, attributes);
							createUpdatePresenter.go(popup);
							popup.center();
						}
					});
				}
			}
		});

		vehicleHeaderButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(vehicleHeaderButton.isEnabled()){
					CreateUpdatePresenter createUpdatePresenter = new CreateVehiclePresenter(rpcService, new CreateUpdate(), constants, loader);
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
