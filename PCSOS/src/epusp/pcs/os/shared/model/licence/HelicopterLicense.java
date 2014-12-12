package epusp.pcs.os.shared.model.licence;

import java.util.Date;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import epusp.pcs.os.shared.model.person.user.agent.Agent;

@PersistenceCapable(identityType=IdentityType.APPLICATION, detachable="true")
public class HelicopterLicense extends License{

	@NotPersistent
	private static final long serialVersionUID = 1L;
	
	@NotPersistent
	public static final LicenseTypes licenceType = LicenseTypes.DrivingLicence;
	
	@Persistent
	private HelicopterLicenseTypes category;
	
    @Persistent
    private Agent agent;

	public HelicopterLicense(Agent agent, String registerCode){
		super(registerCode);
		this.agent = agent;
	}
	
	public HelicopterLicense(Agent agent, String registerCode, Date effectiveUntil){
		super(registerCode, effectiveUntil);
		this.agent = agent;
	}
    
	@Override
	public LicenseTypes getLicenceType() {
		return licenceType;
	}

}
