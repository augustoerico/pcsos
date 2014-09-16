package epusp.pcs.os.model.attribute.types.arrays;

import java.util.Arrays;
import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import epusp.pcs.os.model.attribute.DataType;

@PersistenceCapable(identityType=IdentityType.APPLICATION, detachable="true")
public class StringArrayAttribute extends ArrayAttribute {
	
	@NotPersistent
	private static final long serialVersionUID = 1L;
	
	@Persistent
	private String[] values;

	public StringArrayAttribute(String[] values, String attributeName){
		super(attributeName);
		this.values = values;
	}
	
	public StringArrayAttribute(List<String> values, String attributeName){
		super(attributeName);
		this.values = (String[]) values.toArray();
	}
	
	public void setValue(String[] values) {
		this.values = values;
	}
	
	@Override
	public DataType getDataType() {
		return DataType.STRING_ARRAY;
	}
	
//	@Override
	public List<String> getValuesAsList() {
		return Arrays.asList(values);
	}

	@Override
	public String[] getValue(){
		return values;
	}
	
	@Override
	public String toString() {
		return Arrays.toString(values);
	}

	/*
	 * Seen by IsSerializable
	 */
	public StringArrayAttribute(){
		super();
	}

}
