package epusp.pcs.os.server.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Properties;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import epusp.pcs.os.shared.model.SystemObject;
import epusp.pcs.os.shared.model.attribute.AttributeInfo;
import epusp.pcs.os.shared.model.attribute.Category;
import epusp.pcs.os.shared.model.attribute.DataType;
import epusp.pcs.os.shared.model.attribute.types.BooleanAttribute;
import epusp.pcs.os.shared.model.attribute.types.DateAttribute;
import epusp.pcs.os.shared.model.attribute.types.FloatAttribute;
import epusp.pcs.os.shared.model.attribute.types.IntegerAttribute;
import epusp.pcs.os.shared.model.attribute.types.StringAttribute;
import epusp.pcs.os.shared.model.attribute.types.arrays.BooleanArrayAttribute;
import epusp.pcs.os.shared.model.attribute.types.arrays.DateArrayAttribute;
import epusp.pcs.os.shared.model.attribute.types.arrays.FloatArrayAttribute;
import epusp.pcs.os.shared.model.attribute.types.arrays.IntegerArrayAttribute;
import epusp.pcs.os.shared.model.attribute.types.arrays.StringArrayAttribute;
import epusp.pcs.os.shared.model.exception.AttributeCastException;
import epusp.pcs.os.shared.model.licence.DrivingCategories;
import epusp.pcs.os.shared.model.licence.DrivingLicense;
import epusp.pcs.os.shared.model.licence.HelicopterLicense;
import epusp.pcs.os.shared.model.licence.HelicopterLicenseTypes;
import epusp.pcs.os.shared.model.person.user.AvailableLanguages;
import epusp.pcs.os.shared.model.person.user.admin.Admin;
import epusp.pcs.os.shared.model.person.user.admin.AdminCustomAttributes;
import epusp.pcs.os.shared.model.person.user.agent.Agent;
import epusp.pcs.os.shared.model.person.user.agent.AgentCustomAttributes;
import epusp.pcs.os.shared.model.person.user.monitor.Monitor;
import epusp.pcs.os.shared.model.person.user.monitor.MonitorCustomAttributes;
import epusp.pcs.os.shared.model.person.user.superuser.SuperUser;
import epusp.pcs.os.shared.model.person.user.superuser.SuperUserCustomAttributes;
import epusp.pcs.os.shared.model.person.victim.Victim;
import epusp.pcs.os.shared.model.person.victim.VictimCustomAttributes;
import epusp.pcs.os.shared.model.vehicle.Priority;
import epusp.pcs.os.shared.model.vehicle.car.Car;
import epusp.pcs.os.shared.model.vehicle.car.CarCustomAttributes;
import epusp.pcs.os.shared.model.vehicle.helicopter.Helicopter;
import epusp.pcs.os.shared.model.vehicle.helicopter.HelicopterCustomAttributes;
import epusp.pcs.os.shared.model.vehicle.motorcycle.Motorcycle;
import epusp.pcs.os.shared.model.vehicle.motorcycle.MotorcycleCustomAttributes;

public class Loader {

	private String path = "WEB-INF/configuration";

	private List<String> errors = new ArrayList<String>();

	DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);

	public Loader(){
		super();
	}

	public Loader(String path){
		this.path = path;
	}

	public void load(PersistenceManagerFactory pmf) throws IOException, LoaderException{
		Date time = new Date(System.currentTimeMillis());
		System.out.println("Configuraton loaded started on " + time);

		final File folder = new File(path);

		for(final File file : folder.listFiles()){
			System.out.println("Reading " + file.getName());
			if(file.isFile()){
				Properties prop = new Properties();

				InputStream inputStream = new FileInputStream(file);

				prop.load(inputStream);

				String type = prop.getProperty("type");

				if(type != null){
					try{
						LoaderObjectTypes objectType = LoaderObjectTypes.valueOf(type);
						switch (objectType) {
						case Admin:
							if(!prop.containsKey("name") || !prop.containsKey("surname") || !prop.containsKey("email"))
								errors.add("Missing mandatory attribute for " + type + " at  " + file.getName());
							else{
								try{
									Admin admin = buildAdmin(prop);
									persistObject(admin, pmf.getPersistenceManager());
								}catch(AttributeCastException | ParseException e){
									errors.add(e.getMessage() + " at " + file.getName());
								}	
							}
							break;
						case Agent:
							if(!prop.containsKey("name") || !prop.containsKey("surname") || !prop.containsKey("email"))
								errors.add("Missing mandatory attribute for " + type + " at  " + file.getName());
							else{
								try{
									Agent agent = buildAgent(prop);
									persistObject(agent, pmf.getPersistenceManager());
								}catch(IOException | AttributeCastException | ParseException | MissingAttributeException e){
									errors.add(e.getMessage() + " at " + file.getName());
								}	
							}
							break;
						case AttributeInfo:
							if(!prop.containsKey("attributeName") || !prop.containsKey("category") || !prop.containsKey("dataType"))
								errors.add("Missing mandatory attribute for " + type + " at  " + file.getName());
							else
								persistObject(buildAttributeInfo(prop), pmf.getPersistenceManager());
							break;
						case Car:
							if(!prop.containsKey("idTag") || !prop.containsKey("plate"))
								errors.add("Missing mandatory attribute for " + type + " at  " + file.getName());
							else{
								try{
									Car car = buildCar(prop);
									persistObject(car, pmf.getPersistenceManager());
								}catch(AttributeCastException | ParseException e){
									errors.add(e.getMessage() + " at " + file.getName());
								}	
							}
							break;
						case Helicopter:
							if(!prop.containsKey("idTag"))
								errors.add("Missing mandatory attribute for " + type + " at  " + file.getName());
							else{
								try{
									Helicopter helicopter = buildHelicopter(prop);
									persistObject(helicopter, pmf.getPersistenceManager());
								}catch(AttributeCastException | ParseException e){
									errors.add(e.getMessage() + " at " + file.getName());
								}	
							}
							break;
						case Monitor:
							if(!prop.containsKey("name") || !prop.containsKey("surname") || !prop.containsKey("email"))
								errors.add("Missing mandatory attribute for " + type + " at  " + file.getName());
							else{
								try{
									Monitor monitor = buildMonitor(prop);
									persistObject(monitor, pmf.getPersistenceManager());
								}catch(AttributeCastException | ParseException e){
									errors.add(e.getMessage() + " at " + file.getName());
								}	
							}
							break;
						case Motorcycle:
							if(!prop.containsKey("idTag") || !prop.containsKey("plate"))
								errors.add("Missing mandatory attribute for " + type + " at  " + file.getName());
							else{
								try{
									Motorcycle motorcycle = buildMotorcycle(prop);
									persistObject(motorcycle, pmf.getPersistenceManager());
								}catch(AttributeCastException | ParseException e){
									errors.add(e.getMessage() + " at " + file.getName());
								}	
							}
							break;
						case SuperUser:
							if(!prop.containsKey("name") || !prop.containsKey("surname") || !prop.containsKey("email"))
								errors.add("Missing mandatory attribute for " + type + " at  " + file.getName());
							else{
								try{
									SuperUser superUser = buildSuperUser(prop);
									persistObject(superUser, pmf.getPersistenceManager());
								}catch(AttributeCastException | ParseException e){
									errors.add(e.getMessage() + " at " + file.getName());
								}	
							}
							break;
						case Victim:
							if(!prop.containsKey("name") || !prop.containsKey("surname") || !prop.containsKey("email"))
								errors.add("Missing mandatory attribute for " + type + " at  " + file.getName());
							else{
								try{
									Victim victim = buildVictim(prop);
									persistObject(victim, pmf.getPersistenceManager());
								}catch(AttributeCastException | ParseException e){
									errors.add(e.getMessage() + " at " + file.getName());
								}	
							}
							break;
						default:
							break;
						}
					}catch(IllegalArgumentException e){}
				}else{
					errors.add("Missing mandatory attribute type at " + file.getName());
				}
			}
		}

		if(!errors.isEmpty()){
			String message = "";
			for(String error : errors){
				message = message.concat(error).concat("\n");
			}
			throw new LoaderException(message);
		}

		time = new Date(System.currentTimeMillis());
		System.out.println("Configuraton loaded ended on " + time);
	}

	private AttributeInfo buildAttributeInfo(Properties prop){
		String attributeName = prop.getProperty("attributeName");
		Category category = Category.valueOf(prop.getProperty("category"));
		DataType dataType = DataType.valueOf(prop.getProperty("dataType").toUpperCase());

		Boolean isRequired = null;
		if(prop.containsKey("isRequired"))
			isRequired = Boolean.valueOf(prop.getProperty("isRequired"));

		Boolean isArray = null;
		if(prop.containsKey("isArray"))
			isArray = Boolean.valueOf(prop.getProperty("isArray"));

		Boolean isEditable = null;
		if(prop.containsKey("isEditable"))
			isEditable = Boolean.valueOf(prop.getProperty("isEditable"));

		List<String> isVisableAt = new ArrayList<String>();
		if(prop.containsKey("isVisableAt")){
			isVisableAt.addAll(Arrays.asList(prop.getProperty("isVisableAt").replaceAll("^\\s*|\\s*$", "").split("\\s*,\\s*")));
		}

		HashMap<String, String> labels = new HashMap<String, String>();
		for(Entry<Object, Object> entry : prop.entrySet()){
			String key = (String) entry.getKey();
			if(key.matches("label_[A-Za-z]+[-_]?[A-Za-z]*")){
				String value = (String) entry.getValue();
				String locale = key.replaceAll("label_", "");
				labels.put(locale, value);
			}
		}

		AttributeInfo attributeInfo = new AttributeInfo(attributeName, category, dataType);
		if(isRequired != null)
			attributeInfo.setIsRequired(isRequired);

		if(isArray != null)
			attributeInfo.setIsArray(isArray);

		if(isEditable != null)
			attributeInfo.setEditable(isEditable);

		for(String isVisable : isVisableAt){
			attributeInfo.addIsVisibleAt(isVisable);
		}

		for(Entry<String, String> label : labels.entrySet()){
			attributeInfo.addLocale(label.getKey(), label.getValue());
		}
		return attributeInfo;
	}

	private Motorcycle buildMotorcycle(Properties prop) throws AttributeCastException, ParseException{
		//Vehicle attributes
		String idTag = prop.getProperty("idTag");
		String plate = prop.getProperty("plate");

		Priority priority = null;
		if(prop.contains("priority"))
			priority = Priority.valueOf(prop.getProperty("priority").toUpperCase());
		
		String imageURL = null;
		if(prop.containsKey("imageURL"))
			imageURL = prop.getProperty("imageURL");

		Boolean isActive = null;
		if(prop.containsKey("isActive"))
			isActive = Boolean.valueOf(prop.getProperty("isActive"));
		
		//Start building Motorcycle
		Motorcycle motorcycle = new Motorcycle(idTag, plate);
		
		if(priority != null)
			motorcycle.setPrioraty(priority);
		
		if(imageURL != null)
			motorcycle.setImageURL(imageURL);
		
		if(isActive != null)
			motorcycle.setIsActive(isActive);

		//Motorcycle Custom Attributes
		for(MotorcycleCustomAttributes attribute : MotorcycleCustomAttributes.values()){
			String attributeName = attribute.getAttributeName();
			if(prop.containsKey(attributeName)){
				String value = prop.getProperty(attributeName);
				try{
					addCustomAttribute(attributeName, attribute.getDataType(), value, motorcycle);
				}catch(AttributeCastException | ParseException e){
					throw e;
				}
			}
		}
		
		return motorcycle;
	}
	
	private Car buildCar(Properties prop) throws AttributeCastException, ParseException{
		//Vehicle attributes
		String idTag = prop.getProperty("idTag");
		String plate = prop.getProperty("plate");

		Priority priority = null;
		if(prop.contains("priority"))
			priority = Priority.valueOf(prop.getProperty("priority").toUpperCase());
		
		String imageURL = null;
		if(prop.containsKey("imageURL"))
			imageURL = prop.getProperty("imageURL");

		Boolean isActive = null;
		if(prop.containsKey("isActive"))
			isActive = Boolean.valueOf(prop.getProperty("isActive"));
		
		//Start building Motorcycle
		Car car = new Car(idTag, plate);
		
		if(priority != null)
			car.setPrioraty(priority);
		
		if(imageURL != null)
			car.setImageURL(imageURL);
		
		if(isActive != null)
			car.setIsActive(isActive);

		//Car Custom Attributes
		for(CarCustomAttributes attribute : CarCustomAttributes.values()){
			String attributeName = attribute.getAttributeName();
			if(prop.containsKey(attributeName)){
				String value = prop.getProperty(attributeName);
				try{
					addCustomAttribute(attributeName, attribute.getDataType(), value, car);
				}catch(AttributeCastException | ParseException e){
					throw e;
				}
			}
		}
		
		return car;
	}

	private Helicopter buildHelicopter(Properties prop) throws AttributeCastException, ParseException{
		//Vehicle attributes
		String idTag = prop.getProperty("idTag");

		Priority priority = null;
		if(prop.contains("priority"))
			priority = Priority.valueOf(prop.getProperty("priority").toUpperCase());
		
		String imageURL = null;
		if(prop.containsKey("imageURL"))
			imageURL = prop.getProperty("imageURL");

		Boolean isActive = null;
		if(prop.containsKey("isActive"))
			isActive = Boolean.valueOf(prop.getProperty("isActive"));
		
		//Start building Motorcycle
		Helicopter helicopter = new Helicopter(idTag);
		
		if(priority != null)
			helicopter.setPrioraty(priority);
		
		if(imageURL != null)
			helicopter.setImageURL(imageURL);
		
		if(isActive != null)
			helicopter.setIsActive(isActive);

		//Helicopter Custom Attributes
		for(HelicopterCustomAttributes attribute : HelicopterCustomAttributes.values()){
			String attributeName = attribute.getAttributeName();
			if(prop.containsKey(attributeName)){
				String value = prop.getProperty(attributeName);
				try{
					addCustomAttribute(attributeName, attribute.getDataType(), value, helicopter);
				}catch(AttributeCastException | ParseException e){
					throw e;
				}
			}
		}
		
		return helicopter;
	}
	
	private SuperUser buildSuperUser(Properties prop) throws AttributeCastException, ParseException{
		//User attributes
		String name = prop.getProperty("name");
		String surname = prop.getProperty("surname");
		String email = prop.getProperty("email");

		AvailableLanguages preferedLanguage = null;
		if(prop.contains("preferedLanguage"))
			preferedLanguage = AvailableLanguages.valueOf(prop.getProperty("preferedLanguage").toUpperCase());

		Boolean isActive = null;
		if(prop.containsKey("isActive"))
			isActive = Boolean.valueOf(prop.getProperty("isActive"));

		String googleUserId = null;
		if(prop.containsKey("googleUserId"))
			googleUserId = prop.getProperty("googleUserId");

		String secondName = null;
		if(prop.containsKey("secondName"))
			secondName = prop.getProperty("secondName");

		String imageURL = null;
		if(prop.containsKey("imageURL"))
			imageURL = prop.getProperty("imageURL");

		//Start building agent
		SuperUser superUser = null;

		if(secondName != null){
			superUser = new SuperUser(name, secondName, surname, email);
		}else{
			superUser = new SuperUser(name, surname, email);
		}		

		if(preferedLanguage != null)
			superUser.setPreferedLanguage(preferedLanguage);

		if(isActive != null)
			superUser.setIsActive(isActive);

		if(googleUserId != null)
			superUser.setGoogleUserId(googleUserId);

		if(imageURL != null)
			superUser.setPictureURL(imageURL);

		//SuperUser Custom Attributes
		for(SuperUserCustomAttributes attribute : SuperUserCustomAttributes.values()){
			String attributeName = attribute.getAttributeName();
			if(prop.containsKey(attributeName)){
				String value = prop.getProperty(attributeName);
				try{
					addCustomAttribute(attributeName, attribute.getDataType(), value, superUser);
				}catch(AttributeCastException | ParseException e){
					throw e;
				}
			}
		}
		
		return superUser;
	}
	
	private Admin buildAdmin(Properties prop) throws AttributeCastException, ParseException{
		//User attributes
		String name = prop.getProperty("name");
		String surname = prop.getProperty("surname");
		String email = prop.getProperty("email");

		AvailableLanguages preferedLanguage = null;
		if(prop.contains("preferedLanguage"))
			preferedLanguage = AvailableLanguages.valueOf(prop.getProperty("preferedLanguage").toUpperCase());

		Boolean isActive = null;
		if(prop.containsKey("isActive"))
			isActive = Boolean.valueOf(prop.getProperty("isActive"));

		String googleUserId = null;
		if(prop.containsKey("googleUserId"))
			googleUserId = prop.getProperty("googleUserId");

		String secondName = null;
		if(prop.containsKey("secondName"))
			secondName = prop.getProperty("secondName");

		String imageURL = null;
		if(prop.containsKey("imageURL"))
			imageURL = prop.getProperty("imageURL");

		//Start building agent
		Admin admin = null;

		if(secondName != null){
			admin = new Admin(name, secondName, surname, email);
		}else{
			admin = new Admin(name, surname, email);
		}		

		if(preferedLanguage != null)
			admin.setPreferedLanguage(preferedLanguage);

		if(isActive != null)
			admin.setIsActive(isActive);

		if(googleUserId != null)
			admin.setGoogleUserId(googleUserId);

		if(imageURL != null)
			admin.setPictureURL(imageURL);

		//SuperUser Custom Attributes
		for(AdminCustomAttributes attribute : AdminCustomAttributes.values()){
			String attributeName = attribute.getAttributeName();
			if(prop.containsKey(attributeName)){
				String value = prop.getProperty(attributeName);
				try{
					addCustomAttribute(attributeName, attribute.getDataType(), value, admin);
				}catch(AttributeCastException | ParseException e){
					throw e;
				}
			}
		}
		
		return admin;
	}
	
	private Victim buildVictim(Properties prop) throws AttributeCastException, ParseException{
		//User attributes
		String name = prop.getProperty("name");
		String surname = prop.getProperty("surname");
		String email = prop.getProperty("email");

		Boolean isActive = null;
		if(prop.containsKey("isActive"))
			isActive = Boolean.valueOf(prop.getProperty("isActive"));

		String googleUserId = null;
		if(prop.containsKey("googleUserId"))
			googleUserId = prop.getProperty("googleUserId");

		String secondName = null;
		if(prop.containsKey("secondName"))
			secondName = prop.getProperty("secondName");

		String imageURL = null;
		if(prop.containsKey("imageURL"))
			imageURL = prop.getProperty("imageURL");

		//Start building agent
		Victim victim = null;

		if(secondName != null){
			victim = new Victim(name, secondName, surname, email);
		}else{
			victim = new Victim(name, surname, email);
		}

		if(isActive != null)
			victim.setIsActive(isActive);

		if(googleUserId != null)
			victim.setGoogleUserId(googleUserId);

		if(imageURL != null)
			victim.setPictureURL(imageURL);

		//SuperUser Custom Attributes
		for(VictimCustomAttributes attribute : VictimCustomAttributes.values()){
			String attributeName = attribute.getAttributeName();
			if(prop.containsKey(attributeName)){
				String value = prop.getProperty(attributeName);
				try{
					addCustomAttribute(attributeName, attribute.getDataType(), value, victim);
				}catch(AttributeCastException | ParseException e){
					throw e;
				}
			}
		}
		
		return victim;
	}
	
	private Monitor buildMonitor(Properties prop) throws AttributeCastException, ParseException{
		//User attributes
		String name = prop.getProperty("name");
		String surname = prop.getProperty("surname");
		String email = prop.getProperty("email");

		AvailableLanguages preferedLanguage = null;
		if(prop.contains("preferedLanguage"))
			preferedLanguage = AvailableLanguages.valueOf(prop.getProperty("preferedLanguage").toUpperCase());

		Boolean isActive = null;
		if(prop.containsKey("isActive"))
			isActive = Boolean.valueOf(prop.getProperty("isActive"));

		String googleUserId = null;
		if(prop.containsKey("googleUserId"))
			googleUserId = prop.getProperty("googleUserId");

		String secondName = null;
		if(prop.containsKey("secondName"))
			secondName = prop.getProperty("secondName");

		String imageURL = null;
		if(prop.containsKey("imageURL"))
			imageURL = prop.getProperty("imageURL");

		//Start building agent
		Monitor monitor = null;

		if(secondName != null){
			monitor = new Monitor(name, secondName, surname, email);
		}else{
			monitor = new Monitor(name, surname, email);
		}		

		if(preferedLanguage != null)
			monitor.setPreferedLanguage(preferedLanguage);

		if(isActive != null)
			monitor.setIsActive(isActive);

		if(googleUserId != null)
			monitor.setGoogleUserId(googleUserId);

		if(imageURL != null)
			monitor.setPictureURL(imageURL);

		//Monitor Custom Attributes
		for(MonitorCustomAttributes attribute : MonitorCustomAttributes.values()){
			String attributeName = attribute.getAttributeName();
			if(prop.containsKey(attributeName)){
				String value = prop.getProperty(attributeName);
				try{
					addCustomAttribute(attributeName, attribute.getDataType(), value, monitor);
				}catch(AttributeCastException | ParseException e){
					throw e;
				}
			}
		}
		
		return monitor;
	}
	
	private Agent buildAgent(Properties prop) throws IOException, AttributeCastException, ParseException, MissingAttributeException{
		//User attributes
		String name = prop.getProperty("name");
		String surname = prop.getProperty("surname");
		String email = prop.getProperty("email");

		AvailableLanguages preferedLanguage = null;
		if(prop.contains("preferedLanguage"))
			preferedLanguage = AvailableLanguages.valueOf(prop.getProperty("preferedLanguage").toUpperCase());

		Boolean isActive = null;
		if(prop.containsKey("isActive"))
			isActive = Boolean.valueOf(prop.getProperty("isActive"));

		String googleUserId = null;
		if(prop.containsKey("googleUserId"))
			googleUserId = prop.getProperty("googleUserId");

		String secondName = null;
		if(prop.containsKey("secondName"))
			secondName = prop.getProperty("secondName");

		String imageURL = null;
		if(prop.containsKey("imageURL"))
			imageURL = prop.getProperty("imageURL");

		//Start building agent
		Agent agent = null;

		if(secondName != null){
			agent = new Agent(name, secondName, surname, email);
		}else{
			agent = new Agent(name, surname, email);
		}		

		if(preferedLanguage != null)
			agent.setPreferedLanguage(preferedLanguage);

		if(isActive != null)
			agent.setIsActive(isActive);

		if(googleUserId != null)
			agent.setGoogleUserId(googleUserId);

		if(imageURL != null)
			agent.setPictureURL(imageURL);

		//Agent Custom Attributes
		for(AgentCustomAttributes attribute : AgentCustomAttributes.values()){
			String attributeName = attribute.getAttributeName();
			if(prop.containsKey(attributeName)){
				String value = prop.getProperty(attributeName);
				try{
					addCustomAttribute(attributeName, attribute.getDataType(), value, agent);
				}catch(AttributeCastException | ParseException e){
					throw e;
				}
			}
		}

		DrivingLicense drivingLicense = null;
		HelicopterLicense helicopterLicense = null;

		if(prop.containsKey("drivingLicence")){
			Properties licenseProperties = new Properties();
			InputStream inputStream = new FileInputStream(prop.getProperty("drivingLicence"));
			licenseProperties.load(inputStream);
			if(licenseProperties.containsKey("registerCode")){
				try{
					drivingLicense = buildDrivingLicense(agent, licenseProperties);
				}catch(ParseException e){
					throw e;
				}
			}else
				throw new MissingAttributeException("Missing mandatory attribute registerCode for Agent's driving license");
		}

		if(prop.containsKey("helicopterLicense")){
			Properties licenseProperties = new Properties();
			InputStream inputStream = new FileInputStream(prop.getProperty("helicopterLicense"));
			licenseProperties.load(inputStream);
			if(licenseProperties.containsKey("registerCode")){
				try{
					helicopterLicense = buildHelicopterLicense(agent, licenseProperties);
				}catch(ParseException e){
					throw e;
				}
			}else
				throw new MissingAttributeException("Missing mandatory attribute registerCode for Agent's helicopter license");
		}

		if(drivingLicense != null)
			agent.addLicense(drivingLicense);
		

		if(helicopterLicense != null)
			agent.addLicense(helicopterLicense);

		return agent;
	}

	private void addCustomAttribute(String attributeName, DataType dataType, String value, SystemObject object) throws AttributeCastException, ParseException{
		switch (dataType) {
		case BOOLEAN:
			object.addAttribute(new BooleanAttribute(Boolean.valueOf(value), attributeName));
			break;
		case BOOLEAN_ARRAY:
			List<String> strBoolList = Arrays.asList(value.replaceAll("^\\s*|\\s*$", "").split("\\s*,\\s*"));
			List<Boolean> boolList = new ArrayList<Boolean>();
			for(String str : strBoolList)
				boolList.add(Boolean.valueOf(str));
			object.addAttribute(new BooleanArrayAttribute(boolList, attributeName));
			break;
		case DATE:
			object.addAttribute(new DateAttribute(format.parse(value), attributeName));
			break;
		case DATE_ARRAY:
			List<String> strDateList = Arrays.asList(value.replaceAll("^\\s*|\\s*$", "").split("\\s*,\\s*"));
			List<Date> dateList = new ArrayList<Date>();
			for(String str : strDateList)
				dateList.add(format.parse(str));
			object.addAttribute(new DateArrayAttribute(dateList, attributeName));
			break;
		case FLOAT:
			object.addAttribute(new FloatAttribute(Float.valueOf(value), attributeName));
			break;
		case FLOAT_ARRAY:
			List<String> strFloatList = Arrays.asList(value.replaceAll("^\\s*|\\s*$", "").split("\\s*,\\s*"));
			List<Float> floatList = new ArrayList<Float>();
			for(String str : strFloatList)
				floatList.add(Float.valueOf(str));
			object.addAttribute(new FloatArrayAttribute(floatList, attributeName));
			break;
		case INTEGER:
			object.addAttribute(new IntegerAttribute(Integer.valueOf(value), attributeName));
			break;
		case INTERGER_ARRAY:
			List<String> strIntList = Arrays.asList(value.replaceAll("^\\s*|\\s*$", "").split("\\s*,\\s*"));
			List<Integer> intList = new ArrayList<Integer>();
			for(String str : strIntList)
				intList.add(Integer.valueOf(str));
			object.addAttribute(new IntegerArrayAttribute(intList, attributeName));
			break;
		case STRING:
			object.addAttribute(new StringAttribute(value, attributeName));
			break;
		case STRING_ARRAY:
			List<String> strList = Arrays.asList(value.replaceAll("^\\s*|\\s*$", "").split("\\s*,\\s*"));
			object.addAttribute(new StringArrayAttribute(strList, attributeName));
			break;
		default:
			break;
		}
	}

	private DrivingLicense buildDrivingLicense(Agent agent, Properties prop) throws ParseException{
		String registerCode = prop.getProperty("registerCode");

		Date effectiveUntil = null;
		if(prop.containsKey("effectiveUntil")){
			effectiveUntil = format.parse(prop.getProperty("effectiveUntil"));
		}

		Boolean hasAcategory = null;
		if(prop.containsKey("hasAcategory")){
			hasAcategory = Boolean.valueOf(prop.getProperty("hasAcategory"));
		}

		DrivingCategories category = null;
		if(prop.containsKey("category")){
			category = DrivingCategories.valueOf(prop.getProperty("category"));
		}

		DrivingLicense drivingLicense = new DrivingLicense(agent, registerCode);

		if(effectiveUntil != null)
			drivingLicense.setValidUntil(effectiveUntil);

		if(hasAcategory != null)
			drivingLicense.setHasAcategory(hasAcategory);

		if(category != null)
			drivingLicense.setCategory(category);

		return drivingLicense;
	}

	private HelicopterLicense buildHelicopterLicense(Agent agent, Properties prop) throws ParseException{
		String registerCode = prop.getProperty("registerCode");

		Date effectiveUntil = null;
		if(prop.containsKey("effectiveUntil")){
			effectiveUntil = format.parse(prop.getProperty("effectiveUntil"));
		}

		HelicopterLicenseTypes category = null;
		if(prop.containsKey("category")){
			category = HelicopterLicenseTypes.valueOf(prop.getProperty("category"));
		}

		HelicopterLicense helicopterLicense = new HelicopterLicense(agent, registerCode);

		if(effectiveUntil != null)
			helicopterLicense.setValidUntil(effectiveUntil);


		if(category != null)
			helicopterLicense.setCategory(category);

		return helicopterLicense;
	}

	private <T> void persistObject(T object, PersistenceManager pm){
		try{
			pm.currentTransaction().begin();
			pm.makePersistent(object);
			pm.currentTransaction().commit();
		}catch (Exception e){
			e.printStackTrace();
			if(pm.currentTransaction().isActive())
				pm.currentTransaction().rollback();
		}finally{
			pm.close();
		}
	}

}
