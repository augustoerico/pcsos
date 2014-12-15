package epusp.pcs.os.shared.model.vehicle.helicopter;

import epusp.pcs.os.shared.model.ICustomAttributes;
import epusp.pcs.os.shared.model.attribute.DataType;

public enum HelicopterCustomAttributes implements ICustomAttributes{
	CARMAKER("carmaker", DataType.STRING), MODEL("model", DataType.STRING), MANUFACTORING_YEAR("manufacturing_year", DataType.STRING);
	
	String attributeName;
	DataType dataType;
	
	HelicopterCustomAttributes(String attributeName, DataType dataType){
		this.attributeName = attributeName;
		this.dataType = dataType;
	}
	
	HelicopterCustomAttributes(){
	}
	
	@Override
	public String getAttributeName(){
		return attributeName;
	}
	
	public DataType getDataType(){
		return dataType;
	}
}