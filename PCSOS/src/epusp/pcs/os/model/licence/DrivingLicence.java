package epusp.pcs.os.model.licence;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DrivingLicence extends Licence implements IsSerializable{

	public static final LicenceTypes licenceType = LicenceTypes.Drive;
	
	private DrivingCategories category;
	
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
	
	/*
	 * Seen by IsSerializable
	 */
	public DrivingLicence(){
		super();
	}
}
