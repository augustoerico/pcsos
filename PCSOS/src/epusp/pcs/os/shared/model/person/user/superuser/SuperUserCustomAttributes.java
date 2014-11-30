package epusp.pcs.os.shared.model.person.user.superuser;

import epusp.pcs.os.shared.model.ICustomAttributes;

public enum SuperUserCustomAttributes implements ICustomAttributes{
	IDADE("idade");
	
	String attributeName;
	
	SuperUserCustomAttributes(String attributeName){
		this.attributeName = attributeName;
	}
	
	SuperUserCustomAttributes(){
	}
	
	@Override
	public String getAttributeName(){
		return attributeName;
	}
}

