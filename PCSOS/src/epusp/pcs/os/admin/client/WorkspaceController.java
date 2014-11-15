package epusp.pcs.os.admin.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.PopupPanel;

import epusp.pcs.os.admin.client.constants.AdminWorkspaceConstants;
import epusp.pcs.os.admin.client.presenter.WorkspacePresenter;
import epusp.pcs.os.admin.client.rpc.IAdminWorkspaceServiceAsync;
import epusp.pcs.os.shared.client.presenter.PreferencesPresenter;
import epusp.pcs.os.shared.client.presenter.Presenter;
import epusp.pcs.os.shared.client.view.Preferences;
import epusp.pcs.os.shared.client.view.Workspace;

public class WorkspaceController implements Presenter{

	private final IAdminWorkspaceServiceAsync rpcService;
	private final AdminWorkspaceConstants constants;
	
	private PopupPanel preferencesPopup = new PopupPanel(true);
	
	private PreferencesPresenter preferencesPresenter;
	private WorkspacePresenter workspacePresenter;
	
	public WorkspaceController(IAdminWorkspaceServiceAsync rpcService, AdminWorkspaceConstants constants){
		this.rpcService = rpcService;
		this.constants = constants;
		
		preferencesPopup.setGlassEnabled(true);
		preferencesPopup.setStyleName("preferencesPopupPanel");
		preferencesPopup.setGlassStyleName("preferencesPopupGlassPanel");
		
		Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				if(preferencesPopup.isShowing())
					preferencesPopup.center();
			}
		});
	}
	
	@Override
	public void go(HasWidgets container) {
		container.clear();
		workspacePresenter = new WorkspacePresenter(rpcService, new Workspace(), constants);
		workspacePresenter.go(container);
		preferencesPresenter = new PreferencesPresenter(rpcService, new Preferences(), constants);
		preferencesPresenter.go(preferencesPopup);
		bind();
	}
	
	private void bind(){
		workspacePresenter.getPreferencesButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				preferencesPopup.center();
			}
		});

		preferencesPresenter.getCancelButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				preferencesPopup.hide();
			}
		});
	}

}
