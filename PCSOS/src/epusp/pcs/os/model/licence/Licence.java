package epusp.pcs.os.model.licence;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class Licence implements IsSerializable {
	
	private String registerCode;
	
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
