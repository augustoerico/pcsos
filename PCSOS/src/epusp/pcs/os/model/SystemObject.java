package epusp.pcs.os.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import epusp.pcs.os.model.attribute.IAttribute;
import epusp.pcs.os.model.attribute.types.BooleanAttribute;
import epusp.pcs.os.model.attribute.types.DateAttribute;
import epusp.pcs.os.model.attribute.types.FloatAttribute;
import epusp.pcs.os.model.attribute.types.IntegerAttribute;
import epusp.pcs.os.model.attribute.types.StringAttribute;
import epusp.pcs.os.model.attribute.types.arrays.BooleanArrayAttribute;
import epusp.pcs.os.model.attribute.types.arrays.DateArrayAttribute;
import epusp.pcs.os.model.attribute.types.arrays.FloatArrayAttribute;
import epusp.pcs.os.model.attribute.types.arrays.IntegerArrayAttribute;
import epusp.pcs.os.model.attribute.types.arrays.StringArrayAttribute;
import epusp.pcs.os.model.exception.AttributeCastException;

@PersistenceCapable(identityType=IdentityType.APPLICATION)
@Inheritance(strategy=InheritanceStrategy.SUBCLASS_TABLE)
@FetchGroup(name="all_system_object_attributes", members = { 
		@Persistent(name="stringAttributes"), @Persistent(name="stringArrayAttributes"),
		@Persistent(name="booleanAttributes"), @Persistent(name="booleanArrayAttributes"),
		@Persistent(name="dateAttributes"), @Persistent(name="dateArrayAttributes"),
		@Persistent(name="integerAttributes"), @Persistent(name="integerArrayAttributes"),
		@Persistent(name="floatAttributes"), @Persistent(name="floatArrayAttributes")})
public abstract class SystemObject implements Serializable {

	@NotPersistent
	private static final long serialVersionUID = 1L;
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String id;

	@Persistent
	private List<StringAttribute> stringAttributes = new ArrayList<StringAttribute>();
	@Persistent
	private List<StringArrayAttribute> stringArrayAttributes = new ArrayList<StringArrayAttribute>();

	@Persistent
	private List<BooleanAttribute> booleanAttributes = new ArrayList<BooleanAttribute>();
	@Persistent
	private List<BooleanArrayAttribute> booleanArrayAttributes = new ArrayList<BooleanArrayAttribute>();

	@Persistent
	private List<DateAttribute> dateAttributes = new ArrayList<DateAttribute>();
	@Persistent
	private List<DateArrayAttribute> dateArrayAttributes = new ArrayList<DateArrayAttribute>();

	@Persistent
	private List<IntegerAttribute> integerAttributes = new ArrayList<IntegerAttribute>();
	@Persistent
	private List<IntegerArrayAttribute> integerArrayAttributes = new ArrayList<IntegerArrayAttribute>();

	@Persistent
	private List<FloatAttribute> floatAttributes = new ArrayList<FloatAttribute>();
	@Persistent
	private List<FloatArrayAttribute> floatArrayAttributes = new ArrayList<FloatArrayAttribute>();

	@NotPersistent
	private HashMap<String, IAttribute> cache = new HashMap<String, IAttribute>();
	
	public String getId(){
		return id;
	}
	
	
	/*****/
	 
	 public void setId(String id){
		 this.id = id;
	 }
	 /***/

	public List<IAttribute> getAllAttributes(){
		List<IAttribute> attributes = new ArrayList<IAttribute>();
		attributes.addAll(stringAttributes);
		attributes.addAll(stringArrayAttributes);
		attributes.addAll(booleanAttributes);
		attributes.addAll(booleanArrayAttributes);
		attributes.addAll(dateAttributes);
		attributes.addAll(dateArrayAttributes);
		attributes.addAll(integerAttributes);
		attributes.addAll(integerArrayAttributes);
		return attributes;
	}
	
	public Set<String> getAllAttributeKeys(){
		if(cache.isEmpty()){
			buildCache();
		}
		
		return cache.keySet();
	}

