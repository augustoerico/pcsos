package epusp.pcs.os.shared.model.vehicle.motorcycle;

import epusp.pcs.os.shared.model.ICustomAttributes;
import epusp.pcs.os.shared.model.attribute.DataType;

public enum MotorcycleCustomAttributes implements ICustomAttributes{
	IDADE("idade", DataType.INTEGER), COR_DOS_OLHOS("cor_dos_olhos", DataType.STRING);
	
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
