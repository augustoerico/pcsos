package epusp.pcs.os.model.oncall;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType=IdentityType.APPLICATION, detachable="true")
public class VehicleOnCall implements Serializable{

	@NotPersistent
	private static final long serialVersionUID = 1L;
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String id;
	
	@Persistent
	private Long vehicleId;
	
	@Persistent
	private List<Double> latitudes = new ArrayList<Double>();
	
	@Persistent
	private List<Double> longitudes = new ArrayList<Double>();
		
	public VehicleOnCall(Long vehicleId){
		this.vehicleId = vehicleId;
	}
	
	public String getId(){
		return id;
	}

	public Long getVehicleId() {
		return vehicleId;
	}
	
	public void addPosition(Position positon){
		latitudes.add(positon.getLatitude());
		longitudes.add(positon.getLongitude());
	}
	
	public Position getPosition(int i){
		return new Position(latitudes.get(i), longitudes.get(i));
	}
	
	public Position getLastPosition() {
		int i = latitudes.size()-1;
		return new Position(latitudes.get(i), longitudes.get(i));
	}
	
	public int getSize(){
		return latitudes.size();
	}

	/*
	 * Seen by Serializable
	 */
	public VehicleOnCall(){
		super();
	}
}
