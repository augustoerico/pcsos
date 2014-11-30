package epusp.pcs.os.shared.model.person.victim;

import epusp.pcs.os.shared.model.ICustomAttributes;

public enum VictimCustomAttributes implements ICustomAttributes{
	IDADE("idade"), COR_DOS_OLHOS("cor_dos_olhos");
	
	String attributeName;
	
	VictimCustomAttributes(String attributeName){
		this.attributeName = attributeName;
	}
	
	VictimCustomAttributes(){
	}
	
	@Override
	public String getAttributeName(){
		return attributeName;
	}
}
