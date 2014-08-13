package epusp.pcs.os.model.person.user;

import com.google.gwt.user.client.rpc.IsSerializable;

import epusp.pcs.os.model.person.Person;

public abstract class User extends Person implements IsSerializable{
	
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
