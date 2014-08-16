package epusp.pcs.os.model.person.user;

import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;

import com.google.gwt.user.client.rpc.IsSerializable;

@PersistenceCapable
public class Monitor extends User implements IsSerializable {

	@NotPersistent
	public static final AccountTypes accountType = AccountTypes.Monitor;
	
	@NotPersistent
	private Boolean status;
	
	public Monitor(String name, String surname){
		super(name, surname);
	}
	
	public Monitor(String name, String secondName, String surname){
		super(name, secondName, surname);
	}
	
	public void setStatus(Boolean status){
		this.status = status;
	}
	
	public Boolean isFree(){
		return status;
	}
	
	@Override
	public AccountTypes getType() {
		return accountType;
	}
	
	/*
	 * Seen by IsSerializable
	 */
	public Monitor(){
		super();
	}
}
