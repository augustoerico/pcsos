package epusp.pcs.os.shared.model.licence;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import epusp.pcs.os.shared.model.person.user.agent.Agent;

@PersistenceCapable(identityType=IdentityType.APPLICATION, detachable="true")
public class DrivingLicence extends Licence implements Serializable{

	@NotPersistent
	private static final long serialVersionUID = 1L;

	@NotPersistent
	public static final LicenceTypes licenceType = LicenceTypes.DrivingLicence;
	
    @Persistent(mappedBy="drivingLicence")
    private Agent agent;
	
	@Persistent
	private DrivingCategories category;
	
	@Persistent
	private Boolean hasAcategory;
	
	public DrivingLicence(String registerCode){
		super(registerCode);
	}
	
	public DrivingLicence(String registerCode, Date effectiveUntil){
		super(registerCode, effectiveUntil);
	}
	
	public void setCategory(DrivingCategories category) {
		this.category = category;
	}
	
	public DrivingCategories getCategory() {
		return category;
	}
	
	public Boolean hasAcategory() {
		return hasAcategory;
	}

	public void setHasAcategory(Boolean hasAcategory) {
		this.hasAcategory = hasAcategory;
	}
	
	@Override
	public LicenceTypes getLicenceType() {
		return licenceType;
	}
	
	public Agent getAgent(){
		return agent;
	}
	
	public void setAgent(Agent agent){
		this.agent = agent;
	}
	
	/*
	 * Seen by IsSerializable
	 */
	public DrivingLicence(){
		super();
	}
}
