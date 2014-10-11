package epusp.pcs.os.monitor.client;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;

import epusp.pcs.os.server.monitor.MonitorWorkspaceService;
import epusp.pcs.os.shared.client.presenter.Presenter;

public class WorkspaceController implements Presenter{

	private MonitorWorkspaceService rpcService;
	private HandlerManager eventBus;
	private HasWidgets container;

	public WorkspaceController(MonitorWorkspaceService rpcService, HandlerManager eventBus) {
		this.eventBus = eventBus;
		this.rpcService = rpcService;
		bind();
	}

	@Override
	public void go(HasWidgets container) {
		this.container = container;
	}

	private void bind() {

	}

}
