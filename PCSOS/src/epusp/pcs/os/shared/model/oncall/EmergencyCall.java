package epusp.pcs.os.shared.model.oncall;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType=IdentityType.APPLICATION, detachable="true")
@FetchGroup(name="all_attributes", members = {@Persistent(name="victim"), @Persistent(name="monitor"), @Persistent(name="vehicles")})
public class EmergencyCall implements Serializable {

	@PrimaryKey
	@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
	private Long id;

	@NotPersistent
	private static final long serialVersionUID = 1L;
	
	@NotPersistent
	private EmergencyCallLifecycle emergencyCallLifecycle = EmergencyCallLifecycle.Waiting;

	@Persistent
	private Date begin, end;

	@Persistent
	private String victimEmail;

	@Persistent
	private String monitorId;

	@Persistent
	private List<VehicleOnCall> vehicles = new ArrayList<VehicleOnCall>();
	
	@Persistent
	private List<Double> latitudes = new ArrayList<Double>(); 

	@Persistent
	private List<Double> longitudes = new ArrayList<Double>();

	public EmergencyCall(Date begin, String victimEmail){
		this.begin = begin;
		this.victimEmail = victimEmail;
	}

	public Long getId(){
		return id;
	}

	public String getVictimEmail(){
		return victimEmail;
	}

	public Date getBegin() {
		return begin;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public String getMonitor() {
		return monitorId;
	}

	public void setMonitor(String monitorId) {
		this.monitorId = monitorId;
	}

	public void addVictimPosition(Position position){
		if(!position.isEmpty()){
			latitudes.add(position.getLatitude());
			longitudes.add(position.getLongitude());
		}
	}
	
	public void addVictimPositions(List<Position> positions){
		for(Position position : positions){
			latitudes.add(position.getLatitude());
			longitudes.add(position.getLongitude());
		}
	}
	
	public List<Position> getVictimPositions(){
		List<Position> positions = new ArrayList<Position>();
		for(int i = 0; i < latitudes.size(); i++){
			positions.add(new Position(latitudes.get(i), longitudes.get(i)));
		}
		return positions;
	}
	
	public List<Position> getVictimPositions(int i){
		List<Position> positions = new ArrayList<Position>();
		for(; i < latitudes.size(); i++){
			positions.add(new Position(latitudes.get(i), longitudes.get(i)));
		}
		return positions;
	}

	public Position getVictimPosition(int i){
		return new Position(latitudes.get(i), longitudes.get(i));
	}
	
	public int getVictimPositionSize(){
		return latitudes.size();
	}
	
	public Position getLastVictimPosition(){
		int i = latitudes.size()-1;
		if(i < 0)
			return new Position();
		return new Position(latitudes.get(i), longitudes.get(i));
	}
	
	public void addVehicle(String vehicleIdTag, Collection<String> agents){
		vehicles.add(new VehicleOnCall(vehicleIdTag, agents));
	}
	
	public void addVehiclePosition(String vehicleIdTag, Position position){
		if(!position.isEmpty()){
			for(VehicleOnCall vehicle : vehicles){
				if(vehicle.getVehicleIdTag().equals(vehicleIdTag)){
					vehicle.addPosition(position);
					return;
				}
			}
		}
	}

	public Position getVehiclePosition(String vehicleIdTag, int i){
		for(VehicleOnCall vehicle : vehicles){
			if(vehicle.getVehicleIdTag().equals(vehicleIdTag)){
				return vehicle.getPosition(i);
			}
		}
		return new Position();
	}
	
	public List<Position> getVehiclePositions(String vehicleIdTag){
		List<Position> positions = new ArrayList<Position>();
		for(VehicleOnCall vehicle : vehicles){
			if(vehicle.getVehicleIdTag().equals(vehicleIdTag)){
				positions.addAll(vehicle.getPositions());
				return positions;
			}
		}
		return positions;
	}
	
	public void addVehiclePositions(String vehicleIdTag, List<Position> positions){
		for(VehicleOnCall vehicle : vehicles){
			if(vehicle.getVehicleIdTag().equals(vehicleIdTag)){
				vehicle.addPositions(positions);
			}
		}
	}
	
	public int getVehiclePositionSize(String vehicleIdTag){
		for(VehicleOnCall vehicle : vehicles){
			if(vehicle.getVehicleIdTag().equals(vehicleIdTag)){
				return vehicle.getSize();
			}
		}
		return 0;
	}
	
	public Position getLastVehiclePosition(String vehicleIdTag){
		for(VehicleOnCall vehicle : vehicles){
			if(vehicle.getVehicleIdTag().equals(vehicleIdTag)){
				return vehicle.getLastPosition();
			}
		}
		return new Position();
	}
	
	public List<VehicleOnCall> getVehicles(){
		return vehicles;
	}
	
	public EmergencyCallLifecycle getEmergencyCallLifecycle(){
		return emergencyCallLifecycle;
	}
	
	public void setEmergencyCallLifecycle(EmergencyCallLifecycle emergencyCallLifecycle){
		this.emergencyCallLifecycle = emergencyCallLifecycle;
	}
	
	/*
	 * Seen by IsSerializable
	 */
	public EmergencyCall(){
		super();
	}
}
