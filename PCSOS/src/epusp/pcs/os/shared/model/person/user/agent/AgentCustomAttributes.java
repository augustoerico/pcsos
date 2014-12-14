package epusp.pcs.os.shared.model.person.user.agent;

import epusp.pcs.os.shared.model.ICustomAttributes;
import epusp.pcs.os.shared.model.attribute.DataType;

public enum AgentCustomAttributes implements ICustomAttributes{
	IDADE("idade", DataType.INTEGER), COR_DOS_OLHOS("cor_dos_olhos", DataType.STRING), TEST("test", DataType.DATE_ARRAY), TEST2("test2", DataType.INTERGER_ARRAY);
	
	String attributeName;
	DataType dataType;
	
	AgentCustomAttributes(String attributeName, DataType dataType){
		this.attributeName = attributeName;
		this.dataType = dataType;
	}
	
	AgentCustomAttributes(){
	}
	
	@Override
	public String getAttributeName(){
		return attributeName;
	}
	
	public DataType getDataType(){
		return dataType;
	}
}
