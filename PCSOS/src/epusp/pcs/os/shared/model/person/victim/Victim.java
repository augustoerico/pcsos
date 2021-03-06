package epusp.pcs.os.shared.model.person.victim;

import java.io.Serializable;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;

import epusp.pcs.os.shared.model.person.Person;

@PersistenceCapable(identityType=IdentityType.APPLICATION, detachable="true")
public class Victim extends Person implements Serializable {
	
	@NotPersistent
	private static final long serialVersionUID = 1L;

	public Victim(String name, String surname, String email){
		super(name, surname, email);
	}
	
	public Victim(String name, String secondName, String surname, String email){
		super(name, secondName, surname, email);
	}

	/*
	 * Seen by Serializable
	 */
	public Victim(){
		super();
	}

}
