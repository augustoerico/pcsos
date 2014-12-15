package epusp.pcs.os.shared.model.person.user.admin;

import epusp.pcs.os.shared.model.ICustomAttributes;
import epusp.pcs.os.shared.model.attribute.DataType;

public enum AdminCustomAttributes implements ICustomAttributes{
	ADDRESS("address", DataType.STRING), ZIP_CODE("zip_code", DataType.STRING),
	RG("rg", DataType.STRING), CPF("cpf", DataType.STRING), BIRTHDAY("birthday", DataType.DATE), CONTACT("contact", DataType.STRING);
	
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
