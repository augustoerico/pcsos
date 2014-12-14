package epusp.pcs.os.shared.model.person.user.admin;

import epusp.pcs.os.shared.model.ICustomAttributes;
import epusp.pcs.os.shared.model.attribute.DataType;

public enum AdminCustomAttributes implements ICustomAttributes{
	IDADE("idade", DataType.INTEGER), COR_DOS_OLHOS("cor_dos_olhos", DataType.STRING);
	
	String attributeName;
	DataType dataType;
	
	AdminCustomAttributes(String attributeName, DataType dataType){
		this.attributeName = attributeName;
		this.dataType = dataType;
	}
	
	AdminCustomAttributes(){
	}
	
	@Override
	public String getAttributeName(){
		return attributeName;
	}
	
	public DataType getDataType(){
		return dataType;
	}
}
