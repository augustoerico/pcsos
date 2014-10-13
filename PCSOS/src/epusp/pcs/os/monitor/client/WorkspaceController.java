package epusp.pcs.os.monitor.client;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import epusp.pcs.os.monitor.client.constants.MonitorWorkspaceContants;
import epusp.pcs.os.monitor.client.presenter.GoogleMapsPresenter;
import epusp.pcs.os.monitor.client.presenter.WorkspacePresenter;
import epusp.pcs.os.monitor.client.rpc.IMonitorWorkspaceServiceAsync;
import epusp.pcs.os.monitor.client.view.Workspace;
import epusp.pcs.os.shared.client.presenter.Presenter;

public class WorkspaceController implements Presenter {
	
	private IMonitorWorkspaceServiceAsync monitorService;
	private HandlerManager eventBus;
	private MonitorWorkspaceContants constants;
	private HasWidgets container;
	
	public interface WorkspaceLayoutPanel extends Presenter {
		HasWidgets getMapsArea();
	}
	
	public WorkspaceController(IMonitorWorkspaceServiceAsync monitorService, HandlerManager eventBus, MonitorWorkspaceContants constants){
		this.monitorService = monitorService;
		this.eventBus = eventBus;
		this.constants = constants;
	}
	
	private void bind(){
		
	}

	@Override
	public void go(final HasWidgets container) {		
		this.container = container;
		WorkspaceLayoutPanel workspacePresenter = new WorkspacePresenter(monitorService, eventBus, new Workspace());
		workspacePresenter.go(container);
		Presenter googleMapsPresenter = new GoogleMapsPresenter(monitorService, eventBus);
		googleMapsPresenter.go(workspacePresenter.getMapsArea());
		bind();
	}

}
