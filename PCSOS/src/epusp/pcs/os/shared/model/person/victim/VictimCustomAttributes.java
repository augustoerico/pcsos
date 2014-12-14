package epusp.pcs.os.shared.model.person.victim;

import epusp.pcs.os.shared.model.ICustomAttributes;
import epusp.pcs.os.shared.model.attribute.DataType;

public enum VictimCustomAttributes implements ICustomAttributes{
	IDADE("idade", DataType.INTEGER), COR_DOS_OLHOS("cor_dos_olhos", DataType.STRING);
	
	String attributeName;
	DataType dataType;
	
	VictimCustomAttributes(String attributeName, DataType dataType){
		this.attributeName = attributeName;
		this.dataType = dataType;
	}
	
	VictimCustomAttributes(){
	}
	
	@Override
	public String getAttributeName(){
		return attributeName;
	}
	
	public DataType getDataType(){
		return dataType;
	}
}
