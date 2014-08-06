package epusp.pcs.os.model.device;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.user.client.rpc.IsSerializable;

@PersistenceCapable
public class Device implements IsSerializable {
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	@Persistent
	private float latitude;
	
	@Persistent
	private float longitude;
	
//	private Id id = new Id(Device.class.getName());
	
//	private Place currentPlace;
	
/*	public Id getId(){
		return id;
	}
	*/
/*	public void setPlace(Place place){
		this.currentPlace = place;
	}
	
	public Place getCurrentPlace(){
		return currentPlace;
	}
	*/
	
	/*
	 * Seen by IsSerializable
	 */
	public Device(){
		super();
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
}
