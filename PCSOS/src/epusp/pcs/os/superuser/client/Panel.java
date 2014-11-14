package epusp.pcs.os.superuser.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;

import epusp.pcs.os.shared.exception.LoginException;
import epusp.pcs.os.superuser.client.constants.SuperUserWorkspaceConstants;
import epusp.pcs.os.superuser.client.rpc.ISuperUserWorkspaceService;
import epusp.pcs.os.superuser.client.rpc.ISuperUserWorkspaceServiceAsync;

public class Panel implements EntryPoint {

	@Override
	public void onModuleLoad() {
		SuperUserWorkspaceConstants constants = GWT.create(SuperUserWorkspaceConstants.class);
		ISuperUserWorkspaceServiceAsync superuserService = GWT.create(ISuperUserWorkspaceService.class);
		final WorkspaceController workspaceController = new WorkspaceController(superuserService, constants);
		
		superuserService.identifySession(Cookies.getCookie("pcs.os-login"), new AsyncCallback<Void>() {
			
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
