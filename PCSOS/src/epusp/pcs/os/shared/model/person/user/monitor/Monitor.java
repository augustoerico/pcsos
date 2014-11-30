package epusp.pcs.os.shared.model.person.user.monitor;

import java.io.Serializable;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;

import epusp.pcs.os.shared.model.person.user.AccountTypes;
import epusp.pcs.os.shared.model.person.user.User;

@PersistenceCapable(identityType=IdentityType.APPLICATION, detachable="true")
public class Monitor extends User implements Serializable {

	@NotPersistent
	private static final long serialVersionUID = 1L;
	
	@NotPersistent
	private Boolean status;
	
	public Monitor(String name, String surname, String email){
		super(name, surname, email);
	}
	
	public Monitor(String name, String secondName, String surname, String email){
		super(name, secondName, surname, email);
	}
	
	public void setStatus(Boolean status){
		this.status = status;
	}
	
	public Boolean isAvailable(){
		return status;
	}
	
	@Override
	public AccountTypes getType() {
		return  AccountTypes.Monitor;
	}
	
	/*
	 * Seen by IsSerializable
	 */
	public Monitor(){
		super();
	}
}
