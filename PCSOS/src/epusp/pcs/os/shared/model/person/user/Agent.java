package epusp.pcs.os.shared.model.person.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import epusp.pcs.os.shared.model.licence.DrivingLicence;
import epusp.pcs.os.shared.model.licence.Licence;

@PersistenceCapable(identityType=IdentityType.APPLICATION, detachable="true")
@FetchGroup(name = "licences", members = { @Persistent(name = "drivingLicence") })
public class Agent extends User implements Serializable {

	@NotPersistent
	private static final long serialVersionUID = 1L;
	
	@Persistent
	private DrivingLicence drivingLicence;
	
	public Agent(String name, String surname, String email){
		super(name, surname, email);
	}
	
	public Agent(String name, String secondName, String surname, String email){
		super(name, secondName, surname, email);
	}
	
	public void addLicence(Licence licence){
		switch(licence.getLicenceType()){
		case DrivingLicence:
			drivingLicence = (DrivingLicence) licence;
			break;
		default:
			break;
		}
	}
	
	public DrivingLicence getDrivingLicence(){
		return drivingLicence;
	}
	
	public List<Licence> getLicences(){
		List<Licence> licences = new ArrayList<Licence>();
		if(drivingLicence !=  null)
			licences.add(drivingLicence);
		return licences;
	}
	
	@Override
	public AccountTypes getType() {
		return AccountTypes.Agent;
	}
	
	/*
	 * Seen by Serializable
	 */
	public Agent(){
		super();
	}
}
