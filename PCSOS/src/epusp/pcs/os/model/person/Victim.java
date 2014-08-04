package epusp.pcs.os.model.person;

import com.google.gwt.user.client.rpc.IsSerializable;

import epusp.pcs.os.model.device.Device;

public class Victim extends Person implements IsSerializable {
	
	private Device device;
	
	public Victim(String name, String surname){
		super(name, surname);
	}
	
	public Victim(String name, String secondName, String surname){
		super(name, secondName, surname);
	}
	
	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}
	
	/*
	 * Seen by IsSerializable
	 */
	public Victim(){
		super();
	}
}