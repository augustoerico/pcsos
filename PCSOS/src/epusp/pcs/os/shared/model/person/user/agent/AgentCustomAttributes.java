package epusp.pcs.os.shared.model.person.user.agent;

import epusp.pcs.os.shared.model.ICustomAttributes;

public enum AgentCustomAttributes implements ICustomAttributes{
	IDADE("idade"), COR_DOS_OLHOS("cor_dos_olhos"), TEST("test"), TEST2("test2");
	
	String attributeName;
	
	AgentCustomAttributes(String attributeName){
		this.attributeName = attributeName;
	}
	
	AgentCustomAttributes(){
	}
	
	@Override
	public String getAttributeName(){
		return attributeName;
	}
}
