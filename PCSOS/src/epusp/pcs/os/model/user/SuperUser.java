package epusp.pcs.os.model.user;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SuperUser extends User implements IsSerializable{

	public static final AccountTypes accountType = AccountTypes.SuperUser;
	
	public SuperUser(String name, String surname){
		super(name, surname);
	}
	
	public SuperUser(String name, String secondName, String surname){
		super(name, secondName, surname);
	}
	
	/*
	 * Seen by IsSerializable
	 */
	public SuperUser(){
		super();
	}
}
