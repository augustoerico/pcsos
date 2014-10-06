package epusp.pcs.os.model.oncall;

import java.io.Serializable;

public class Position implements Serializable{

	private static final long serialVersionUID = 1L;

	private Double latitude, longitude;
	
	public Position(double latitude, double longitude){
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}
	
	public Boolean isEmpty(){
		return (latitude == null) || (longitude == null);
	}
	
	@Override
	public String toString() {
		return "(".concat(String.valueOf(latitude)).concat(", ").concat(String.valueOf(longitude)).concat(")");
	}
	
	/*
	 * Seen by Serializable
	 */
	public Position(){
		super();
	}
}
