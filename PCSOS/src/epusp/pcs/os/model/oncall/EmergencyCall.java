package epusp.pcs.os.model.oncall;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import epusp.pcs.os.model.person.Victim;
import epusp.pcs.os.model.person.user.Monitor;

@PersistenceCapable(identityType=IdentityType.APPLICATION, detachable="true")
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
@FetchGroup(name="all_attributes", members = {@Persistent(name="victim"), @Persistent(name="monitor"), @Persistent(name="vehicles")})
public class EmergencyCall implements Serializable {

	@PrimaryKey
	@Persistent(valueStrategy=IdGeneratorStrategy.INCREMENT)
	private Long id;

	@NotPersistent
	private static final long serialVersionUID = 1L;

	@Persistent
	private Date begin, end;

	@Persistent
	private Victim victim;

	@Persistent
	private Monitor monitor;

	@Persistent
	private List<VehicleOnCall> vehicles = new ArrayList<VehicleOnCall>();
	
	@Persistent
	private final List<Double> latitudes = new ArrayList<Double>();

	@Persistent
	private final List<Double> longitudes = new ArrayList<Double>();

	public EmergencyCall(Date begin, Victim victim){
		this.begin = begin;
		this.victim = victim;
	}

	public Long getId(){
		return id;
	}

	public Victim getVictim(){
		return victim;
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

	public Monitor getMonitor() {
		return monitor;
	}

	public void setMonitor(Monitor monitor) {
		this.monitor = monitor;
	}

	public void addVictimPosition(Position position){
		latitudes.add(position.getLatitude());
		longitudes.add(position.getLongitude());
	}
	
	public Position getVictimPosition(int i){
		return new Position(latitudes.get(i), longitudes.get(i));
	}
	
	public int getVictimPositionSize(){
		return latitudes.size();
	}
	
	public void addVehicle(Long vehicleId){
		vehicles.add(new VehicleOnCall(vehicleId));
	}
	
	public void addVehiclePosition(Long vehicleId, Position position){
		for(VehicleOnCall vehicle : vehicles){
			if(vehicle.getVehicleId().equals(vehicleId)){
				vehicle.addPosition(position);
				return;
			}
		}
	}

	public Position getVehiclePosition(Long vehicleId, int i){
		for(VehicleOnCall vehicle : vehicles){
			if(vehicle.getVehicleId().equals(vehicleId)){
				return vehicle.getPosition(i);
			}
		}
		return null;
	}
	
	public int getVehiclePositionSize(Long vehicleId){
		for(VehicleOnCall vehicle : vehicles){
			if(vehicle.getVehicleId().equals(vehicleId)){
				return vehicle.getSize();
			}
		}
		return 0;
	}
	
	public Position getLastVehiclePosition(Long vehicleId){
		for(VehicleOnCall vehicle : vehicles){
			if(vehicle.getVehicleId().equals(vehicleId)){
				return vehicle.getLastPosition();
			}
		}
		return null;
	}
	
	/*
	 * Seen by IsSerializable
	 */
	public EmergencyCall(){
		super();
	}
}
