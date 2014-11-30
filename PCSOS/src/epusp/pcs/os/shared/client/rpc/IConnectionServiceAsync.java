package epusp.pcs.os.shared.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import epusp.pcs.os.shared.model.ICustomAttributes;
import epusp.pcs.os.shared.model.attribute.AttributeInfo;
import epusp.pcs.os.shared.model.person.user.AvailableLanguages;
import epusp.pcs.os.shared.model.person.user.User;
import epusp.pcs.os.shared.model.person.user.agent.Agent;
import epusp.pcs.os.shared.model.person.victim.Victim;
import epusp.pcs.os.shared.model.vehicle.Vehicle;


public interface IConnectionServiceAsync {

	void getUserInfo(AsyncCallback<User> callback);

	void identifySession(String key, AsyncCallback<Void> callback);

	void logout(AsyncCallback<Void> callback);

	void setPreferredLanguage(AvailableLanguages language,
			AsyncCallback<Void> callback);

	void getUserLanguage(AsyncCallback<AvailableLanguages> callback);

	void getVehicle(String vehicleId, AsyncCallback<Vehicle> callback);

	void getVictim(String email, AsyncCallback<Victim> callback);

	void getFullVictim(String email, AsyncCallback<Victim> callback);

	void getAttributeInfo(String attributeName,
			AsyncCallback<AttributeInfo> callback);

	void getAgent(String id, AsyncCallback<Agent> callback);

	void getFullAgent(String id, AsyncCallback<Agent> callback);

	void getCustomAttributesInfo(ICustomAttributes[] customAttributes,
			AsyncCallback<List<AttributeInfo>> callback);
}
