package epusp.pcs.os.shared.model.oncall;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType=IdentityType.APPLICATION, detachable="true")
@FetchGroup(name="agents", members = {@Persistent(name="agents")})
public class VehicleOnCall implements Serializable{

	@NotPersistent
	private static final long serialVersionUID = 1L;
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String id;
	
	@Persistent
	private String vehicleIdTag;
	
	@Persistent
	private List<String> agents = new ArrayList<String>();
	
	@Persistent
	private List<Double> latitudes = new ArrayList<Double>();
	
	@Persistent
	private List<Double> longitudes = new ArrayList<Double>();
		
	public VehicleOnCall(String vehicleIdTag, Collection<String> agents){
		this.vehicleIdTag = vehicleIdTag;
		this.agents.addAll(agents);
	}
	
	public String getId(){
		return id;
	}

	public String getVehicleIdTag() {
		return vehicleIdTag;
	}
	
	public void addPosition(Position positon){
		if(!positon.isEmpty()){
			latitudes.add(positon.getLatitude());
			longitudes.add(positon.getLongitude());
		}
	}
	
	public List<Position> getPositions(){
		List<Position> positions = new ArrayList<Position>();
		for(int i = 0; i < latitudes.size(); i++){
			positions.add(new Position(latitudes.get(i), longitudes.get(i)));
		}
		return positions;
	}
	
	public List<Position> getPositions(int i){
		List<Position> positions = new ArrayList<Position>();
		for(; i < latitudes.size(); i++){
			positions.add(new Position(latitudes.get(i), longitudes.get(i)));
		}
		return positions;
	}
	
	public void addPositions(List<Position> positions){
		for(Position position : positions){
			latitudes.add(position.getLatitude());
			longitudes.add(position.getLongitude());
		}
	}
	
	public Position getPosition(int i){
		return new Position(latitudes.get(i), longitudes.get(i));
	}
	
	public Position getLastPosition(){
		int i = latitudes.size()-1;
		if(i < 0)
			return new Position();
		return new Position(latitudes.get(i), longitudes.get(i));
	}
	
	public List<String> getAgents(){
		return agents;
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
