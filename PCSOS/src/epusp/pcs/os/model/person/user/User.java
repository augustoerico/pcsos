package epusp.pcs.os.model.person.user;

import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.gwt.user.client.rpc.IsSerializable;

import epusp.pcs.os.model.person.Person;

@PersistenceCapable
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class User extends Person implements IsSerializable{
	
	@Persistent
	private String email;
	
	public User(String name, String surname){
		super(name, surname);
	}
	
	public User(String name, String secondName, String surname){
		super(name, secondName, surname);
	}
	
	public String getEmail(){
		return email;
	}
	
	public void setEmail(String email){
		this.email = email;
	}
	
	public abstract AccountTypes getType();
	
	/*
	 * Seen by isSerializable
	 */
	public User(){
		super();
	}



}
