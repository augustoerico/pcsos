package epusp.pcs.os.server;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JDOCursorHelper;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import epusp.pcs.os.server.loader.Loader;
import epusp.pcs.os.server.loader.LoaderException;
import epusp.pcs.os.server.login.AuthenticationManager;
import epusp.pcs.os.server.workflow.EmergencyCallWorkflow;
import epusp.pcs.os.shared.client.rpc.IConnectionService;
import epusp.pcs.os.shared.exception.CannotLogoutExeception;
import epusp.pcs.os.shared.exception.LoginException;
import epusp.pcs.os.shared.general.MoveCursor;
import epusp.pcs.os.shared.model.ICustomAttributes;
import epusp.pcs.os.shared.model.attribute.AttributeInfo;
import epusp.pcs.os.shared.model.person.user.AvailableLanguages;
import epusp.pcs.os.shared.model.person.user.User;
import epusp.pcs.os.shared.model.person.user.agent.Agent;
import epusp.pcs.os.shared.model.person.victim.Victim;
import epusp.pcs.os.shared.model.vehicle.Vehicle;
import epusp.pcs.os.shared.model.vehicle.car.Car;
import epusp.pcs.os.shared.model.vehicle.helicopter.Helicopter;

public class Connection extends RemoteServiceServlet implements IConnectionService{

	private static final long serialVersionUID = 1L;

	protected EmergencyCallWorkflow workflow = EmergencyCallWorkflow.getInstance();

	protected AuthenticationManager authenticationManager = AuthenticationManager.getInstance();

	protected String userSessionAttribute = "userSessionAttribute";

	private static final HashMap<String, AttributeInfo> attributesInfo = new HashMap<String, AttributeInfo>();

