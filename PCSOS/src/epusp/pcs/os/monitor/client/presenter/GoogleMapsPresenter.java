package epusp.pcs.os.monitor.client.presenter;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl3D;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.user.client.ui.HasWidgets;

import epusp.pcs.os.monitor.client.constants.MonitorWorkspaceConstants;
import epusp.pcs.os.monitor.client.rpc.IMonitorWorkspaceServiceAsync;
import epusp.pcs.os.shared.client.presenter.Presenter;

public class GoogleMapsPresenter implements Presenter {

	private static final String mapsAPIKey = "AIzaSyDNVvFn0tU2jIjWsOj4oiGaWACX6BgF7Eo";
	private static final String mapsVersion = "2";
	private MapWidget view;
	
	private IMonitorWorkspaceServiceAsync monitorService; 
	private HandlerManager eventBus;
	
	private HasWidgets container;
	
	private MonitorWorkspaceConstants constants;
	
	public GoogleMapsPresenter(IMonitorWorkspaceServiceAsync monitorService, HandlerManager eventBus, MonitorWorkspaceConstants constants){
		this.monitorService = monitorService;
		this.eventBus = eventBus;
		this.constants = constants;
	}
	
	@Override
	public void go(final HasWidgets container) {
		this.container = container;
		container.clear();
		Maps.loadMapsApi(mapsAPIKey, mapsVersion, false, new Runnable() {
			@Override
			public void run() {
				buildUi();
				container.add(view);
				bind();
			}
		});
	}
	
	private void bind(){
		
	}
	
	private void buildUi(){
	    LatLng brazil = LatLng.newInstance(-14.2400732, -53.1805017);
	    view = new MapWidget(brazil, 4);
	    view.setSize("100%", "100%");
	    view.addControl(new LargeMapControl3D());
	}

}
