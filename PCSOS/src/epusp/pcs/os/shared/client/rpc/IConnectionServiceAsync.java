package epusp.pcs.os.shared.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

import epusp.pcs.os.shared.model.person.user.AvailableLanguages;
import epusp.pcs.os.shared.model.person.user.User;
import epusp.pcs.os.shared.model.vehicle.Vehicle;


public interface IConnectionServiceAsync {

	void getUserInfo(AsyncCallback<User> callback);

	void identifySession(String key, AsyncCallback<Void> callback);

	void logout(AsyncCallback<Void> callback);

	void setPreferredLanguage(AvailableLanguages language,
			AsyncCallback<Void> callback);

	void getUserLanguage(AsyncCallback<AvailableLanguages> callback);

	void getVehicle(String vehicleId, AsyncCallback<Vehicle> callback);
}
