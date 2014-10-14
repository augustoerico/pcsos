package epusp.pcs.os.monitor.client.presenter;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
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
		Maps.loadMapsApi(mapsAPIKey, mapsVersion, false, new Runnable() {
			@Override
			public void run() {
				buildUi();
			}
		});
	}
	
	@Override
	public void go(final HasWidgets container) {
		this.container = container;
		container.clear();
		bind();
	}
	
	private void bind(){
		
	}
	
	private void buildUi(){
		 // Open a map centered on Cawker City, KS USA
	    LatLng cawkerCity = LatLng.newInstance(39.509, -98.434);

	    view = new MapWidget(cawkerCity, 2);
	    container.add(view);
	    view.setSize("100%", "100%");
	    // Add some controls for the zoom level
	    view.addControl(new LargeMapControl());

	    // Add a marker
	    view.addOverlay(new Marker(cawkerCity));

	    // Add an info window to highlight a point of interest
	    view.getInfoWindow().open(view.getCenter(),
	        new InfoWindowContent("World's Largest Ball of Sisal Twine"));
	}

}
