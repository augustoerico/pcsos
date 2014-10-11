package epusp.pcs.os.monitor.client.presenter;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import epusp.pcs.os.monitor.client.rpc.IMonitorWorkspaceServiceAsync;
import epusp.pcs.os.shared.client.presenter.Presenter;

public class WorkspacePresenter implements Presenter{

	public interface Display {
		Widget asWidget();
	}

	private final IMonitorWorkspaceServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final Display display;

	public WorkspacePresenter(IMonitorWorkspaceServiceAsync rpcService, HandlerManager eventBus, Display view) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.display = view;
	}

	@Override
	public void go(HasWidgets container) {
	    bind();
	    container.clear();
	    container.add(display.asWidget());
	}

	public void bind() {

	}

}
