package epusp.pcs.os.shared.model.attribute.types;

import java.util.Date;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;

import epusp.pcs.os.shared.model.attribute.Attribute;
import epusp.pcs.os.shared.model.attribute.DataType;

@PersistenceCapable(identityType=IdentityType.APPLICATION, detachable="true")
public class DateAttribute extends Attribute {
	
	@NotPersistent
	private static final long serialVersionUID = 1L;
	
	@Persistent
	private Date value;
	
	public DateAttribute(Date value, String attributeName){
		super(attributeName);
		this.value = value;
	}
	
	public void setValue(Date date){
		this.value = date;
	}
	
	@Override
	public Date getValue(){
		return value;
	}
	
	@Override
	public DataType getDataType() {
		return DataType.DATE;
	}
	
	@Override
	public String toString() {
		if(value != null){
			return String.valueOf(value);
		}else return "Desconhecido/Unknown";
	}

	
	/*
	 * Seen by IsSerializable
	 */
	public DateAttribute(){
		super();
	}
	
}
