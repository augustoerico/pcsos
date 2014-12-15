package epusp.pcs.os.shared.model.person.user.agent;

import epusp.pcs.os.shared.model.ICustomAttributes;
import epusp.pcs.os.shared.model.attribute.DataType;

public enum AgentCustomAttributes implements ICustomAttributes{
	ADDRESS("address", DataType.STRING), ZIP_CODE("zip_code", DataType.STRING), RG("rg", DataType.STRING), CPF("cpf", DataType.STRING),
	BITHDATE("birthday", DataType.DATE), MARITAL_STATUS("marital_status", DataType.STRING), BLOOD_TYPE("blood_type", DataType.STRING),
	DIABETIC("diabetic", DataType.BOOLEAN), CARDIO("cardio", DataType.BOOLEAN), CONTACT("contact", DataType.STRING), WARNAME("warname", DataType.STRING),
	EMERGENCY_CONTACT("emergency_contact", DataType.STRING_ARRAY);
	
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
