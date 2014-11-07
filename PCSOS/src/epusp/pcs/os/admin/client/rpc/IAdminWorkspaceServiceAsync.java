package epusp.pcs.os.admin.client.rpc;

import java.util.Collection;

import com.google.gwt.user.client.rpc.AsyncCallback;

import epusp.pcs.os.shared.client.rpc.IConnectionServiceAsync;
import epusp.pcs.os.shared.general.MoveCursor;
import epusp.pcs.os.shared.model.person.Victim;
import epusp.pcs.os.shared.model.person.user.Agent;
import epusp.pcs.os.shared.model.vehicle.Car;
import epusp.pcs.os.shared.model.vehicle.Helicopter;

public interface IAdminWorkspaceServiceAsync extends IConnectionServiceAsync {

	void getVictims(MoveCursor move, int pageSize, AsyncCallback<Collection<Victim>> callback);

	void getAgents(MoveCursor move, int range,
			AsyncCallback<Collection<Agent>> callback);

	void getCars(MoveCursor move, int range,
			AsyncCallback<Collection<Car>> callback);

	void getHelicopters(MoveCursor move, int range,
			AsyncCallback<Collection<Helicopter>> callback);

}
