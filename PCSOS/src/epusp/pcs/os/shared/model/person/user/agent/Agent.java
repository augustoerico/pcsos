package epusp.pcs.os.shared.model.person.user.agent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import epusp.pcs.os.shared.model.licence.DrivingLicence;
import epusp.pcs.os.shared.model.licence.License;
import epusp.pcs.os.shared.model.person.user.AccountTypes;
import epusp.pcs.os.shared.model.person.user.User;

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
	
	public void addLicence(License licence){
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
	
	public List<License> getLicences(){
		List<License> licences = new ArrayList<License>();
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
