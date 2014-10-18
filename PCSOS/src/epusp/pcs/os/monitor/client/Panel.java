package epusp.pcs.os.monitor.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;

import epusp.pcs.os.monitor.client.constants.MonitorWorkspaceConstants;
import epusp.pcs.os.monitor.client.rpc.IMonitorWorkspaceService;
import epusp.pcs.os.monitor.client.rpc.IMonitorWorkspaceServiceAsync;

public class Panel implements EntryPoint {

	@Override
	public void onModuleLoad() {
		MonitorWorkspaceConstants constants = GWT.create(MonitorWorkspaceConstants.class);
		IMonitorWorkspaceServiceAsync monitorService = GWT.create(IMonitorWorkspaceService.class);
		final WorkspaceController workspaceController = new WorkspaceController(monitorService, constants);
		
		monitorService.identifySession(Cookies.getCookie("pcs.os-login"), new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				workspaceController.go(RootLayoutPanel.get());
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}

}
