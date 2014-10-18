package epusp.pcs.os.monitor.client.presenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl3D;
import com.google.gwt.maps.client.event.PolylineMouseOutHandler;
import com.google.gwt.maps.client.event.PolylineMouseOverHandler;
import com.google.gwt.maps.client.event.TrafficOverlayChangedHandler;
import com.google.gwt.maps.client.geocode.DirectionQueryOptions;
import com.google.gwt.maps.client.geocode.DirectionQueryOptions.TravelMode;
import com.google.gwt.maps.client.geocode.DirectionResults;
import com.google.gwt.maps.client.geocode.Directions;
import com.google.gwt.maps.client.geocode.DirectionsCallback;
import com.google.gwt.maps.client.geocode.StatusCodes;
import com.google.gwt.maps.client.geocode.Waypoint;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.Point;
import com.google.gwt.maps.client.geom.Size;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.maps.client.overlay.PolyStyleOptions;
import com.google.gwt.maps.client.overlay.Polyline;
import com.google.gwt.maps.client.overlay.MarkerOptions.ZIndexProcess;
import com.google.gwt.maps.client.overlay.TrafficOverlay;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.event.shared.Event;

import epusp.pcs.os.model.oncall.Position;
import epusp.pcs.os.model.vehicle.Car;
import epusp.pcs.os.model.vehicle.Helicopter;
import epusp.pcs.os.model.vehicle.Priority;
import epusp.pcs.os.model.vehicle.Vehicle;
import epusp.pcs.os.monitor.client.MonitorResources;
import epusp.pcs.os.monitor.client.constants.MonitorWorkspaceConstants;
import epusp.pcs.os.monitor.client.event.HideShowTrafficEvent;
import epusp.pcs.os.monitor.client.event.HideShowTrafficEvent.HideShowTrafficHandler;
import epusp.pcs.os.monitor.client.rpc.IMonitorWorkspaceServiceAsync;
import epusp.pcs.os.shared.client.event.EventBus;
import epusp.pcs.os.shared.client.presenter.Presenter;

public class GoogleMapsPresenter implements Presenter {

	private static final String mapsAPIKey = "AIzaSyDNVvFn0tU2jIjWsOj4oiGaWACX6BgF7Eo";
	private static final String mapsVersion = "2";
	private MapWidget view;

	private DirectionQueryOptions options;

	private IMonitorWorkspaceServiceAsync monitorService; 
	private HandlerManager eventBus;

	private MonitorWorkspaceConstants constants;

	private Marker victim;
	private List<Polyline> victimRoute = new ArrayList<Polyline>();
	
	private HashMap<String, Marker> vehiclesMarker =  new HashMap<String, Marker>(); 
	private HashMap<String, Vehicle> vehicles = new HashMap<String, Vehicle>();
	private HashMap<String, List<Polyline>> vehicleRoute = new HashMap<String, List<Polyline>>();
	
	  private TrafficOverlay trafficInfo;
	
	private MonitorResources resources = MonitorResources.INSTANCE;
	
	public GoogleMapsPresenter(IMonitorWorkspaceServiceAsync monitorService, HandlerManager eventBus, MonitorWorkspaceConstants constants){
		this.monitorService = monitorService;
		this.eventBus = eventBus;
		this.constants = constants;
	}

	@Override
	public void go(final HasWidgets container) {
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
		
		/***************/
		
		LatLng ATLANTA = LatLng.newInstance(33.7814790,
				-84.3880580);

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


		List<Position> p = new ArrayList<Position>();
		p.add(new Position(ATLANTA.getLatitude(), ATLANTA.getLongitude()));
		addVictim(new Position(ATLANTA.getLatitude(), ATLANTA.getLongitude()));
		p.clear();
		
		p.add(new Position(STONE_MOUNTAIN_PARK.getLatitude(), STONE_MOUNTAIN_PARK.getLongitude()));
		updateVictimPosition(p);
		p.clear();

		p.add(new Position(CYCLORAMA.getLatitude(), CYCLORAMA.getLongitude()));
		p.add(new Position(GEORGIA_AQUARIUM.getLatitude(), GEORGIA_AQUARIUM.getLongitude()));
		updateVictimPosition(p);
		p.clear();
		
		p.add(new Position(UNDERGROUND_ATLANTA.getLatitude(), UNDERGROUND_ATLANTA.getLongitude()));
		updateVictimPosition(p);
		
		
		Vehicle vehicle = new Car("2143 XMS");
		vehicle.setPrioraty(Priority.PRIMARY);
		vehicle.setStatus(true);
		vehicle.setId("231290841");
		addVehicle(vehicle, new Position(UNDERGROUND_ATLANTA.getLatitude(), UNDERGROUND_ATLANTA.getLongitude()));
		
		Vehicle vehicle2 = new Car("2143 XKS");
		vehicle2.setPrioraty(Priority.SUPPORT);
		vehicle2.setStatus(true);
		vehicle2.setId("314345454");
		addVehicle(vehicle2, new Position(STONE_MOUNTAIN_PARK.getLatitude(), STONE_MOUNTAIN_PARK.getLongitude()));
		
		p.clear();
		p.add(new Position(CYCLORAMA.getLatitude(), CYCLORAMA.getLongitude()));
		p.add(new Position(ATLANTA.getLatitude(), ATLANTA.getLongitude()));
		updateVehiclePosition(vehicle.getId(), p);
		
		p.clear();
		p.add(new Position(CYCLORAMA.getLatitude(), CYCLORAMA.getLongitude()));
		p.add(new Position(UNDERGROUND_ATLANTA.getLatitude(), UNDERGROUND_ATLANTA.getLongitude()));
		updateVehiclePosition(vehicle2.getId(), p);
		
		Vehicle vehicle3 = new Helicopter();
		vehicle3.setPrioraty(Priority.SUPPORT);
		vehicle3.setStatus(true);
		vehicle3.setId("3021840983");
		addVehicle(vehicle3, new Position(STONE_MOUNTAIN_PARK.getLatitude(), STONE_MOUNTAIN_PARK.getLongitude()));
		
		p.clear();
		p.add(new Position(ATLANTA.getLatitude(), ATLANTA.getLongitude()));
		p.add(new Position(GEORGIA_AQUARIUM.getLatitude(), GEORGIA_AQUARIUM.getLongitude()));
		updateVehiclePosition(vehicle3.getId(), p);
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
		view.setCenter(latLng, 15);
	}

	public void updateVictimPosition(List<Position> positions){
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

	public void addVehicle(Vehicle vehicle, Position begin){
		if(!vehicles.containsKey(vehicle.getId())){
			vehicles.put(vehicle.getId(), vehicle);

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
			default:
				break;
			}
			
			icon.setIconSize(Size.newInstance(25, 25));
			options.setIcon(icon);
			
			LatLng latLng = LatLng.newInstance(begin.getLatitude(), begin.getLongitude());
			
			Marker vehicleMarker = new Marker(latLng, options);
			vehicleMarker.setDraggingEnabled(false);
			vehiclesMarker.put(vehicle.getId(), vehicleMarker);
			
			view.addOverlay(vehicleMarker);
		}
	}
	
	public void updateVehiclePosition(final String id, List<Position> positions){
		if(vehicles.containsKey(id)){
			Vehicle vehicle = vehicles.get(id);
			Marker vehicleMarker = vehiclesMarker.get(id);
			if(!vehicleRoute.containsKey(id)){
				vehicleRoute.put(id, new ArrayList<Polyline>());
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
						final List<Polyline> route = vehicleRoute.get(id);
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
				
				final List<Polyline> route = vehicleRoute.get(id);
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
