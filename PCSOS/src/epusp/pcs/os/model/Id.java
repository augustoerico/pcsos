package epusp.pcs.os.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Id implements IsSerializable{
	
	public static long counter = 0;
	
	private long value;
	
	public Id(){
		super();
		value = counter;
		counter++;
	}
	
	public long getValue(){
		return value;
	}
}
