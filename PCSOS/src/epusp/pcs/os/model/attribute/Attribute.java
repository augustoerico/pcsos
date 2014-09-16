package epusp.pcs.os.model.attribute;

import java.io.Serializable;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType=IdentityType.APPLICATION)
@Inheritance(strategy=InheritanceStrategy.SUBCLASS_TABLE)
public abstract class Attribute implements Serializable, IAttribute {
	
	@NotPersistent
	private static final long serialVersionUID = 1L;
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String id;
	
	@Persistent
	private String attributeName;
	
	protected Attribute(String attributeName){
		this.attributeName = attributeName;
	}
	
	@Override
	public String getAttributeName(){
		return attributeName;
	}
	
	@Override
	public boolean equals(Object obj) {
		return attributeName.equals(obj);
	}
	
	@Override
	public abstract Object getValue();
	
	@Override
	public abstract DataType getDataType();
	
	/*
	 * Seen by IsSerializable
	 */
	public Attribute(){
		super();
	}
	
}
