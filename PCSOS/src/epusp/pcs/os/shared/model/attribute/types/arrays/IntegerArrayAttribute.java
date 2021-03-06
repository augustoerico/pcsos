package epusp.pcs.os.shared.model.attribute.types.arrays;

import java.util.Arrays;
import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import epusp.pcs.os.shared.model.attribute.DataType;

@PersistenceCapable(identityType=IdentityType.APPLICATION, detachable="true")
public class IntegerArrayAttribute extends ArrayAttribute {
	
	@NotPersistent
	private static final long serialVersionUID = 1L;
	
	@Persistent
	private Integer[] values;

	public IntegerArrayAttribute(Integer[] values, String attributeName){
		super(attributeName);
		this.values = values;
	}
	
	public IntegerArrayAttribute(List<Integer> values, String attributeName){
		super(attributeName);
		this.values = values.toArray(new Integer[values.size()]);
	}
	
	public void setValue(Integer[] values) {
		this.values = values;
	}
	
	@Override
	public DataType getDataType() {
		return DataType.INTERGER_ARRAY;
	}

	public List<Integer> getValuesAsList() {
		return Arrays.asList(values);
	}

	@Override
	public Integer[] getValue(){
		return values;
	}
	
	@Override
	public String toString() {
		String output = "";
		for(int i = 0; i < values.length; i++){
			output = output.concat(String.valueOf(values[i])).concat(", ");
		}
		return output;
	}

	/*
	 * Seen by IsSerializable
	 */
	public IntegerArrayAttribute(){
		super();
	}
}
