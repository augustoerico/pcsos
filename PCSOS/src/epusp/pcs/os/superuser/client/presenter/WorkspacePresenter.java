package epusp.pcs.os.superuser.client.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

import epusp.pcs.os.shared.client.WorkspaceStyles;
import epusp.pcs.os.shared.client.event.ClosePopupEvent;
import epusp.pcs.os.shared.client.event.ClosePopupEvent.ClosePopupHandler;
import epusp.pcs.os.shared.client.event.EventBus;
import epusp.pcs.os.shared.client.presenter.Presenter;
import epusp.pcs.os.shared.general.Display;
import epusp.pcs.os.shared.model.person.user.User;
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
	
	public WorkspacePresenter(ISuperUserWorkspaceServiceAsync rpcService, Display view, SuperUserWorkspaceConstants constants){
		this.rpcService = rpcService;
		this.display = view;
		this.constants = constants;

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
		
//		ImageTabPresenter imageTabPresenter = new ImageTabPresenter(new ImageTabPanel());
//		imageTabPresenter.go(display.getWorkArea());
		
		AbsolutePanel adminPanel = new AbsolutePanel();
		adminPanel.setSize("100%", "100%");
		UserTablePresenter adminTablePresenter = new AdminTablePresenter(rpcService, constants, pageSize);
		adminTablePresenter.go(adminPanel);
		
		display.addType("ADMIN", new Label("Admin"));
		display.addType("MONITOR", new Label("Monitor"));
		display.addType("SUPERUSER", new Label("SuperUser"));

		
//		imageTabPresenter.addInfo(constants.admin(), resources.admin().getSafeUri().asString(), adminPanel);
//		imageTabPresenter.addInfo(constants.monitor(), resources.monitor().getSafeUri().asString(), new Label("Monitor"));
//		imageTabPresenter.addInfo(constants.superUser(), resources.superuser().getSafeUri().asString(), new Label("SuperUser"));
		
		bind();
	}

	private void bind(){
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

	@Override
	public void onClosingPopup(ClosePopupEvent closePopupEvent) {
		popup.clear();
		popup.hide();
	}

}
