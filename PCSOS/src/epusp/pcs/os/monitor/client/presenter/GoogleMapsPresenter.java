package epusp.pcs.os.monitor.client.presenter;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl3D;
import com.google.gwt.maps.client.geocode.DirectionQueryOptions;
import com.google.gwt.maps.client.geocode.DirectionQueryOptions.TravelMode;
import com.google.gwt.maps.client.geocode.DirectionResults;
import com.google.gwt.maps.client.geocode.Directions;
import com.google.gwt.maps.client.geocode.DirectionsCallback;
import com.google.gwt.maps.client.geocode.Placemark;
import com.google.gwt.maps.client.geocode.Route;
import com.google.gwt.maps.client.geocode.StatusCodes;
import com.google.gwt.maps.client.geocode.Waypoint;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.PolyStyleOptions;
import com.google.gwt.maps.client.overlay.Polygon;
import com.google.gwt.maps.client.overlay.Polyline;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;

import epusp.pcs.os.model.oncall.Position;
import epusp.pcs.os.monitor.client.constants.MonitorWorkspaceConstants;
import epusp.pcs.os.monitor.client.rpc.IMonitorWorkspaceServiceAsync;
import epusp.pcs.os.shared.client.presenter.Presenter;

public class GoogleMapsPresenter implements Presenter {

	private static final String mapsAPIKey = "AIzaSyDNVvFn0tU2jIjWsOj4oiGaWACX6BgF7Eo";
	private static final String mapsVersion = "2";
	private MapWidget view;

	private String color = "#FF0000";
	private double opacity = 1.0;
	private int weight = 1;
	private boolean fillFlag = false;
	private Polygon poly;
	
	private DirectionQueryOptions options;
	
	private IMonitorWorkspaceServiceAsync monitorService; 
	private HandlerManager eventBus;

	private HasWidgets container;

	private MonitorWorkspaceConstants constants;

	private List<Position> victimPositions = new ArrayList<Position>();
	private Marker victim;

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
		LatLng ATLANTA = LatLng.newInstance(33.7814790,
			      -84.3880580);
		addVictimCall(new Position(ATLANTA.getLatitude(), ATLANTA.getLongitude()));
	}

	private void buildUi(){
		LatLng brazil = LatLng.newInstance(-14.2400732, -53.1805017);
		view = new MapWidget(brazil, 4);
		view.setSize("100%", "100%");
		view.addControl(new LargeMapControl3D());
		view.addStyleName("maps");
		options = new DirectionQueryOptions();
		options.setAvoidHighways(false);
		options.setPreserveViewport(false);
		options.setRetrieveSteps(false);
		options.setTravelMode(TravelMode.DRIVING);
	}

	public void addVictimCall(Position begin){
		victimPositions.add(begin);
		LatLng latLng = LatLng.newInstance(begin.getLatitude(), begin.getLongitude());
		victim = new Marker(latLng);
		victim.setDraggingEnabled(false);
		view.addOverlay(victim);
		view.setCenter(latLng, 15);
		
		
			  // Points of interest along the map.
		LatLng STONE_MOUNTAIN_PARK = LatLng.newInstance(
			      33.80653802509606, -84.15252685546875);
			  // Cyclorama
		LatLng CYCLORAMA = LatLng.newInstance(
			      33.741185330333956, -84.35834884643555);
			  // Georgia Aquarium
	     LatLng GEORGIA_AQUARIUM = LatLng.newInstance(
			      33.761443931868925, -84.39432263374329);
			  // Underground Atlanta
	     LatLng UNDERGROUND_ATLANTA = LatLng.newInstance(
			      33.75134645137294, -84.39026713371277);
			  
	     String DWARF_HOUSE = "461 N Central Ave Hapeville, GA";
		
		Waypoint waypoints[] = {
			      new Waypoint("10 10th St NE, Atlanta, GA 30309"), // Midtown Atlanta
			      new Waypoint(GEORGIA_AQUARIUM), //
			      new Waypoint(UNDERGROUND_ATLANTA)
			  };
		
		Directions.loadFromWaypoints(waypoints, options, new DirectionsCallback() {

			public void onFailure(int statusCode) {
				Window.alert("Failed to load directions: Status "
						+ StatusCodes.getName(statusCode) + " " + statusCode);
			}

			public void onSuccess(DirectionResults result) {
				GWT.log("Successfully loaded directions.", null);

				// A little exercise of the route API
				Polyline polyline = result.getPolyline();
				view.addOverlay(polyline);
			}
		});
		
		Waypoint waypoints2[] = {
		 new Waypoint(CYCLORAMA), //
	      new Waypoint(STONE_MOUNTAIN_PARK), //
	      new Waypoint(DWARF_HOUSE), //
	      new Waypoint("N Terminal Pkwy, Atlanta, GA 30320") // The Airport
		  };
		
		Directions.loadFromWaypoints(waypoints2, options, new DirectionsCallback() {

			public void onFailure(int statusCode) {
				Window.alert("Failed to load directions: Status "
						+ StatusCodes.getName(statusCode) + " " + statusCode);
			}

			public void onSuccess(DirectionResults result) {
				GWT.log("Successfully loaded directions.", null);

				// A little exercise of the route API
				Polyline polyline = result.getPolyline();
				view.addOverlay(polyline);
			}
		});
		
		
//		PolyStyleOptions style = PolyStyleOptions.newInstance(color, weight, opacity);
//		
//		LatLng[] latLngV = new LatLng[] {LatLng.newInstance(begin.getLatitude(), begin.getLongitude()), LatLng.newInstance(-23.5683157, -46.7093514)};
//		
//		poly = new Polygon(latLngV, color, weight, opacity, color, fillFlag ? .7 : 0.0);
//		view.addOverlay(poly);
//		poly.insertVertex(0, latLng);
	}

	public void updateVictimPosition(List<Position> p){
		if(victimPositions.size() < p.size()){
			int j = victimPositions.size()-1;
			for(int i = victimPositions.size()-1; i < p.size(); i++){
				victimPositions.add(p.get(i));
			}

			for(; j < p.size(); j++){
				Position position = p.get(j);
				poly.insertVertex(j, LatLng.newInstance(position.getLatitude(), position.getLongitude()));
			}

		}
	}

}
