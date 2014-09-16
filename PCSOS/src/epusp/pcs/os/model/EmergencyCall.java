package epusp.pcs.os.model;

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
import epusp.pcs.os.model.vehicle.Car;

@PersistenceCapable(identityType=IdentityType.APPLICATION, detachable="true")
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
@FetchGroup(name="all_attributes", members = {@Persistent(name="victim"), @Persistent(name="monitor"), @Persistent(name="cars")})
public class EmergencyCall extends SystemObject implements Serializable {

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
	private List<Car> cars = new ArrayList<Car>();
	
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

	public void addPosition(double latitude, double longitude){
		latitudes.add(latitude);
		longitudes.add(longitude);
	}

	public Double[] getPosition(int i){
		Double[] position = new Double[2];
		if(i < latitudes.size()){
			position[0] = latitudes.get(i);
			position[1] = latitudes.get(i);
		}
		return position;
	}
	
	public int getPositionSize(){
		return latitudes.size();
	}
	
	/*
	 * Seen by IsSerializable
	 */
	public EmergencyCall(){
		super();
	}
}
