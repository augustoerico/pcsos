package epusp.pcs.os.model.person.user;

import java.io.Serializable;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;

import epusp.pcs.os.model.person.Person;

@PersistenceCapable(identityType=IdentityType.APPLICATION, detachable="true")
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class User extends Person implements Serializable{
	
	@NotPersistent
	private static final long serialVersionUID = 1L;

	public User(String name, String surname, String email){
		super(name, surname, email);
	}
	
	public User(String name, String secondName, String surname, String email){
		super(name, secondName, surname, email);
	}
	
	public abstract AccountTypes getType();
	
	/*
	 * Seen by isSerializable
	 */
	public User(){
		super();
	}



}
