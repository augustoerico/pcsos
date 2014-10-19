package epusp.pcs.os.monitor.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;

import epusp.pcs.os.monitor.client.constants.MonitorWorkspaceConstants;
import epusp.pcs.os.monitor.client.presenter.GoogleMapsPresenter;
import epusp.pcs.os.monitor.client.presenter.WorkspacePresenter;
import epusp.pcs.os.monitor.client.rpc.IMonitorWorkspaceServiceAsync;
import epusp.pcs.os.monitor.client.view.Workspace;
import epusp.pcs.os.shared.client.presenter.PreferencesPresenter;
import epusp.pcs.os.shared.client.presenter.Presenter;
import epusp.pcs.os.shared.client.view.Preferences;

public class WorkspaceController implements Presenter {
	
	private IMonitorWorkspaceServiceAsync monitorService;
	private MonitorWorkspaceConstants constants;
	private HasWidgets container;
	
	private WorkspaceLayoutPanel workspacePresenter;
	private GoogleMapsPresenter googleMapsPresenter;
	private PreferencesPresenter preferencesPresenter;
	
	PopupPanel preferencesPopup = new PopupPanel(true);
	
	public interface WorkspaceLayoutPanel extends Presenter {
		HasWidgets getMapsArea();
		Image getPreferencesButton();
	}
	
	public WorkspaceController(IMonitorWorkspaceServiceAsync monitorService, MonitorWorkspaceConstants constants){
		this.monitorService = monitorService;
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

	@Override
	public void go(final HasWidgets container) {		
		this.container = container;
		workspacePresenter = new WorkspacePresenter(monitorService, new Workspace(), constants);
		workspacePresenter.go(container);
		googleMapsPresenter = new GoogleMapsPresenter(monitorService, constants);
		googleMapsPresenter.go(workspacePresenter.getMapsArea());
		preferencesPresenter = new PreferencesPresenter(monitorService, new Preferences(), constants);
		preferencesPresenter.go(preferencesPopup);
		bind();
	}

}
