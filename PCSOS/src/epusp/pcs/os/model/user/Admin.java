package epusp.pcs.os.model.user;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Admin extends User implements IsSerializable {

	public static final AccountTypes accountType = AccountTypes.Admin;
	
	public Admin(String name, String surname){
		super(name, surname);
	}
	
	public Admin(String name, String secondName, String surname){
		super(name, secondName, surname);
	}

	/*
	 * Seen by IsSerializable
	 */
	public Admin(){
		super();
	}
}
