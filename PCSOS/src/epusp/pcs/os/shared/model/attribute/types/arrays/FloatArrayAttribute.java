package epusp.pcs.os.shared.model.attribute.types.arrays;

import java.util.Arrays;
import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import epusp.pcs.os.shared.model.attribute.DataType;

@PersistenceCapable(identityType=IdentityType.APPLICATION, detachable="true")
public class FloatArrayAttribute extends ArrayAttribute {
	
	@NotPersistent
	private static final long serialVersionUID = 1L;
	
	@Persistent
	private Float[] values;

	public FloatArrayAttribute(Float[] values, String attributeName){
		super(attributeName);
		this.values = values;
	}
	
	public FloatArrayAttribute(List<Float> values, String attributeName){
		super(attributeName);
		this.values = values.toArray(new Float[values.size()]);
	}
	
	public void setValue(Float[] values) {
		this.values = values;
	}
	
	@Override
	public DataType getDataType() {
		return DataType.FLOAT_ARRAY;
	}
	
	public List<Float> getValuesAsList() {
		return Arrays.asList(values);
	}

	@Override
	public Float[] getValue(){
		return values;
	}
	
	@Override
	public String toString() {
		return Arrays.toString(values);
	}

	/*
	 * Seen by IsSerializable
	 */
	public FloatArrayAttribute(){
		super();
	}
}
