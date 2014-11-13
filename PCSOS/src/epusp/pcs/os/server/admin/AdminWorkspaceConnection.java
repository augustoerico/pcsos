package epusp.pcs.os.server.admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

import epusp.pcs.os.admin.client.rpc.IAdminWorkspaceService;
import epusp.pcs.os.server.Connection;
import epusp.pcs.os.server.PMF;
import epusp.pcs.os.shared.general.MoveCursor;
import epusp.pcs.os.shared.model.person.Victim;
import epusp.pcs.os.shared.model.person.user.AccountTypes;
import epusp.pcs.os.shared.model.person.user.Agent;
import epusp.pcs.os.shared.model.person.user.User;
import epusp.pcs.os.shared.model.vehicle.Car;
import epusp.pcs.os.shared.model.vehicle.Helicopter;
import epusp.pcs.os.shared.model.vehicle.Vehicle;

public class AdminWorkspaceConnection extends Connection implements IAdminWorkspaceService{

	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException{}

	@Override
	protected Boolean isLoggedIn() {
		if(super.isLoggedIn()){
			User user = (User) getSessionAttibute(userSessionAttribute);
			if(user.getType().equals(AccountTypes.Admin)){
				return true;
			}
		}
		return false;
	}

	private static final String victimCursorPositionSessionAttribute = "victimCursorPositionSessionAttribute";
	private static final String victimCursorsListSessionAttribute = "victimCursorsListSessionAttribute";

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Victim> getVictims(MoveCursor move, int range){
		if(isLoggedIn()){
			Integer cursorPosition = (Integer) getSessionAttibute(victimCursorPositionSessionAttribute);
			List<Cursor> cursors = (List<Cursor>) getSessionAttibute(victimCursorsListSessionAttribute);

			PersistenceManager pm = PMF.get().getPersistenceManager();

			Query q = pm.newQuery(Victim.class);
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

			List<Victim> victims = null;
			Collection<Victim> detachedVictims = null;

			try {
				victims = (List<Victim>) q.execute();
				if(victims != null)
					detachedVictims = pm.detachCopyAll(victims);
			} finally {
				q.closeAll();
			}

			if((detachedVictims != null && !detachedVictims.isEmpty() && move.equals(MoveCursor.FORWARD) && cursorPosition == cursors.size()-1) 
					|| (cursors.isEmpty() && cursorPosition == -1)){
				Cursor cursor = JDOCursorHelper.getCursor(victims);
				cursors.add(cursor);
			}

			setSessionAttribute(victimCursorPositionSessionAttribute, cursorPosition);
			setSessionAttribute(victimCursorsListSessionAttribute, cursors);

			if(detachedVictims == null)
				return new ArrayList<Victim>();

			return detachedVictims;
		}
		return null;
	}

	private static final String agentCursorPositionSessionAttribute = "agentCursorPositionSessionAttribute";
	private static final String agentCursorsListSessionAttribute = "agentCursorsListSessionAttribute";

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Agent> getAgents(MoveCursor move, int range){
		if(isLoggedIn()){
			Integer cursorPosition = (Integer) getSessionAttibute(agentCursorPositionSessionAttribute);
			List<Cursor> cursors = (List<Cursor>) getSessionAttibute(agentCursorsListSessionAttribute);

			PersistenceManager pm = PMF.get().getPersistenceManager();

			Query q = pm.newQuery(Agent.class);
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

			List<Agent> agents = null;
			Collection<Agent> detachedAgents = null;

			try {
				agents = (List<Agent>) q.execute();
				if(agents != null)
					detachedAgents = pm.detachCopyAll(agents);
			} finally {
				q.closeAll();
			}

			if((detachedAgents != null && !detachedAgents.isEmpty() && move.equals(MoveCursor.FORWARD) && cursorPosition == cursors.size()-1) 
					|| (cursors.isEmpty() && cursorPosition == -1)){
				Cursor cursor = JDOCursorHelper.getCursor(agents);
				cursors.add(cursor);
			}

			setSessionAttribute(agentCursorPositionSessionAttribute, cursorPosition);
			setSessionAttribute(agentCursorsListSessionAttribute, cursors);

			if(detachedAgents == null)
				return new ArrayList<Agent>();

			return detachedAgents;
		}
		return null;
	}

