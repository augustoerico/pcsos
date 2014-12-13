package epusp.pcs.os.shared.model.person.user.agent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import epusp.pcs.os.shared.model.licence.DrivingLicense;
import epusp.pcs.os.shared.model.licence.HelicopterLicense;
import epusp.pcs.os.shared.model.licence.License;
import epusp.pcs.os.shared.model.person.user.AccountTypes;
import epusp.pcs.os.shared.model.person.user.User;

@PersistenceCapable(identityType=IdentityType.APPLICATION, detachable="true")
public class Agent extends User implements Serializable {

	@NotPersistent
	private static final long serialVersionUID = 1L;
	
	@Persistent(defaultFetchGroup="true")
	private DrivingLicense drivingLicence;
	
	@Persistent(defaultFetchGroup="true")
	private HelicopterLicense helicopterLicense;
	
	public Agent(String name, String surname, String email){
		super(name, surname, email);
	}
	
	public Agent(String name, String secondName, String surname, String email){
		super(name, secondName, surname, email);
	}
	
	public void addLicense(License license){
		switch(license.getLicenceType()){
		case DrivingLicence:
			drivingLicence = (DrivingLicense) license;
			break;
		case HelicopterLicense:
			helicopterLicense = (HelicopterLicense) license;
			break;
		default:
			break;
		}
	}
	
	public DrivingLicense getDrivingLicence(){
		return drivingLicence;
	}
	
	public HelicopterLicense getHelicopterLicense(){
		return helicopterLicense;
	}
	
	public List<License> getLicenses(){
		List<License> licences = new ArrayList<License>();
		if(drivingLicence !=  null)
			licences.add(drivingLicence);
		if(helicopterLicense != null)
			licences.add(helicopterLicense);
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
