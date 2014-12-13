package epusp.pcs.os.shared.model.vehicle.helicopter;

import epusp.pcs.os.shared.model.ICustomAttributes;

public enum HelicopterCustomAttributes implements ICustomAttributes{
	IDADE("idade"), COR_DOS_OLHOS("cor_dos_olhos");
	
	String attributeName;
	
	HelicopterCustomAttributes(String attributeName){
		this.attributeName = attributeName;
	}
	
	HelicopterCustomAttributes(){
	}
	
	@Override
	public String getAttributeName(){
		return attributeName;
	}
}