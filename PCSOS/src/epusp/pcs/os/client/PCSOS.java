package epusp.pcs.os.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.ui.RootLayoutPanel;

import epusp.pcs.os.client.rpc.IConnectionService;
import epusp.pcs.os.client.rpc.IConnectionServiceAsync;

public class PCSOS implements EntryPoint {

	IConnectionServiceAsync connection = (IConnectionServiceAsync) GWT.create(IConnectionService.class);

	private static final String APIKey = "AIzaSyDNVvFn0tU2jIjWsOj4oiGaWACX6BgF7Eo";
	
	//private User user;
	
	public void onModuleLoad() {

//		connection.Hello(new AsyncCallback<User>() {
//
//			public void onSuccess(User result) {
//				user = result;
//
//			}
//
//			public void onFailure(Throwable caught) {
//				RootLayoutPanel.get().add(new Label("I couldn't connect... ;("));
//			}
//		});

		Maps.loadMapsApi(APIKey, "2", false, new Runnable() {
			public void run() {
				buildUi();
			}
		});
	}

	private void buildUi() {
		//Centering map at my home!
		LatLng home = LatLng.newInstance(-23.4094674, -46.623421);

		final MapWidget map = new MapWidget(home, 17);
		map.setSize("100%", "100%");
		// Add some controls for the zoom level
		map.addControl(new LargeMapControl());

		// Add a marker
		map.addOverlay(new Marker(home));

		// Add an info window to highlight a point of interest
//		map.getInfoWindow().open(map.getCenter(),
//				new InfoWindowContent("Hello, " + user.getName()));

		// Add the map to the HTML host page
		RootLayoutPanel.get().add(map);
	}

}
