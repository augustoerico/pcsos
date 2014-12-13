package epusp.pcs.os.shared.model.vehicle.motorcycle;

import epusp.pcs.os.shared.model.ICustomAttributes;

public enum MotorcycleCustomAttributes implements ICustomAttributes{
	IDADE("idade"), COR_DOS_OLHOS("cor_dos_olhos");
	
	String attributeName;
	
	MotorcycleCustomAttributes(String attributeName){
		this.attributeName = attributeName;
	}
	
	MotorcycleCustomAttributes(){
	}
	
	@Override
	public String getAttributeName(){
		return attributeName;
	}
}
