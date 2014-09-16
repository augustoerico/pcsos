package epusp.pcs.os.model.person.user;

import java.io.Serializable;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;

@PersistenceCapable(identityType=IdentityType.APPLICATION, detachable="true")
public class SuperUser extends User implements Serializable{

	@NotPersistent
	private static final long serialVersionUID = 1L;
	
	public SuperUser(String name, String surname, String email){
		super(name, surname, email);
	}
	
	public SuperUser(String name, String secondName, String surname, String email){
		super(name, secondName, surname, email);
	}
	
	@Override
	public AccountTypes getType() {
		return AccountTypes.SuperUser;
	}
	
	/*
	 * Seen by IsSerializable
	 */
	public SuperUser(){
		super();
	}
}
