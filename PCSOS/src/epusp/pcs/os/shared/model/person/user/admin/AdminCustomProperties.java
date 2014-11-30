package epusp.pcs.os.shared.model.person.user.admin;

import epusp.pcs.os.shared.model.ICustomAttributes;

public enum AdminCustomProperties implements ICustomAttributes{
	IDADE("idade"), COR_DOS_OLHOS("cor_dos_olhos"), TEST("test"), TEST2("test2");
	
	String attributeName;
	
	AdminCustomProperties(String attributeName){
		this.attributeName = attributeName;
	}
	
	AdminCustomProperties(){
	}
	
	@Override
	public String getAttributeName(){
		return attributeName;
	}
}
