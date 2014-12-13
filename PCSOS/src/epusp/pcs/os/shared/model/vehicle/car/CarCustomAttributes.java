package epusp.pcs.os.shared.model.vehicle.car;

import epusp.pcs.os.shared.model.ICustomAttributes;

public enum CarCustomAttributes implements ICustomAttributes{
	IDADE("idade"), COR_DOS_OLHOS("cor_dos_olhos");
	
	String attributeName;
	
	CarCustomAttributes(String attributeName){
		this.attributeName = attributeName;
	}
	
	CarCustomAttributes(){
	}
	
	@Override
	public String getAttributeName(){
		return attributeName;
	}
}