package epusp.pcs.os.shared.model.person.user;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;

import com.google.gwt.user.client.rpc.IsSerializable;

@PersistenceCapable(identityType=IdentityType.APPLICATION, detachable="true")
public class Admin extends User implements IsSerializable {

	@NotPersistent
	private static final long serialVersionUID = 1L;
	
	public Admin(String name, String surname, String email){
		super(name, surname, email);
	}
	
	public Admin(String name, String secondName, String surname, String email){
		super(name, secondName, surname, email);
	}
	
	@Override
	public AccountTypes getType() {
		return AccountTypes.Admin;
	}

	/*
	 * Seen by Serializable
	 */
	public Admin(){
		super();
	}
}
