package epusp.pcs.os.model.user;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Monitor extends User implements IsSerializable {

	public static final AccountTypes accountType = AccountTypes.Monitor;
	
	private Boolean status;
	
	public void setStatus(Boolean status){
		this.status = status;
	}
	
	public Boolean isFree(){
		return status;
	}
	
	/*
	 * Seen by IsSerializable
	 */
	public Monitor(){
		super();
	}

}
