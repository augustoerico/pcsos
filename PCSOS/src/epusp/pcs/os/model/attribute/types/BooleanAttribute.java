package epusp.pcs.os.model.attribute.types;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import epusp.pcs.os.model.attribute.Attribute;
import epusp.pcs.os.model.attribute.DataType;

@PersistenceCapable(identityType=IdentityType.APPLICATION, detachable="true")
public class BooleanAttribute extends Attribute {
	
	@NotPersistent
	private static final long serialVersionUID = 1L;

	@Persistent
	private Boolean value;

	public BooleanAttribute(Boolean value, String attributeName){
		super(attributeName);
		this.value = value;
	}
	
	public void setValue(Boolean value) {
		this.value = value;
	}

	@Override
	public DataType getDataType() {
		return DataType.BOOLEAN;
	}
	
	@Override
	public Boolean getValue(){
		return value;
	}
	
	@Override
	public String toString() {
		return String.valueOf(value);
	}

	/*
	 * Seen by IsSerializable
	 */
	public BooleanAttribute(){
		super();
	}
}
