package epusp.pcs.os.model.user;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SuperUser extends User implements IsSerializable{

	public static final AccountTypes accountType = AccountTypes.SuperUser;
	
	/*
	 * Seen by IsSerializable
	 */
	public SuperUser(){
		super();
	}
}
