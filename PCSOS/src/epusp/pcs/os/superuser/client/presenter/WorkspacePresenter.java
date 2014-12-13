package epusp.pcs.os.superuser.client.presenter;

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
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;

import epusp.pcs.os.shared.client.WorkspaceStyles;
import epusp.pcs.os.shared.client.event.ClosePopupEvent;
import epusp.pcs.os.shared.client.event.ClosePopupEvent.ClosePopupHandler;
import epusp.pcs.os.shared.client.event.EventBus;
import epusp.pcs.os.shared.client.presenter.CreateUpdatePresenter;
import epusp.pcs.os.shared.client.presenter.Presenter;
import epusp.pcs.os.shared.client.view.CreateUpdate;
import epusp.pcs.os.shared.client.view.HeaderButton;
import epusp.pcs.os.shared.general.AttributeInfoLoader;
import epusp.pcs.os.shared.general.AttributeInfoLoader.IAttributeInfoLoaded;
import epusp.pcs.os.shared.general.Display;
import epusp.pcs.os.shared.general.SelectedRowHandler;
import epusp.pcs.os.shared.model.ICustomAttributes;
import epusp.pcs.os.shared.model.attribute.AttributeInfo;
import epusp.pcs.os.shared.model.person.user.User;
import epusp.pcs.os.shared.model.person.user.admin.Admin;
import epusp.pcs.os.shared.model.person.user.admin.AdminCustomProperties;
import epusp.pcs.os.shared.model.person.user.monitor.Monitor;
import epusp.pcs.os.shared.model.person.user.monitor.MonitorCustomAttributes;
import epusp.pcs.os.shared.model.person.user.superuser.SuperUser;
import epusp.pcs.os.shared.model.person.user.superuser.SuperUserCustomAttributes;
import epusp.pcs.os.superuser.client.SuperUserResources;
import epusp.pcs.os.superuser.client.constants.SuperUserWorkspaceConstants;
import epusp.pcs.os.superuser.client.rpc.ISuperUserWorkspaceServiceAsync;

public class WorkspacePresenter implements Presenter, ClosePopupHandler{

	private ISuperUserWorkspaceServiceAsync rpcService;
	private Display display;
	private SuperUserWorkspaceConstants constants;

	private SuperUserResources resources = SuperUserResources.INSTANCE;

	private WorkspaceStyles backgroundResources = WorkspaceStyles.INSTANCE;

	private final int pageSize = 2;
	private final PopupPanel popup = new PopupPanel(false, false);

	private final HeaderButton adminHeaderButton, superHeaderButton, monitorHeaderButton;
	
	private final HashMap<String, AttributeInfo> attributesInfo = new HashMap<String, AttributeInfo>();
	
	private final AttributeInfoLoader loader;

