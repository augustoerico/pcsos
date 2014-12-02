package epusp.pcs.os.shared.model.vehicle.car;

import epusp.pcs.os.shared.model.ICustomAttributes;

public enum CarCustomProperties implements ICustomAttributes{
	IDADE("idade"), COR_DOS_OLHOS("cor_dos_olhos");
	
	String attributeName;
	
	CarCustomProperties(String attributeName){
		this.attributeName = attributeName;
	}
	
	CarCustomProperties(){
	}
	
	@Override
	public String getAttributeName(){
		return attributeName;
	}
}