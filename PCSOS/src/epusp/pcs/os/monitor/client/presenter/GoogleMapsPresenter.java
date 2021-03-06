package epusp.pcs.os.monitor.client.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl3D;
import com.google.gwt.maps.client.event.PolylineMouseOutHandler;
import com.google.gwt.maps.client.event.PolylineMouseOverHandler;
import com.google.gwt.maps.client.geocode.DirectionQueryOptions;
import com.google.gwt.maps.client.geocode.DirectionQueryOptions.TravelMode;
import com.google.gwt.maps.client.geocode.DirectionResults;
import com.google.gwt.maps.client.geocode.Directions;
import com.google.gwt.maps.client.geocode.DirectionsCallback;
import com.google.gwt.maps.client.geocode.StatusCodes;
import com.google.gwt.maps.client.geocode.Waypoint;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.geom.Point;
import com.google.gwt.maps.client.geom.Size;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.maps.client.overlay.PolyStyleOptions;
import com.google.gwt.maps.client.overlay.Polyline;
import com.google.gwt.maps.client.overlay.TrafficOverlay;
import com.google.gwt.user.client.ui.HasWidgets;

import epusp.pcs.os.monitor.client.MonitorResources;
import epusp.pcs.os.monitor.client.constants.MonitorWorkspaceConstants;
import epusp.pcs.os.monitor.client.event.FinishCallEvent;
import epusp.pcs.os.monitor.client.event.FinishCallEvent.FinishCallHandler;
import epusp.pcs.os.monitor.client.event.HideShowTrafficEvent;
import epusp.pcs.os.monitor.client.event.HideShowTrafficEvent.HideShowTrafficHandler;
import epusp.pcs.os.shared.client.event.EventBus;
import epusp.pcs.os.shared.client.presenter.Presenter;
import epusp.pcs.os.shared.model.oncall.Position;
import epusp.pcs.os.shared.model.vehicle.Vehicle;

public class GoogleMapsPresenter implements Presenter, FinishCallHandler {

	private static final String mapsAPIKey = "AIzaSyDNVvFn0tU2jIjWsOj4oiGaWACX6BgF7Eo";
	private static final String mapsVersion = "2";
	private MapWidget view;

	private DirectionQueryOptions options;
	
	private MonitorWorkspaceConstants constants;
	
	private HasWidgets container;

	private Marker victim;
	private List<Polyline> victimRoute = new ArrayList<Polyline>();
	
	private HashMap<String, Marker> vehiclesMarker =  new HashMap<String, Marker>(); 
	private HashMap<String, Vehicle> vehicles = new HashMap<String, Vehicle>();
	private HashMap<String, List<Polyline>> vehicleRoute = new HashMap<String, List<Polyline>>();
	
	private TrafficOverlay trafficInfo;
	
	private MonitorResources resources = MonitorResources.INSTANCE;
	
	public GoogleMapsPresenter(MonitorWorkspaceConstants constants){
		this.constants = constants;
		
		EventBus.get().addHandler(FinishCallEvent.TYPE, this);
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
		
		EventBus.get().addHandler(HideShowTrafficEvent.TYPE, new HideShowTrafficHandler() {
			
			@Override
			public void onHideShowRequest(HideShowTrafficEvent hideShowTrafficEvent) {
				if(hideShowTrafficEvent.hide()){
					view.addOverlay(trafficInfo);
				}else{
					view.removeOverlay(trafficInfo);
				}
			}
		});
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
		
		trafficInfo = new TrafficOverlay();
	}

	public void addVictim(Position begin){
		Icon icon = Icon.newInstance(resources.victimMarker().getSafeUri().asString());
		icon.setIconSize(Size.newInstance(60, 60));
		icon.setIconAnchor(Point.newInstance(6, 20));
		icon.setInfoWindowAnchor(Point.newInstance(5, 1));
		MarkerOptions options = MarkerOptions.newInstance();
		options.setIcon(icon);
		options.setTitle(constants.victim());
		
		LatLng latLng = LatLng.newInstance(begin.getLatitude(), begin.getLongitude());
		victim = new Marker(latLng, options);
		victim.setDraggingEnabled(false);
		
		view.addOverlay(victim);
//		view.setCenter(latLng, 15);
		resetBounds();
	}

