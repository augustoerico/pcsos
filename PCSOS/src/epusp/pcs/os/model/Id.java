package epusp.pcs.os.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Id implements IsSerializable{
	
	public static long counter = 0;
	
	private String value;
	
	public Id(String type){
		value = type + "_" + counter;
		counter++;
	}
	
	public String getValue(){
		return value;
	}
	
	/*
	 * Seen by IsSerializable
	 */
	public Id(){
		super();
	}
}
