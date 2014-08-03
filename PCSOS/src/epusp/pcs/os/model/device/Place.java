package epusp.pcs.os.model.device;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Place implements IsSerializable {
	private int latitude;
	private int longitude;
	
	public Place(){
		super();
	}
	
	public int getLatitude() {
		return latitude;
	}
	
	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}
	
	public int getLongitude() {
		return longitude;
	}
	
	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}

}
