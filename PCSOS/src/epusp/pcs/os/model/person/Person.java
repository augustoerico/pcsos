package epusp.pcs.os.model.person;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.user.client.rpc.IsSerializable;

@PersistenceCapable
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class Person implements IsSerializable {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	@Persistent
	private String name;
	@Persistent
	private String secondName;
	@Persistent
	private String surname;
	@Persistent
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
	
	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
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
