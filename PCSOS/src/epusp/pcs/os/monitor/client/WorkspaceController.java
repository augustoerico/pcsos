package epusp.pcs.os.monitor.client;

import com.google.gwt.user.client.ui.HasWidgets;

import epusp.pcs.os.monitor.client.constants.MonitorWorkspaceConstants;
import epusp.pcs.os.monitor.client.presenter.GoogleMapsPresenter;
import epusp.pcs.os.monitor.client.presenter.WorkspacePresenter;
import epusp.pcs.os.monitor.client.rpc.IMonitorWorkspaceServiceAsync;
import epusp.pcs.os.monitor.client.view.Workspace;
import epusp.pcs.os.shared.client.presenter.Presenter;

public class WorkspaceController implements Presenter {
	
	private IMonitorWorkspaceServiceAsync monitorService;
	private MonitorWorkspaceConstants constants;
	private HasWidgets container;
	
	private WorkspaceLayoutPanel workspacePresenter;
	private GoogleMapsPresenter googleMapsPresenter;
	
	public interface WorkspaceLayoutPanel extends Presenter {
		HasWidgets getMapsArea();
	}
	
	public WorkspaceController(IMonitorWorkspaceServiceAsync monitorService, MonitorWorkspaceConstants constants){
		this.monitorService = monitorService;
		this.constants = constants;
	}
	
	private void bind(){
		
	}

	@Override
	public void go(final HasWidgets container) {		
		this.container = container;
		workspacePresenter = new WorkspacePresenter(monitorService, new Workspace(), constants);
		workspacePresenter.go(container);
		googleMapsPresenter = new GoogleMapsPresenter(monitorService, constants);
		googleMapsPresenter.go(workspacePresenter.getMapsArea());
		bind();
	}

}
