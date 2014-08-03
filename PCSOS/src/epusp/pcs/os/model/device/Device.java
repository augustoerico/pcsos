package epusp.pcs.os.model.device;

import com.google.gwt.user.client.rpc.IsSerializable;

import epusp.pcs.os.model.Id;

public class Device implements IsSerializable {
	
	private Id id = new Id();
	
	private Place currentPlace;
	
	public Id getId(){
		return id;
	}
	
	public void setPlace(Place place){
		this.currentPlace = place;
	}
	
	public Place getCurrentPlace(){
		return currentPlace;
	}
	
	/*
	 * Seen by IsSerializable
	 */
	public Device(){
		super();
	}
}
