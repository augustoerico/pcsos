package epusp.pcs.os.shared.model.licence;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.IsSerializable;

import epusp.pcs.os.shared.client.constants.CommonWorkspaceConstants;

public enum LicenceTypes implements IsSerializable{
	DrivingLicence;
	
	public String getText(){
		CommonWorkspaceConstants constants = GWT.create(CommonWorkspaceConstants.class);
		switch (this) {
		case DrivingLicence:
			return constants.drivingLicence();
		default:
			return "";
		}
	}
	
	/*
	 * Seen by IsSerializable
	 */
	LicenceTypes(){
	}
}
