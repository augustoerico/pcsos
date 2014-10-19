package epusp.pcs.os.shared.model.licence;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType=IdentityType.APPLICATION, detachable="true")
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class Licence implements Serializable {
	
	@NotPersistent
	private static final long serialVersionUID = 1L;
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String id;
	
    @Persistent
    @Extension(vendorName="datanucleus", key="gae.pk-name", value="true")
	private String registerCode;
	
	@Persistent
	private Date effectiveUntil;
	
	public Licence(String registerCode){
		this.registerCode = registerCode;
	}
	
	public Licence(String registerCode, Date effectiveUntil){
		this.registerCode = registerCode;
		this.effectiveUntil = effectiveUntil;
	}
	
	public String getRegisterCode(){
		return registerCode;
	}
	
	public void setValidUntil(Date effectiveUntil){
		this.effectiveUntil = effectiveUntil;
	}
	
	public Date validUntil(){
		return effectiveUntil;
	}

	public abstract LicenceTypes getLicenceType();
	
	/*
	 * Seen by IsSerializable
	 */
	public Licence(){
		super();
	}
}
