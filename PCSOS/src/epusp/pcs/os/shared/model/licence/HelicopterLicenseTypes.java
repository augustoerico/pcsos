package epusp.pcs.os.shared.model.licence;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.IsSerializable;

import epusp.pcs.os.shared.client.constants.CommonWorkspaceConstants;

public enum HelicopterLicenseTypes implements IsSerializable, LicenseCategory{
	PrivatePilot, CommercialPilot, CertifiedFlightInstructor, CertifiedFlightInstructorInstrument;
	
	@Override
	public String getText(){
		CommonWorkspaceConstants constants = GWT.create(CommonWorkspaceConstants.class);
		switch(this){
		case CertifiedFlightInstructorInstrument:
			return constants.certifiedFlightInstructorInstrument();
		case CommercialPilot:
			return constants.commercialPilot();
		case CertifiedFlightInstructor:
			return constants.certifiedFlightInstructor();			
		case PrivatePilot:
			return constants.privatePilot();
		default:
			return "";
		}
	}
	
	/*
	 * Seen by IsSerializable
	 */
	HelicopterLicenseTypes(){
	}
}
