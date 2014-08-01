package epusp.pcs.os.model.licence;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CarLicence extends Licence implements IsSerializable{

	public static final LicenceTypes licenceType = LicenceTypes.Car;
	
	/*
	 * Seen by IsSerializable
	 */
	public CarLicence(){
		super();
	}

}
