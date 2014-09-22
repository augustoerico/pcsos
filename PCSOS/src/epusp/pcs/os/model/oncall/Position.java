package epusp.pcs.os.model.oncall;

import java.io.Serializable;

public class Position implements Serializable{

	private static final long serialVersionUID = 1L;

	private double latitude, longitude;
	
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
	
	/*
	 * Seen by Serializable
	 */
	public Position(){
		super();
	}
}
