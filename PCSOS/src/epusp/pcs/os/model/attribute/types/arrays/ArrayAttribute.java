package epusp.pcs.os.model.attribute.types.arrays;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;

import epusp.pcs.os.model.attribute.Attribute;

@PersistenceCapable(identityType=IdentityType.APPLICATION)
@Inheritance(strategy=InheritanceStrategy.SUBCLASS_TABLE)
public abstract class ArrayAttribute extends Attribute {

	@NotPersistent
	private static final long serialVersionUID = 1L;

	protected ArrayAttribute(String attributeName) {
		super(attributeName);
	}

//	public abstract List<?> getValuesAsList();
	
	/*
	 * Seen by IsSerializable
	 */
	public ArrayAttribute(){
		super();
	}

}