	private static final String carCursorPositionSessionAttribute = "carCursorPositionSessionAttribute";
	private static final String carCursorsListSessionAttribute = "carCursorsListSessionAttribute";

	@Override
	@SuppressWarnings("unchecked")
	public Collection<Car> getCars(MoveCursor move, int range){
		Integer cursorPosition = (Integer) getSessionAttibute(carCursorPositionSessionAttribute);
		List<Cursor> cursors = (List<Cursor>) getSessionAttibute(carCursorsListSessionAttribute);

		PersistenceManager pm = PMF.get().getPersistenceManager();

		Query q = pm.newQuery(Car.class);
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

		List<Car> car = null;
		Collection<Car> detachedCar = null;

		try {
			car = (List<Car>) q.execute();
			if(car != null)
				detachedCar = pm.detachCopyAll(car);
		} finally {
			q.closeAll();
		}

		if((detachedCar != null && !detachedCar.isEmpty() && move.equals(MoveCursor.FORWARD) && cursorPosition == cursors.size()-1) 
				|| (cursors.isEmpty() && cursorPosition == -1)){
			Cursor cursor = JDOCursorHelper.getCursor(car);
			cursors.add(cursor);
		}

		setSessionAttribute(carCursorPositionSessionAttribute, cursorPosition);
		setSessionAttribute(carCursorsListSessionAttribute, cursors);

		if(detachedCar == null)
			return new ArrayList<Car>();

		return detachedCar;
	}

	private static final String helicopterCursorPositionSessionAttribute = "helicopterCursorPositionSessionAttribute";
	private static final String helicopterCursorsListSessionAttribute = "helicopterCursorsListSessionAttribute";

	@Override
	@SuppressWarnings("unchecked")
	public Collection<Helicopter> getHelicopters(MoveCursor move, int range){
		Integer cursorPosition = (Integer) getSessionAttibute(helicopterCursorPositionSessionAttribute);
		List<Cursor> cursors = (List<Cursor>) getSessionAttibute(helicopterCursorsListSessionAttribute);

		PersistenceManager pm = PMF.get().getPersistenceManager();

		Query q = pm.newQuery(Helicopter.class);
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

		List<Helicopter> helicopter = null;
		Collection<Helicopter> detachedHelicopter = null;

		try {
			helicopter = (List<Helicopter>) q.execute();
			if(helicopter != null)
				detachedHelicopter = pm.detachCopyAll(helicopter);
		} finally {
			q.closeAll();
		}

		if((detachedHelicopter != null && !detachedHelicopter.isEmpty() && move.equals(MoveCursor.FORWARD) && cursorPosition == cursors.size()-1) 
				|| (cursors.isEmpty() && cursorPosition == -1)){
			Cursor cursor = JDOCursorHelper.getCursor(helicopter);
			cursors.add(cursor);
		}

		setSessionAttribute(helicopterCursorPositionSessionAttribute, cursorPosition);
		setSessionAttribute(helicopterCursorsListSessionAttribute, cursors);

		if(detachedHelicopter == null)
			return new ArrayList<Helicopter>();

		return detachedHelicopter;
	}
	
	@Override
	public void createVictim(Victim victim){
	
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		System.out.println("Creating/updating Victim " + victim.getEmail());
		
		try{
			pm.currentTransaction().begin();
			pm.makePersistent(victim);
			pm.currentTransaction().commit();
		}catch (Exception e){
			e.printStackTrace();
			if(pm.currentTransaction().isActive())
				pm.currentTransaction().rollback();
		}finally{
			pm.close();
		}
	}
	
	@Override
	public void createAgent(Agent agent){
	
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		System.out.println("Creating/updating new Agent " + agent.getEmail());
		
		try{
			pm.currentTransaction().begin();
			pm.makePersistent(agent);
			pm.currentTransaction().commit();
		}catch (Exception e){
			e.printStackTrace();
			if(pm.currentTransaction().isActive())
				pm.currentTransaction().rollback();
		}finally{
			pm.close();
		}
	}
	
	@Override
	public void createVehicle(Vehicle vehicle){
	
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		System.out.println("Creating/updating new Vehicle " + vehicle.getIdTag());
		
		try{
			pm.currentTransaction().begin();

			switch (vehicle.getType()) {
			case Car:
				pm.makePersistent((Car) vehicle);
				break;
			case Helicopter:
				pm.makePersistent((Helicopter) vehicle);
				break;
			default:
				break;
			};
			
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
