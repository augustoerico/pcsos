package epusp.pcs.os.shared.model.licence;

import java.io.Serializable;

public enum DrivingCategories implements Serializable, LicenseCategory {
	B, C, D, E;
	
	/*
	 * Seen by IsSerializable
	 */
	DrivingCategories(){
	}

	@Override
	public String getText() {
		return this.name();
	}
}
