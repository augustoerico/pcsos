package epusp.pcs.os.shared.model.person.user.superuser;

import epusp.pcs.os.shared.model.ICustomAttributes;
import epusp.pcs.os.shared.model.attribute.DataType;

public enum SuperUserCustomAttributes implements ICustomAttributes{
	IDADE("idade", DataType.INTEGER);
	
	String attributeName;
	DataType dataType;
	
	SuperUserCustomAttributes(String attributeName, DataType dataType){
		this.attributeName = attributeName;
		this.dataType = dataType;
	}
	
	SuperUserCustomAttributes(){
	}
	
	@Override
	public String getAttributeName(){
		return attributeName;
	}
	
	public DataType getDataType(){
		return dataType;
	}
}

