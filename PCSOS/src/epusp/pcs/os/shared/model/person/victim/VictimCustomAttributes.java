package epusp.pcs.os.shared.model.person.victim;

import epusp.pcs.os.shared.model.ICustomAttributes;
import epusp.pcs.os.shared.model.attribute.DataType;

public enum VictimCustomAttributes implements ICustomAttributes{
	EMERGENCY_CONTACT("emergency_contact", DataType.STRING_ARRAY), BIRTHDAY("birthday", DataType.DATE),
	HEIGHT("height", DataType.FLOAT), WEIGHT("weight", DataType.FLOAT), MARITAL_STATUS("victim_marital_status", DataType.STRING),
	EYE_COLOR("eye_color", DataType.STRING), ADDRESS("victim_address", DataType.STRING), ZIP_CODE("victim_zip_code", DataType.STRING),
	DIABETIC("diabetic", DataType.BOOLEAN), CARDIO("cardio", DataType.BOOLEAN), BLOOD_TYPE("blood_type", DataType.STRING), CARMAKER("carmaker", DataType.STRING),
	MODEL("model", DataType.STRING), PLATE("license_plate", DataType.STRING), CAR_COLOR("car_color", DataType.STRING), CONTACT("contact", DataType.STRING),
	RG("rg", DataType.STRING), CPF("cpf", DataType.STRING);
	
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