	public WorkspacePresenter(ISuperUserWorkspaceServiceAsync rpcService, Display view, SuperUserWorkspaceConstants constants){
		this.rpcService = rpcService;
		this.display = view;
		this.constants = constants;
		
		popup.setGlassEnabled(true);
		popup.setStyleName("preferencesPopupPanel");
		popup.setGlassStyleName("preferencesPopupGlassPanel");
		
		popup.setSize("1800px", "900px");

		adminHeaderButton = new HeaderButton(constants.admin(), resources.newAdmin().getSafeUri());
		superHeaderButton = new HeaderButton(constants.superUser(), resources.newSuperUser().getSafeUri());
		monitorHeaderButton = new HeaderButton(constants.monitor(), resources.newMonitor().getSafeUri());
		
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

		display.setBackgroundStyleName(backgroundResources.backgroundStyles().superuserBackground());

		HasWidgets monitorContainer = display.addType(Monitor.class.getName(), monitorHeaderButton);
		HasWidgets adminContainer = display.addType(Admin.class.getName(), adminHeaderButton);
		HasWidgets superUserContainer = display.addType(SuperUser.class.getName(), superHeaderButton);

		UserTablePresenter monitorTablePresenter = new MonitorTablePresenter(rpcService, constants, resources, pageSize, new SelectedRowHandler<Monitor>() {
			@Override
			public void onSelectedRow(final Monitor objectSelected) {
				loader.loadCustomAttributes(MonitorCustomAttributes.values(), new IAttributeInfoLoaded() {
					@Override
					public void onCustomAttributesLoaded() {
						rpcService.getFullMonitor(objectSelected.getId(), new AsyncCallback<Monitor>() {
							
							@Override
							public void onSuccess(Monitor result) {
								List<AttributeInfo> attributes = new ArrayList<AttributeInfo>();
								for(ICustomAttributes attribute : MonitorCustomAttributes.values()){
									attributes.add(attributesInfo.get(attribute.getAttributeName()));
								}
								CreateUpdatePresenter createUpdatePresenter = new UpdateMonitorPresenter(rpcService, new CreateUpdate(), constants, attributes, result);
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
		
		monitorTablePresenter.go(monitorContainer);

		UserTablePresenter adminTablePresenter = new AdminTablePresenter(rpcService, constants, resources, pageSize, new SelectedRowHandler<Admin>() {
			@Override
			public void onSelectedRow(final Admin objectSelected) {
				loader.loadCustomAttributes(AdminCustomProperties.values(), new IAttributeInfoLoaded() {
					@Override
					public void onCustomAttributesLoaded() {
						rpcService.getFullAdmin(objectSelected.getId(), new AsyncCallback<Admin>() {
							
							@Override
							public void onSuccess(Admin result) {
								List<AttributeInfo> attributes = new ArrayList<AttributeInfo>();
								for(ICustomAttributes attribute : AdminCustomProperties.values()){
									attributes.add(attributesInfo.get(attribute.getAttributeName()));
								}
								CreateUpdatePresenter createUpdatePresenter = new UpdateAdminPresenter(rpcService, new CreateUpdate(), constants, attributes, result);
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
		
		adminTablePresenter.go(adminContainer);

		UserTablePresenter superUserTablePresenter = new SuperUserTablePresenter(rpcService, constants, resources, pageSize, new SelectedRowHandler<SuperUser>() {
			@Override
			public void onSelectedRow(final SuperUser objectSelected) {
				loader.loadCustomAttributes(SuperUserCustomAttributes.values(), new IAttributeInfoLoaded() {
					@Override
					public void onCustomAttributesLoaded() {
						rpcService.getFullSuperUser(objectSelected.getId(), new AsyncCallback<SuperUser>() {
							
							@Override
							public void onSuccess(SuperUser result) {
								List<AttributeInfo> attributes = new ArrayList<AttributeInfo>();
								for(ICustomAttributes attribute : SuperUserCustomAttributes.values()){
									attributes.add(attributesInfo.get(attribute.getAttributeName()));
								}
								CreateUpdatePresenter createUpdatePresenter = new UpdateSuperUserPresenter(rpcService, new CreateUpdate(), constants, attributes, result);
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
		
		superUserTablePresenter.go(superUserContainer);

		monitorHeaderButton.enable();
		adminHeaderButton.disable();
		superHeaderButton.disable();
		bind();
	}

	private void bind(){
		display.getLogout().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				rpcService.logout(new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						String query = Window.Location.getQueryString();
						query = query.replaceAll("&locale=[^&]+", "");
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

		adminHeaderButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(adminHeaderButton.isEnabled()){
					loader.loadCustomAttributes(AdminCustomProperties.values(), new IAttributeInfoLoaded() {
						@Override
						public void onCustomAttributesLoaded() {
							List<AttributeInfo> attributes = new ArrayList<AttributeInfo>();
							for(ICustomAttributes attribute : AdminCustomProperties.values()){
								attributes.add(attributesInfo.get(attribute.getAttributeName()));
							}
							CreateUpdatePresenter createUpdatePresenter = new CreateAdminPresenter(rpcService, new CreateUpdate(), constants, attributes);
							createUpdatePresenter.go(popup);
							popup.center();
						}
					});
				}
			}
		});

		superHeaderButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(superHeaderButton.isEnabled()){
					loader.loadCustomAttributes(SuperUserCustomAttributes.values(), new IAttributeInfoLoaded() {
						@Override
						public void onCustomAttributesLoaded() {
							List<AttributeInfo> attributes = new ArrayList<AttributeInfo>();
							for(ICustomAttributes attribute : SuperUserCustomAttributes.values()){
								attributes.add(attributesInfo.get(attribute.getAttributeName()));
							}
							CreateUpdatePresenter createUpdatePresenter = new CreateSuperUserPresenter(rpcService, new CreateUpdate(), constants, attributes);
							createUpdatePresenter.go(popup);
							popup.center();
						}
					});
				}
			}
		});

		monitorHeaderButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(monitorHeaderButton.isEnabled()){
					loader.loadCustomAttributes(MonitorCustomAttributes.values(), new IAttributeInfoLoaded() {
						@Override
						public void onCustomAttributesLoaded() {
							List<AttributeInfo> attributes = new ArrayList<AttributeInfo>();
							for(ICustomAttributes attribute : MonitorCustomAttributes.values()){
								attributes.add(attributesInfo.get(attribute.getAttributeName()));
							}
							CreateUpdatePresenter createUpdatePresenter = new CreateMonitorPresenter(rpcService, new CreateUpdate(), constants, attributes);
							createUpdatePresenter.go(popup);
							popup.center();
						}
					});
				}
			}
		});

		display.addSelectionHandler(new SelectionHandler<Integer>() {

			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				Integer i = event.getSelectedItem();

				adminHeaderButton.disable();
				superHeaderButton.disable();
				monitorHeaderButton.disable();

				if(i == 0)
					monitorHeaderButton.enable();
				else if(i == 1)
					adminHeaderButton.enable();
				else if(i == 2)
					superHeaderButton.enable();
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