	public void updateVictimPosition(List<Position> positions){
		if(!positions.isEmpty()){
			Waypoint waypoints[] = new Waypoint[positions.size()+1];

			waypoints[0] = new Waypoint(victim.getLatLng());
			int j = 1;
			for(Position position : positions)
				waypoints[j++] = new Waypoint(LatLng.newInstance(position.getLatitude(), position.getLongitude()));

			victim.setLatLng(LatLng.newInstance(positions.get(positions.size()-1).getLatitude(), positions.get(positions.size()-1).getLongitude()));

			Directions.loadFromWaypoints(waypoints, options, new DirectionsCallback() {

				public void onFailure(int statusCode) {
					System.out.println("Failed to load directions: Status "
							+ StatusCodes.getName(statusCode) + " " + statusCode);
				}

				public void onSuccess(DirectionResults result) {
					final Polyline polyline = result.getPolyline();
					PolyStyleOptions style = PolyStyleOptions.newInstance("#480000", 4, 0.7);
					polyline.setStrokeStyle(style);
					polyline.addPolylineMouseOverHandler(new PolylineMouseOverHandler() {
						@Override
						public void onMouseOver(PolylineMouseOverEvent event) {
							PolyStyleOptions style = PolyStyleOptions.newInstance("#480000", 6, 1.0);
							for(Polyline polyline : victimRoute){
								polyline.setStrokeStyle(style);
							}
						}
					});
					polyline.addPolylineMouseOutHandler(new PolylineMouseOutHandler() {

						@Override
						public void onMouseOut(PolylineMouseOutEvent event) {
							PolyStyleOptions style = PolyStyleOptions.newInstance("#480000", 4, 0.7);
							for(Polyline polyline : victimRoute){
								polyline.setStrokeStyle(style);
							}
						}
					});
					victimRoute.add(polyline);
					view.addOverlay(polyline);
				}
			});
		}
	}

	public void addVehicle(Vehicle vehicle, Position begin){
		if(!vehicles.containsKey(vehicle.getIdTag())){
			vehicles.put(vehicle.getIdTag(), vehicle);

			Icon icon = null;
			MarkerOptions options = MarkerOptions.newInstance();
			
			switch (vehicle.getType()) {
			case Car:
				icon = Icon.newInstance(resources.carMarker().getSafeUri().asString());
				options.setTitle(constants.car());
				break;
			case Helicopter:
				icon = Icon.newInstance(resources.helicopterMarker().getSafeUri().asString());
				options.setTitle(constants.helicopter());
				break;
			case Motorcycle:
				icon = Icon.newInstance(resources.motorcycleMarker().getSafeUri().asString());
				options.setTitle(constants.motorcycle());
				break;
			default:
				break;
			}
			
			icon.setIconSize(Size.newInstance(25, 25));
			options.setIcon(icon);
			
			LatLng latLng = LatLng.newInstance(begin.getLatitude(), begin.getLongitude());
			
			Marker vehicleMarker = new Marker(latLng, options);
			vehicleMarker.setDraggingEnabled(false);
			vehiclesMarker.put(vehicle.getIdTag(), vehicleMarker);
			
			view.addOverlay(vehicleMarker);
			resetBounds();
		}
	}
	
	public void resetBounds(){
		try{
			LatLngBounds bounds = LatLngBounds.newInstance();
			Iterator<Marker> markerItarator = vehiclesMarker.values().iterator();
			while(markerItarator.hasNext()){
				Marker marker = markerItarator.next();
				bounds.extend(marker.getLatLng());
			}
			bounds.extend(victim.getLatLng());
			int zoomLevel = view.getBoundsZoomLevel(bounds);
			LatLng center = bounds.getCenter();
			view.setCenter(center, zoomLevel);
		}catch(NoSuchElementException e){
			e.printStackTrace();
		}
	}
	
