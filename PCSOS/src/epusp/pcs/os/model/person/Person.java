package epusp.pcs.os.model.person;

import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class Person implements IsSerializable {
	
	private String name;
	private String secondName;
	private String surname;
	private Boolean isActive = false; //default value
	
	public Person(String name, String surname){
		this.name = name;
		this.surname = surname;
	}
	
	public Person(String name, String secondName, String surname){
		this.name = name;
		this.secondName = secondName;
		this.surname = surname;
	}
	
	public String getName(){
		return name;
	}
	
	public String getSecondName(){
		return secondName;
	}
	
	public String getSurname(){
		return surname;
	}
	
	public Boolean isActive(){
		return isActive;
	}
	
	public void setIsActive(Boolean isActive){
		this.isActive = isActive;
	}

	/*
	 * Seen by IsSerializable
	 */
	public Person(){
		super();
	}
}
