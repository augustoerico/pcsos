package epusp.pcs.os.model.person;

import java.io.Serializable;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import epusp.pcs.os.model.SystemObject;

@PersistenceCapable(identityType=IdentityType.APPLICATION, detachable="true")
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class Person extends SystemObject implements Serializable {

	@NotPersistent
	private static final long serialVersionUID = 1L;
	
    @Persistent
    @Extension(vendorName="datanucleus", key="gae.pk-name", value="true")
	private String email;
    
    @Persistent
    private String googleUserId;
	
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
	
	public Person(String name, String surname, String email){
		this.name = name;
		this.surname = surname;
		this.email = email;
	}
	
	public Person(String name, String secondName, String surname, String email){
		this.name = name;
		this.secondName = secondName;
		this.surname = surname;
		this.email = email;
	}
	
	public String getEmail(){
		return email;
	}
	
	public String getGoogleUserId() {
		return googleUserId;
	}

	public void setGoogleUserId(String googleUserId) {
		this.googleUserId = googleUserId;
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
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Person){
			Person person = (Person) obj;
			return getId().equals(person.getId());
		}
		return false;
	}

	/*
	 * Seen by Serializable
	 */
	public Person(){
		super();
	}
}
