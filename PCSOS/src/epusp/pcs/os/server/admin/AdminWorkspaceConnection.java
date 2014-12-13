package epusp.pcs.os.server.admin;

import java.util.Collection;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;

import epusp.pcs.os.admin.client.rpc.IAdminWorkspaceService;
import epusp.pcs.os.server.Connection;
import epusp.pcs.os.server.PMF;
import epusp.pcs.os.shared.general.MoveCursor;
import epusp.pcs.os.shared.model.person.user.AccountTypes;
import epusp.pcs.os.shared.model.person.user.User;
import epusp.pcs.os.shared.model.person.user.agent.Agent;
import epusp.pcs.os.shared.model.person.victim.Victim;
import epusp.pcs.os.shared.model.vehicle.Vehicle;
import epusp.pcs.os.shared.model.vehicle.car.Car;
import epusp.pcs.os.shared.model.vehicle.helicopter.Helicopter;
import epusp.pcs.os.shared.model.vehicle.motorcycle.Motorcycle;

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

	@Override
	public Collection<Victim> getVictims(MoveCursor move, int range){
		if(isLoggedIn()){
			return getData(Victim.class, move, range, victimCursorPositionSessionAttribute, victimCursorsListSessionAttribute);
		}
		return null;
	}

	private static final String agentCursorPositionSessionAttribute = "agentCursorPositionSessionAttribute";
	private static final String agentCursorsListSessionAttribute = "agentCursorsListSessionAttribute";

	@Override
	public Collection<Agent> getAgents(MoveCursor move, int range){
		if(isLoggedIn()){
			return getData(Agent.class, move, range, agentCursorPositionSessionAttribute, agentCursorsListSessionAttribute);
		}
		return null;
	}

	private static final String carCursorPositionSessionAttribute = "carCursorPositionSessionAttribute";
	private static final String carCursorsListSessionAttribute = "carCursorsListSessionAttribute";

	@Override
	public Collection<Car> getCars(MoveCursor move, int range){
		if(isLoggedIn()){
			return getData(Car.class, move, range, carCursorPositionSessionAttribute, carCursorsListSessionAttribute);
		}
		return null;
	}

	private static final String helicopterCursorPositionSessionAttribute = "helicopterCursorPositionSessionAttribute";
	private static final String helicopterCursorsListSessionAttribute = "helicopterCursorsListSessionAttribute";

	@Override
	public Collection<Helicopter> getHelicopters(MoveCursor move, int range){
		if(isLoggedIn()){
			return getData(Helicopter.class, move, range, helicopterCursorPositionSessionAttribute, helicopterCursorsListSessionAttribute);
		}
		return null;
	}
	
	private static final String motorcycleCursorPositionSessionAttribute = "motorcycleCursorPositionSessionAttribute";
	private static final String motorcycleCursorsListSessionAttribute = "motorcycleCursorsListSessionAttribute";
	
	@Override
	public Collection<Motorcycle> getMotorcycles(MoveCursor move, int range) {
		return getData(Motorcycle.class, move, range, motorcycleCursorPositionSessionAttribute, motorcycleCursorsListSessionAttribute);
	}

	@Override
	public void createVictim(Victim victim){
		if(isLoggedIn()){
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
	}

	@Override
	public void createAgent(Agent agent){
		if(isLoggedIn()){
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
	}

	@Override
	public void createVehicle(Vehicle vehicle){
		if(isLoggedIn()){
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
				case Motorcycle:
					pm.makePersistent((Motorcycle) vehicle);
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

	@Override
	public Car getFullCar(String id) {
		if(isLoggedIn()){
			PersistenceManager pm = PMF.get().getPersistenceManager();

			pm.getFetchPlan().addGroup("all_system_object_attributes");
			pm.getFetchPlan().setMaxFetchDepth(-1);

			Car car = null, detached = null;
			try{
				pm.currentTransaction().begin();
				car = pm.getObjectById(Car.class, id);
				detached = pm.detachCopy(car);
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
	public Helicopter getFullHelicopter(String id) {
		if(isLoggedIn()){
			PersistenceManager pm = PMF.get().getPersistenceManager();

			pm.getFetchPlan().addGroup("all_system_object_attributes");
			pm.getFetchPlan().setMaxFetchDepth(-1);

			Helicopter car = null, detached = null;
			try{
				pm.currentTransaction().begin();
				car = pm.getObjectById(Helicopter.class, id);
				detached = pm.detachCopy(car);
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
	public Motorcycle getFullMotorcycle(String id) {
		if(isLoggedIn()){
			PersistenceManager pm = PMF.get().getPersistenceManager();

			pm.getFetchPlan().addGroup("all_system_object_attributes");
			pm.getFetchPlan().setMaxFetchDepth(-1);

			Motorcycle motorcycle = null, detached = null;
			try{
				pm.currentTransaction().begin();
				motorcycle = pm.getObjectById(Motorcycle.class, id);
				detached = pm.detachCopy(motorcycle);
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
}
