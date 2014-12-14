package epusp.pcs.os.shared.model.person.user.monitor;

import epusp.pcs.os.shared.model.ICustomAttributes;
import epusp.pcs.os.shared.model.attribute.DataType;

public enum MonitorCustomAttributes implements ICustomAttributes{
	IDADE("idade", DataType.INTEGER), TEST2("test2", DataType.INTERGER_ARRAY);
	
	String attributeName;
	DataType dataType;
	
	MonitorCustomAttributes(String attributeName, DataType dataType){
		this.attributeName = attributeName;
		this.dataType = dataType;
	}
	
	MonitorCustomAttributes(){
	}
	
	@Override
	public String getAttributeName(){
		return attributeName;
	}
	
	public DataType getDataType(){
		return dataType;
	}
}