	public void addAttribute(IAttribute attribute) throws AttributeCastException{

		if(cache.isEmpty()){
			buildCache();
		}

		switch(attribute.getDataType()){
		case BOOLEAN:
			if(cache.containsKey(attribute.getAttributeName()))
				((BooleanAttribute) cache.get(attribute.getAttributeName())).setValue((Boolean)attribute.getValue());
			else{
				cache.put(attribute.getAttributeName(), attribute);
				booleanAttributes.add((BooleanAttribute) attribute);
			}
			break;
		case BOOLEAN_ARRAY:
			if(cache.containsKey(attribute.getAttributeName()))
				((BooleanArrayAttribute) cache.get(attribute.getAttributeName())).setValue((Boolean[])attribute.getValue());
			else{
				cache.put(attribute.getAttributeName(), attribute);
				booleanArrayAttributes.add((BooleanArrayAttribute) attribute);
			}
			break;
		case DATE:
			if(cache.containsKey(attribute.getAttributeName()))
				((DateAttribute) cache.get(attribute.getAttributeName())).setValue((Date)attribute.getValue());
			else{
				cache.put(attribute.getAttributeName(), attribute);
				dateAttributes.add((DateAttribute) attribute);
			}
			break;
		case DATE_ARRAY:
			if(cache.containsKey(attribute.getAttributeName()))
				((DateArrayAttribute) cache.get(attribute.getAttributeName())).setValue((Date[])attribute.getValue());
			else{
				cache.put(attribute.getAttributeName(), attribute);
				dateArrayAttributes.add((DateArrayAttribute) attribute);
			}
			break;
		case FLOAT:
			if(cache.containsKey(attribute.getAttributeName()))
				((FloatAttribute) cache.get(attribute.getAttributeName())).setValue((Float)attribute.getValue());
			else{
				cache.put(attribute.getAttributeName(), attribute);
				floatAttributes.add((FloatAttribute) attribute);
			}
			break;
		case FLOAT_ARRAY:
			if(cache.containsKey(attribute.getAttributeName()))
				((FloatArrayAttribute) cache.get(attribute.getAttributeName())).setValue((Float[])attribute.getValue());
			else{
				cache.put(attribute.getAttributeName(), attribute);
				floatArrayAttributes.add((FloatArrayAttribute) attribute);
			}
			break;
		case INTEGER:
			if(cache.containsKey(attribute.getAttributeName()))
				((IntegerAttribute) cache.get(attribute.getAttributeName())).setValue((Integer)attribute.getValue());
			else{
				cache.put(attribute.getAttributeName(), attribute);
				integerAttributes.add((IntegerAttribute) attribute);
			}
			break;
		case INTERGER_ARRAY:
			if(cache.containsKey(attribute.getAttributeName()))
				((IntegerArrayAttribute) cache.get(attribute.getAttributeName())).setValue((Integer[])attribute.getValue());
			else{
				cache.put(attribute.getAttributeName(), attribute);
				integerArrayAttributes.add((IntegerArrayAttribute) attribute);
			}
			break;
		case STRING:
			if(cache.containsKey(attribute.getAttributeName()))
				((StringAttribute) cache.get(attribute.getAttributeName())).setValue((String)attribute.getValue());
			else{
				cache.put(attribute.getAttributeName(), attribute);
				stringAttributes.add((StringAttribute) attribute);
			}
			break;
		case STRING_ARRAY:
			if(cache.containsKey(attribute.getAttributeName()))
				((StringArrayAttribute) cache.get(attribute.getAttributeName())).setValue((String[])attribute.getValue());
			else{
				cache.put(attribute.getAttributeName(), attribute);
				stringArrayAttributes.add((StringArrayAttribute) attribute);
			}
			break;
		default:
			throw new AttributeCastException("Unknown Data Type: did you override the required module?");
		}
	}

	public void removeAttribute(String attributeName) throws AttributeCastException{

		if(cache.isEmpty()){
			buildCache();
		}

		if(cache.containsKey(attributeName)){
			IAttribute attribute = cache.get(attributeName);
			cache.remove(attributeName);

			switch(attribute.getDataType()){
			case BOOLEAN:
				booleanAttributes.remove(attribute);
				break;
			case BOOLEAN_ARRAY:
				booleanArrayAttributes.remove(attribute);
				break;
			case DATE:
				dateAttributes.remove(attribute);
				break;
			case DATE_ARRAY:
				dateArrayAttributes.remove(attribute);
				break;
			case FLOAT:
				floatAttributes.remove(attribute);
				break;
			case FLOAT_ARRAY:
				floatArrayAttributes.remove(attribute);
				break;
			case INTEGER:
				integerAttributes.remove(attribute);
				break;
			case INTERGER_ARRAY:
				integerArrayAttributes.remove(attribute);
				break;
			case STRING:
				stringAttributes.remove(attribute);
				break;
			case STRING_ARRAY:
				stringArrayAttributes.remove(attribute);
				break;
			default:
				throw new AttributeCastException("Unknown Data Type: did you override the required module?");
			}
		}
	}

	public IAttribute getAttribute(String attribute_name){

		if(cache.isEmpty()){
			buildCache();
		}

		return cache.get(attribute_name);
	}

	public Boolean contains(String attributeName){

		if(cache.isEmpty()){
			buildCache();
		}

		return cache.containsKey(attributeName);
	}

	private void buildCache(){
		for(IAttribute attribute : getAllAttributes()){
			try{
				cache.put(attribute.getAttributeName(), attribute);
			}catch(NullPointerException e){
				System.out.println("NullPointerException Error: did you overide getAllAttributes method properly?");
			}
		}
	}

	public List<StringAttribute> getStringAttributes(){
		return stringAttributes;
	}

	public List<StringArrayAttribute> getStringArrayAttributes(){
		return stringArrayAttributes;
	}

	public List<BooleanAttribute> getBooleanAttributes(){
		return booleanAttributes;
	}

	public List<BooleanArrayAttribute> getBooleanArrayAttributes(){
		return booleanArrayAttributes;
	}

	public List<DateAttribute> getDateAttributes(){
		return dateAttributes;
	}

	public List<DateArrayAttribute> getDateArrayAttributes(){
		return dateArrayAttributes;
	}

	public List<IntegerAttribute> getIntegerAttributes(){
		return integerAttributes;
	}

	public List<IntegerArrayAttribute> getIntegerArrayAttributes(){
		return integerArrayAttributes;
	}

	public List<FloatAttribute> getFloatAttributes(){
		return floatAttributes;
	}

	public List<FloatArrayAttribute> getFloatArrayAttributes(){
		return floatArrayAttributes;
	}

	/*
	 * Seen by Serializable
	 */
	public SystemObject(){
		super();
	}
}
