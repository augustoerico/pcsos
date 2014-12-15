package epusp.pcs.os.shared.model.vehicle.car;

import epusp.pcs.os.shared.model.ICustomAttributes;
import epusp.pcs.os.shared.model.attribute.DataType;

public enum CarCustomAttributes implements ICustomAttributes{
	CARMAKER("carmaker", DataType.STRING), MODEL("model", DataType.STRING), MANUFACTORING_YEAR("manufacturing_year", DataType.STRING),
	FUEL("fuel", DataType.STRING), SHIELD("shield", DataType.BOOLEAN);
	
	String attributeName;
	DataType dataType;
	
	CarCustomAttributes(String attributeName, DataType dataType){
		this.attributeName = attributeName;
		this.dataType = dataType;
	}
	
	CarCustomAttributes(){
	}
	
	@Override
	public String getAttributeName(){
		return attributeName;
	}
	
	public DataType getDataType(){
		return dataType;
	}
}