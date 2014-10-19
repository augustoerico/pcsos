package epusp.pcs.os.shared.model.attribute.types;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import epusp.pcs.os.shared.model.attribute.Attribute;
import epusp.pcs.os.shared.model.attribute.DataType;

@PersistenceCapable(identityType=IdentityType.APPLICATION, detachable="true")
public class StringAttribute extends Attribute {
	
	@NotPersistent
	private static final long serialVersionUID = 1L;
	
	@Persistent
	private String value;
	
	public StringAttribute(String value, String attributeName){
		super(attributeName);
		this.value = value;
	}
	
	public void setValue(String value){
		this.value = value;
	}
	
	@Override
	public DataType getDataType() {
		return DataType.STRING;
	}
	
	@Override
	public String getValue(){
		return value;
	}
	
	@Override
	public String toString() {
		return value;
	}
	
	/*
	 * Seen by IsSerializable
	 */
	public StringAttribute(){
		super();
	}
}
