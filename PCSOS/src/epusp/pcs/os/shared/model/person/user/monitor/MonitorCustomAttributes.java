package epusp.pcs.os.shared.model.person.user.monitor;

import epusp.pcs.os.shared.model.ICustomAttributes;

public enum MonitorCustomAttributes implements ICustomAttributes{
	IDADE("idade"), TEST2("test2");
	
	String attributeName;
	
	MonitorCustomAttributes(String attributeName){
		this.attributeName = attributeName;
	}
	
	MonitorCustomAttributes(){
	}
	
	@Override
	public String getAttributeName(){
		return attributeName;
	}
}
