package epusp.pcs.os.shared.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import epusp.pcs.os.shared.exception.LoginException;
import epusp.pcs.os.shared.model.person.user.AvailableLanguages;
import epusp.pcs.os.shared.model.person.user.User;

@RemoteServiceRelativePath("connectionService")
public interface IConnectionService extends RemoteService {

	User getUserInfo();
	
	void identifySession(String key) throws LoginException;

	void logout();

	void setPreferredLanguage(AvailableLanguages language);

	AvailableLanguages getUserLanguage();
	
}