	public void updateVehiclePosition(final String idTag, List<Position> positions){
		if(!positions.isEmpty()){
			if(vehicles.containsKey(idTag)){
				Vehicle vehicle = vehicles.get(idTag);
				Marker vehicleMarker = vehiclesMarker.get(idTag);
				if(!vehicleRoute.containsKey(idTag)){
					vehicleRoute.put(idTag, new ArrayList<Polyline>());
				}

				switch (vehicle.getType()) {
				case Car:
					Waypoint waypoints[] = new Waypoint[positions.size()+1];

					waypoints[0] = new Waypoint(vehicleMarker.getLatLng());
					int j = 1;
					for(Position position : positions)
						waypoints[j++] = new Waypoint(LatLng.newInstance(position.getLatitude(), position.getLongitude()));

					Directions.loadFromWaypoints(waypoints, options, new DirectionsCallback() {

						public void onFailure(int statusCode) {
							System.out.println("Failed to load directions: Status "
									+ StatusCodes.getName(statusCode) + " " + statusCode);
						}

						public void onSuccess(DirectionResults result) {
							final List<Polyline> route = vehicleRoute.get(idTag);
							final Polyline polyline = result.getPolyline();
							PolyStyleOptions style = PolyStyleOptions.newInstance("#0000FF", 4, 0.6);
							polyline.setStrokeStyle(style);
							polyline.addPolylineMouseOverHandler(new PolylineMouseOverHandler() {
								@Override
								public void onMouseOver(PolylineMouseOverEvent event) {
									PolyStyleOptions style = PolyStyleOptions.newInstance("#0000FF", 6, 1.0);
									for(Polyline polyline : route){
										polyline.setStrokeStyle(style);
									}
								}
							});
							polyline.addPolylineMouseOutHandler(new PolylineMouseOutHandler() {

								@Override
								public void onMouseOut(PolylineMouseOutEvent event) {
									PolyStyleOptions style = PolyStyleOptions.newInstance("#0000FF", 4, 0.6);
									for(Polyline polyline : route){
										polyline.setStrokeStyle(style);
									}
								}
							});
							route.add(polyline);
							view.addOverlay(polyline);
						}
					});
					break;
				default:
					LatLng points[] = new LatLng[positions.size()+1];

					points[0] = vehicleMarker.getLatLng();
					j = 1;
					for(Position position : positions)
						points[j++] = LatLng.newInstance(position.getLatitude(), position.getLongitude());

					final List<Polyline> route = vehicleRoute.get(idTag);
					final Polyline polyline = new Polyline(points);
					PolyStyleOptions style = PolyStyleOptions.newInstance("#DAA520", 4, 0.7);
					polyline.setStrokeStyle(style);
					polyline.addPolylineMouseOverHandler(new PolylineMouseOverHandler() {
						@Override
						public void onMouseOver(PolylineMouseOverEvent event) {
							PolyStyleOptions style = PolyStyleOptions.newInstance("#DAA520", 6, 1.0);
							for(Polyline polyline : route){
								polyline.setStrokeStyle(style);
							}
						}
					});
					polyline.addPolylineMouseOutHandler(new PolylineMouseOutHandler() {

						@Override
						public void onMouseOut(PolylineMouseOutEvent event) {
							PolyStyleOptions style = PolyStyleOptions.newInstance("#DAA520", 4, 0.7);
							for(Polyline polyline : route){
								polyline.setStrokeStyle(style);
							}
						}
					});
					route.add(polyline);
					view.addOverlay(polyline);
					break;
				}

				vehicleMarker.setLatLng(LatLng.newInstance(positions.get(positions.size()-1).getLatitude(), positions.get(positions.size()-1).getLongitude()));
			}
		}
	}
	
	public Boolean hasVictim(){
		return victim != null;
	}
	
	public Boolean hasVehicle(String idTag){
		return vehicles.containsKey(idTag);
	}

	@Override
	public void onFinishCall(FinishCallEvent finishCallEvent) {
		this.go(container);
	}
}
