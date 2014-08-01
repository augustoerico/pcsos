package epusp.pcs.os.model.user;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Auditor extends User implements IsSerializable {

	public static final AccountTypes accountType = AccountTypes.Auditor;
	
	/*
	 * Seen by IsSerializable
	 */
	public Auditor(){
		super();
	}
}
