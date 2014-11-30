package epusp.pcs.os.shared.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import epusp.pcs.os.shared.exception.CannotLogoutExeception;
import epusp.pcs.os.shared.exception.LoginException;
import epusp.pcs.os.shared.model.ICustomAttributes;
import epusp.pcs.os.shared.model.attribute.AttributeInfo;
import epusp.pcs.os.shared.model.person.user.AvailableLanguages;
import epusp.pcs.os.shared.model.person.user.User;
import epusp.pcs.os.shared.model.person.user.agent.Agent;
import epusp.pcs.os.shared.model.person.victim.Victim;
import epusp.pcs.os.shared.model.vehicle.Vehicle;

@RemoteServiceRelativePath("connectionService")
public interface IConnectionService extends RemoteService {

	User getUserInfo();
	
	void identifySession(String key) throws LoginException;

	void logout() throws CannotLogoutExeception;

	void setPreferredLanguage(AvailableLanguages language);

	AvailableLanguages getUserLanguage();

	Vehicle getVehicle(String vehicleId);

	Victim getVictim(String email);

	Victim getFullVictim(String email);

	AttributeInfo getAttributeInfo(String attributeName);

	Agent getAgent(String id);

	Agent getFullAgent(String id);

	List<AttributeInfo> getCustomAttributesInfo(
			ICustomAttributes[] customAttributes);
	
}
