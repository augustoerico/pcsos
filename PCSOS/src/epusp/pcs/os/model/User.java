package epusp.pcs.os.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class User implements IsSerializable{
	private static String name = "Ercius";
	
	public String getName(){
		return name;
	}
}
