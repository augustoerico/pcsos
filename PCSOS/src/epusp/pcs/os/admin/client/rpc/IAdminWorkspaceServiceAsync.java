package epusp.pcs.os.admin.client.rpc;

import java.util.Collection;

import com.google.gwt.user.client.rpc.AsyncCallback;

import epusp.pcs.os.shared.client.rpc.IConnectionServiceAsync;
import epusp.pcs.os.shared.general.MoveCursor;
import epusp.pcs.os.shared.model.person.user.agent.Agent;
import epusp.pcs.os.shared.model.person.victim.Victim;
import epusp.pcs.os.shared.model.vehicle.Vehicle;
import epusp.pcs.os.shared.model.vehicle.car.Car;
import epusp.pcs.os.shared.model.vehicle.helicopter.Helicopter;

public interface IAdminWorkspaceServiceAsync extends IConnectionServiceAsync {

	void getVictims(MoveCursor move, int pageSize, AsyncCallback<Collection<Victim>> callback);

	void getAgents(MoveCursor move, int range,
			AsyncCallback<Collection<Agent>> callback);

	void getCars(MoveCursor move, int range,
			AsyncCallback<Collection<Car>> callback);

	void getHelicopters(MoveCursor move, int range,
			AsyncCallback<Collection<Helicopter>> callback);

	void createVictim(Victim victim, AsyncCallback<Void> callback);

	void createAgent(Agent agent, AsyncCallback<Void> callback);

	void createVehicle(Vehicle vehicle, AsyncCallback<Void> callback);

	void getFullCar(String id, AsyncCallback<Car> asyncCallback);

	void getFullHelicopter(String id, AsyncCallback<Helicopter> asyncCallback);

}
