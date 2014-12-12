package epusp.pcs.os.shared.model.licence;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import epusp.pcs.os.shared.model.person.user.agent.Agent;

@PersistenceCapable(identityType=IdentityType.APPLICATION, detachable="true")
public class DrivingLicence extends License implements Serializable{

	@NotPersistent
	private static final long serialVersionUID = 1L;

	@NotPersistent
	public static final LicenseTypes licenceType = LicenseTypes.DrivingLicence;
	
	@Persistent
	private DrivingCategories category;
	
	@Persistent
	private Boolean hasAcategory;
	
    @Persistent(mappedBy="drivingLicence")
    private Agent agent;
	
	public DrivingLicence(Agent agent, String registerCode){
		super(registerCode);
		this.agent = agent;
	}
	
	public DrivingLicence(Agent agent,String registerCode, Date effectiveUntil){
		super(registerCode, effectiveUntil);
		this.agent = agent;
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
	public LicenseTypes getLicenceType() {
		return licenceType;
	}
	
	/*
	 * Seen by IsSerializable
	 */
	public DrivingLicence(){
		super();
	}
}