	public void init() throws ServletException
	{
		System.out.println("Initializing with test data");
		
		Loader loader = new Loader();
		try {
			loader.load(PMF.get());
		} catch (IOException | LoaderException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void identifySession(String key) throws LoginException{
		if(!isLoggedIn()){
			if(authenticationManager.isLoggedIn(key)){
				User user = authenticationManager.getUser(key);
				setSessionAttribute(userSessionAttribute, user);
			}else
				throw new LoginException();
		}
	}

	@Override
	public User getUserInfo(){
		if(isLoggedIn()){
			return (User) getSessionAttibute(userSessionAttribute);
		}
		return null;
	}

	@Override
	public void logout() throws CannotLogoutExeception{
		if(isLoggedIn()){
			HttpSession session = this.getThreadLocalRequest().getSession(false);
			if (session == null)
				return;

			System.out.println(getUserInfo().getEmail() + " has logout @ " + DateFormat.getDateInstance().format(new Date()));
			session.invalidate();
		}
	}

	@Override
	public void setPreferredLanguage(AvailableLanguages language){
		if(isLoggedIn()){
			User user = getUserInfo();
			user.setPreferedLanguage(language);

			PersistenceManager pm = PMF.get().getPersistenceManager();
			System.out.println("Changing preferred language for " + user.getEmail() + " to " + user.getPreferedLanguage().name());
			
			pm = PMF.get().getPersistenceManager();
			try{
				pm.currentTransaction().begin();
				user = pm.getObjectById(user.getClass(), user.getEmail());
				user.setPreferedLanguage(language);
				pm.makePersistent(user);
				pm.currentTransaction().commit();
			}catch(Exception e){
				e.printStackTrace();
				if(pm.currentTransaction().isActive())
					pm.currentTransaction().rollback();
			}finally{				
				pm.close();
			}
		}
	}

	@Override
	public AvailableLanguages getUserLanguage(){
		if(isLoggedIn()){
			return getUserInfo().getPreferedLanguage();
		}
		return null;
	}

	@Override
	public Vehicle getVehicle(String vehicleId){
		if(isLoggedIn()){
			PersistenceManager pm = PMF.get().getPersistenceManager();

			pm = PMF.get().getPersistenceManager();
			Vehicle vehicle = null;
			try{
				vehicle = pm.getObjectById(Car.class, vehicleId);
			}catch(Exception e){
			}finally{				
				pm.close();
			}
			
			if(vehicle == null){
				pm = PMF.get().getPersistenceManager();
				try{
					vehicle = pm.getObjectById(Helicopter.class, vehicleId);
				}catch(Exception e){
				}finally{
					pm.close();
				}
			}
			return vehicle;
		}

		return null;
	}

	@Override
	public Victim getVictim(String email){
		if(isLoggedIn()){
			PersistenceManager pm = PMF.get().getPersistenceManager();

			Victim victim = null;
			try{
				victim = pm.getObjectById(Victim.class, email);
			}catch(Exception e){
			}finally{				
				pm.close();
			}

			return victim;
		}
		return null;
	}

	@Override
	public Victim getFullVictim(String email){
		if(isLoggedIn()){
			PersistenceManager pm = PMF.get().getPersistenceManager();

			pm.getFetchPlan().addGroup("all_system_object_attributes");
			pm.getFetchPlan().setMaxFetchDepth(-1);

			Victim victim = null, detached = null;
			try{
				pm.currentTransaction().begin();
				victim = pm.getObjectById(Victim.class, email);
				detached = pm.detachCopy(victim);
				pm.currentTransaction().commit();
			}catch(Exception e){
				e.printStackTrace();
				if (pm.currentTransaction().isActive())
					pm.currentTransaction().rollback();
			}finally{
				pm.close();
			}

			return detached;
		}
		return null;
	}

	@Override
	public Agent getAgent(String id){
		if(isLoggedIn()){
			PersistenceManager pm = PMF.get().getPersistenceManager();

			Agent agent = null;
			try{
				agent = pm.getObjectById(Agent.class, id);
			}catch(Exception e){
			}finally{				
				pm.close();
			}

			return agent;
		}
		return null;
	}

	@Override
	public Agent getFullAgent(String id){
		if(isLoggedIn()){
			PersistenceManager pm = PMF.get().getPersistenceManager();

			pm.getFetchPlan().addGroup("all_system_object_attributes");
			pm.getFetchPlan().setMaxFetchDepth(-1);

			Agent agent = null, detached = null;
			try{
				pm.currentTransaction().begin();
				agent = pm.getObjectById(Agent.class, id);
				detached = pm.detachCopy(agent);
				pm.currentTransaction().commit();
			}catch(Exception e){
				e.printStackTrace();
				if (pm.currentTransaction().isActive())
					pm.currentTransaction().rollback();
			}finally{
				pm.close();
			}

			return detached;
		}
		return null;
	}

	@Override
	public AttributeInfo getAttributeInfo(String attributeName){
		if(isLoggedIn()){
			PersistenceManager pm = PMF.get().getPersistenceManager();

			pm = PMF.get().getPersistenceManager();
			AttributeInfo attributeInfo = null, detached = null;
			try{
				pm.currentTransaction().begin();
				attributeInfo = pm.getObjectById(AttributeInfo.class, attributeName);
				detached = pm.detachCopy(attributeInfo);
				pm.currentTransaction().commit();
			}catch(Exception e){
				e.printStackTrace();
				if (pm.currentTransaction().isActive())
					pm.currentTransaction().rollback();
			}finally{
				pm.close();
			}

			return detached;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	protected <T> Collection<T> getData(Class<T> t, MoveCursor move, int range, String cursorPositionSessionAttribute, String cursorsListSessionAttribute){
		Integer cursorPosition = (Integer) getSessionAttibute(cursorPositionSessionAttribute);
		List<Cursor> cursors = (List<Cursor>) getSessionAttibute(cursorsListSessionAttribute);

		PersistenceManager pm = PMF.get().getPersistenceManager();

		Query q = pm.newQuery(t);
		q.setRange(0, range);

		//Initializing
		if(cursorPosition == null && cursors == null){
			cursorPosition = -1;
			cursors = new ArrayList<Cursor>();
		}

		switch(move){
		case BACKWARD:
			if(cursorPosition > 0){
				cursorPosition--;
			}
			break;
		case FORWARD:
			cursorPosition++;
			break;
		case FIRST:
			cursorPosition = -1;
			break;
		default:
			break;
		}

		if(!move.equals(MoveCursor.FIRST)){
			Cursor cursor = cursors.get(cursorPosition);
			Map<String, Object> extensionMap = new HashMap<String, Object>();
			extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
			q.setExtensions(extensionMap);
		}

		List<T> data = null;
		Collection<T> detachedData = null;

		try {
			data = (List<T>) q.execute();
			if(data != null)
				detachedData = pm.detachCopyAll(data);
		} finally {
			q.closeAll();
			pm.close();
		}

		if(detachedData != null && !detachedData.isEmpty()){
			Cursor cursor = JDOCursorHelper.getCursor(data);
			if((move.equals(MoveCursor.FORWARD) && cursorPosition == cursors.size()-1) ||
					(cursors.isEmpty() && cursorPosition == -1)){
				cursors.add(cursor);
			}else{
				cursors.set(cursorPosition+1, cursor);
			}
		}

		setSessionAttribute(cursorPositionSessionAttribute, cursorPosition);
		setSessionAttribute(cursorsListSessionAttribute, cursors);

		if(detachedData == null)
			return new ArrayList<T>();

		return detachedData;
	}

	@Override
	public List<AttributeInfo> getCustomAttributesInfo(ICustomAttributes[] customAttributes){
		if(isLoggedIn()){
			List<AttributeInfo> attributesInfo = new ArrayList<AttributeInfo>();

			for(ICustomAttributes customAttribute : customAttributes){
				AttributeInfo attrInfo = Connection.attributesInfo.get(customAttribute.getAttributeName());
				if(attrInfo == null){
					PersistenceManager pm = PMF.get().getPersistenceManager();
					

					AttributeInfo attributeInfo = null, detached = null;
					try{
						pm.currentTransaction().begin();
						attributeInfo = pm.getObjectById(AttributeInfo.class, customAttribute.getAttributeName());
						detached = pm.detachCopy(attributeInfo);
						pm.currentTransaction().commit();
					}catch(Exception e){
						e.printStackTrace();
						if (pm.currentTransaction().isActive())
							pm.currentTransaction().rollback();
					}finally{
						pm.close();
					}

					Connection.attributesInfo.put(detached.getAttributeName(), detached);
					attrInfo = detached;
				}
				attributesInfo.add(attrInfo);
			}
			return attributesInfo;
		}
		
		return null;
	}

	protected Boolean isLoggedIn(){
		return getSessionAttibute(userSessionAttribute) != null;
	}

	protected void setSessionAttribute(String attribute, Object value){
		this.getThreadLocalRequest().getSession(true).setAttribute(attribute, value);
	}

	protected Object getSessionAttibute(String attribute){
		return this.getThreadLocalRequest().getSession(true).getAttribute(attribute);
	}

}
