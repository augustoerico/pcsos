package epusp.pcs.os.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

import epusp.pcs.os.model.User;

public interface IConnectionServiceAsync {

	void Hello(AsyncCallback<User> callback);

}
