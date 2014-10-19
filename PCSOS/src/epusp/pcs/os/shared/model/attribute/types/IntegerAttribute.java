package epusp.pcs.os.shared.model.attribute.types;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import epusp.pcs.os.shared.model.attribute.Attribute;
import epusp.pcs.os.shared.model.attribute.DataType;

@PersistenceCapable(identityType=IdentityType.APPLICATION, detachable="true")
public class IntegerAttribute extends Attribute {	

	@NotPersistent
	private static final long serialVersionUID = 1L;
	
	@Persistent
	private Integer value;
	
	public IntegerAttribute(Integer value, String attributeName){
		super(attributeName);
		this.value = value;
	}
	
	public void setValue(Integer value){
		this.value = value;
	}
	
	@Override
	public Integer getValue(){
		return value;
	}
	
	@Override
	public DataType getDataType(){
		return DataType.INTEGER;
	}
	
	@Override
	public String toString(){
		return String.valueOf(value);
	}
	
	/*
	 * Seen by IsSerializable
	 */
	public IntegerAttribute(){
		super();
	}
	
}
