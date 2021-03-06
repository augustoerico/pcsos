package epusp.pcs.os.shared.model.attribute.types.arrays;

import java.util.Arrays;
import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import epusp.pcs.os.shared.model.attribute.DataType;

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
		this.values = values.toArray(new String[values.size()]);
	}
	
	public void setValue(String[] values) {
		this.values = values;
	}
	
	@Override
	public DataType getDataType() {
		return DataType.STRING_ARRAY;
	}
	
	public List<String> getValuesAsList() {
		return Arrays.asList(values);
	}

	@Override
	public String[] getValue(){
		return values;
	}
	
	@Override
	public String toString() {
		String output = "";
		for(int i = 0; i < values.length; i++){
			output = output.concat(values[i]).concat(", ");
		}
		return output;
	}

	/*
	 * Seen by IsSerializable
	 */
	public StringArrayAttribute(){
		super();
	}

}
