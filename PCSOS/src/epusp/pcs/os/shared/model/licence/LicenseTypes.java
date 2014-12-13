package epusp.pcs.os.shared.model.licence;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.IsSerializable;

import epusp.pcs.os.shared.client.constants.CommonWorkspaceConstants;

public enum LicenseTypes implements IsSerializable{
	DrivingLicence, HelicopterLicense;
	
	public String getText(){
		CommonWorkspaceConstants constants = GWT.create(CommonWorkspaceConstants.class);
		switch (this) {
		case DrivingLicence:
			return constants.drivingLicence();
		case HelicopterLicense:
			return constants.helicopterLicence();
		default:
			return "";
		}
	}
	
	public LicenseCategory[] getLicenseCategories(){
		switch (this) {
		case DrivingLicence:
			return DrivingCategories.values();
		case HelicopterLicense:
			return HelicopterLicenseTypes.values();
		default:
			return null;
		}
	}
	
	/*
	 * Seen by IsSerializable
	 */
	LicenseTypes(){
	}
}
