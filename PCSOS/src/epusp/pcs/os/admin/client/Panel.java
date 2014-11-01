package epusp.pcs.os.admin.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;

import epusp.pcs.os.admin.client.constants.AdminWorkspaceConstants;
import epusp.pcs.os.admin.client.rpc.IAdminWorkspaceService;
import epusp.pcs.os.admin.client.rpc.IAdminWorkspaceServiceAsync;
import epusp.pcs.os.shared.exception.LoginException;

public class Panel implements EntryPoint {

	@Override
	public void onModuleLoad() {
		AdminWorkspaceConstants constants = GWT.create(AdminWorkspaceConstants.class);
		IAdminWorkspaceServiceAsync adminService = GWT.create(IAdminWorkspaceService.class);
		final WorkspaceController workspaceController = new WorkspaceController(adminService, constants);
		
		adminService.identifySession(Cookies.getCookie("pcs.os-login"), new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				workspaceController.go(RootLayoutPanel.get());
			}
			
			@Override
			public void onFailure(Throwable caught) {
				if(caught instanceof LoginException){
					String path = Window.Location.getProtocol().concat("//").concat(Window.Location.getHost()).concat("/")
							.concat("PCSOS.html").concat(Window.Location.getQueryString());
					Window.Location.replace(path);
				}
			}
		});
	}

}
