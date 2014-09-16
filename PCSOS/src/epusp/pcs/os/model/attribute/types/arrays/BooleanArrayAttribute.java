package epusp.pcs.os.model.attribute.types.arrays;

import java.util.Arrays;
import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import epusp.pcs.os.model.attribute.DataType;

@PersistenceCapable(identityType=IdentityType.APPLICATION, detachable="true")
public class BooleanArrayAttribute extends ArrayAttribute {
	
	@NotPersistent
	private static final long serialVersionUID = 1L;
	
	@Persistent
	private Boolean[] values;

	public BooleanArrayAttribute(Boolean[] values, String attributeName){
		super(attributeName);
		this.values = values;
	}
	
	public BooleanArrayAttribute(List<Boolean> values, String attributeName){
		super(attributeName);
		this.values = (Boolean[]) values.toArray();
	}
	
	public void setValue(Boolean[] values) {
		this.values = values;
	}
	
	@Override
	public DataType getDataType() {
		return DataType.BOOLEAN_ARRAY;
	}
	
//	@Override
	public List<Boolean> getValuesAsList() {
		return Arrays.asList(values);
	}

	@Override
	public Boolean[] getValue(){
		return values;
	}
	
	@Override
	public String toString() {
		return Arrays.toString(values);
	}

	/*
	 * Seen by IsSerializable
	 */
	public BooleanArrayAttribute(){
		super();
	}
}
