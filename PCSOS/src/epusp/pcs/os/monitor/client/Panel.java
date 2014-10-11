package epusp.pcs.os.monitor.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.RootLayoutPanel;

import epusp.pcs.os.monitor.client.constants.MonitorWorkspaceContants;
import epusp.pcs.os.monitor.client.presenter.WorkspacePresenter;
import epusp.pcs.os.monitor.client.rpc.IMonitorWorkspaceService;
import epusp.pcs.os.monitor.client.rpc.IMonitorWorkspaceServiceAsync;

public class Panel implements EntryPoint {

	@Override
	public void onModuleLoad() {
		MonitorWorkspaceContants constants = GWT.create(MonitorWorkspaceContants.class);
		IMonitorWorkspaceServiceAsync monitorService = GWT.create(IMonitorWorkspaceService.class);
		HandlerManager eventBus = new HandlerManager(null);
		WorkspaceController workspaceController = new WorkspaceController(monitorService, eventBus, constants);
		workspaceController.go(RootLayoutPanel.get());
	}

}
