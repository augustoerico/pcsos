package epusp.pcs.os.model.person;

import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.gwt.user.client.rpc.IsSerializable;

@PersistenceCapable
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class Person implements IsSerializable {

	@PrimaryKey
	@Persistent
	private String googleUserID;
	@Persistent
	private String name;
	@Persistent
	private String secondName;
	@Persistent
	private String surname;
	@Persistent
	private Boolean isActive = false; //default value
	@Persistent
	private String imageURL;
	
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
	
	public String getPictureURL() {
		return imageURL;
	}

	public void setPictureURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
	public String getGoogleUserID() {
		return googleUserID;
	}

	public void setGoogleUserID(String googleUserID) {
		this.googleUserID = googleUserID;
	}

	/*
	 * Seen by IsSerializable
	 */
	public Person(){
		super();
	}
}
