package epusp.pcs.os.shared.model.vehicle.helicopter;

import epusp.pcs.os.shared.model.ICustomAttributes;

public enum HelicopterCustomProperties implements ICustomAttributes{
	IDADE("idade"), COR_DOS_OLHOS("cor_dos_olhos");
	
	String attributeName;
	
	HelicopterCustomProperties(String attributeName){
		this.attributeName = attributeName;
	}
	
	HelicopterCustomProperties(){
	}
	
	@Override
	public String getAttributeName(){
		return attributeName;
	}
}