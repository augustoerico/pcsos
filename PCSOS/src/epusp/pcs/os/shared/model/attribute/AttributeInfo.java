package epusp.pcs.os.shared.model.attribute;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType=IdentityType.APPLICATION, detachable="true")
public class AttributeInfo implements Serializable {
	
	@NotPersistent
	private static final long serialVersionUID = 1L;

	@Persistent
	@PrimaryKey
	private String attributeName;
	
	@Persistent
	private String label;
	
	@Persistent
	private Boolean isRequired = false;
	
	@Persistent
	private Boolean isArray = false; 

	@Persistent
	private List<String> isVisableAt = new ArrayList<String>();

	@Persistent
	private Category category;

	public AttributeInfo(String attributeName, Category category, String label){
		this.attributeName = attributeName;
		this.category = category;
		this.label = label;
	}

	public String getAttributeName(){
		return attributeName;
	}

	public String getLabel(){
		return label;
	}

	public Category getCategory(){
		return category;
	}

	public Boolean isArray(){
		return isArray;
	}

	public void setIsArray(Boolean isArray){
		isArray = this.isArray;
	}
	
	public Boolean isRequired(){
		return isRequired;
	}

	public void setIsRequired(Boolean isRequired){
		this.isRequired = isRequired;
	}

	public Boolean isVisable(String className){
		if(isVisableAt.isEmpty()){
			return true;
		}else{
			for(String item : isVisableAt)
				if(item.equals(className))
					return true;
			return false;
		}
	}
	
	public void addIsVisibleAt(String className){
		isVisableAt.add(className);
	}

	public void makeAlwaysVisible(){
		isVisableAt.clear();
	}
	
	public void unableVisibleAt(String className){
		isVisableAt.remove(className);
	}
	
	public List<String> getAllVisibleClasses(){
		return isVisableAt;
	}
	
	/*
	 * Seen by IsSerializable
	 */
	public AttributeInfo(){
		super();
	}
}
