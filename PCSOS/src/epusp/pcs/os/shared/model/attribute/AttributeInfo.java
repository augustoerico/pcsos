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
	private List<String> locales = new ArrayList<String>();
	
	@Persistent
	private List<String> labels = new ArrayList<String>();
	
	@Persistent
	private Boolean isRequired = false;
	
	@Persistent
	private Boolean isArray = false; 
	
	@Persistent
	private Boolean isEditable = true;

	@Persistent
	private List<String> isVisableAt = new ArrayList<String>();

	@Persistent
	private Category category;
	
	@Persistent
	private DataType dataType;

	public AttributeInfo(String attributeName, Category category, DataType dataType){
		this.attributeName = attributeName;
		this.category = category;
		this.dataType = dataType;
	}

	public String getAttributeName(){
		return attributeName;
	}

	public String getLabel(String locale){
		int i = locales.indexOf(locale);
		if(i >= 0)
			return labels.get(i);
		else{
			i = locales.indexOf("en");
			if(i >= 0)
				return labels.get(i);
			else
				return "";
		}
	}
	
	public void addLocale(String locale, String label){
		labels.add(label);
		locales.add(locale);
	}
	
	public void removeLocale(String locale){
		int i = locales.indexOf(locale);
		if(i >= 0){
			labels.remove(i);
			locales.remove(i);
		}
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
	
	public DataType getDataType(){
		return dataType;
	}
	
	public void setEditable(Boolean isEditable){
		this.isEditable = isEditable;
	}
	
	public Boolean isEditable(){
		return isEditable;
	}
	
	/*
	 * Seen by IsSerializable
	 */
	public AttributeInfo(){
		super();
	}
}
