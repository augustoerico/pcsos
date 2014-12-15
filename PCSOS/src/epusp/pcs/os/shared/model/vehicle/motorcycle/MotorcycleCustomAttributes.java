package epusp.pcs.os.shared.model.vehicle.motorcycle;

import epusp.pcs.os.shared.model.ICustomAttributes;
import epusp.pcs.os.shared.model.attribute.DataType;

public enum MotorcycleCustomAttributes implements ICustomAttributes{
	CARMAKER("carmaker", DataType.STRING), MODEL("model", DataType.STRING), MANUFACTORING_YEAR("manufacturing_year", DataType.STRING),
	FUEL("fuel", DataType.STRING);
	
	String attributeName;
	DataType dataType;
	
	
	MotorcycleCustomAttributes(String attributeName, DataType dataType){
		this.attributeName = attributeName;
		this.dataType = dataType;
	}
	
	MotorcycleCustomAttributes(){
	}
	
	@Override
	public String getAttributeName(){
		return attributeName;
	}
	
	public DataType getDataType(){
		return dataType;
	}
}
