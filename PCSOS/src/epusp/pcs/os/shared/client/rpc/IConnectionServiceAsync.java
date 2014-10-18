package epusp.pcs.os.shared.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

import epusp.pcs.os.model.person.user.User;


public interface IConnectionServiceAsync {

	void getUserInfo(AsyncCallback<User> callback);

	void identifySession(String key, AsyncCallback<Void> callback);
}
