package epusp.pcs.os.model.attribute.types.arrays;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import epusp.pcs.os.model.attribute.DataType;

@PersistenceCapable(identityType=IdentityType.APPLICATION, detachable="true")
public class DateArrayAttribute extends ArrayAttribute {
	
	@NotPersistent
	private static final long serialVersionUID = 1L;
	
	@Persistent
	private Date[] values;
	
	public DateArrayAttribute(Date[] values, String attributeName){
		super(attributeName);
		this.values = values;
	}
	
	public DateArrayAttribute(List<Date> values, String attributeName){
		super(attributeName);
		this.values = (Date[]) values.toArray();
	}
	
	public void setValue(Date[] values) {
		this.values = values;
	}
	
	@Override
	public DataType getDataType() {
		return DataType.DATE_ARRAY;
	}

	@Override
	public Object getValue() {
		return values;
	}

//	@Override
	public List<Date> getValuesAsList() {
		return Arrays.asList(values);
	}
	
	@Override
	public String toString() {
		return Arrays.toString(values);
	}
	
	/*
	 * Seen by Serializable
	 */
	public DateArrayAttribute(){
		super();
	}
}