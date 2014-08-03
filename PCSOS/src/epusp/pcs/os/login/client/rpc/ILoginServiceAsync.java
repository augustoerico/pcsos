package epusp.pcs.os.login.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

import epusp.pcs.os.model.person.user.User;

public interface ILoginServiceAsync {
	
	void Hello(AsyncCallback<String> callback);

	void login(String username, String password, AsyncCallback<User> callback);

}
