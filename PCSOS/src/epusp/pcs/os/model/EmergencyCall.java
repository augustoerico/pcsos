package epusp.pcs.os.model;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

import epusp.pcs.os.model.person.Victim;
import epusp.pcs.os.model.person.user.Monitor;

public class EmergencyCall implements IsSerializable{

	private Date begin, end;

	private Victim victim;
	
	private Monitor monitor;
	
	public EmergencyCall(Date begin, Victim victim){
		this.begin = begin;
		this.victim = victim;
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
	
	/*
	 * Seen by IsSerializable
	 */
	public EmergencyCall(){
		super();
	}
}
