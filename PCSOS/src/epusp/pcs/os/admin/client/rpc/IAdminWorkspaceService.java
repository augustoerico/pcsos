package epusp.pcs.os.admin.client.rpc;

import java.util.Collection;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import epusp.pcs.os.shared.client.rpc.IConnectionService;
import epusp.pcs.os.shared.general.MoveCursor;
import epusp.pcs.os.shared.model.person.user.agent.Agent;
import epusp.pcs.os.shared.model.person.victim.Victim;
import epusp.pcs.os.shared.model.vehicle.Vehicle;
import epusp.pcs.os.shared.model.vehicle.car.Car;
import epusp.pcs.os.shared.model.vehicle.helicopter.Helicopter;

@RemoteServiceRelativePath("adminWorkspaceService")
public interface IAdminWorkspaceService extends IConnectionService {

	Collection<Victim> getVictims(MoveCursor move, int pageSize);

	Collection<Agent> getAgents(MoveCursor move, int range);

	Collection<Car> getCars(MoveCursor move, int range);

	Collection<Helicopter> getHelicopters(MoveCursor move, int range);

	void createVictim(Victim victim);

	void createAgent(Agent agent);

	void createVehicle(Vehicle vehicle);

	Car getFullCar(String id);

	Helicopter getFullHelicopter(String id);
}
