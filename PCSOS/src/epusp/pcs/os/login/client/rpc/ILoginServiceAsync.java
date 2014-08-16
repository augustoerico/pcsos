package epusp.pcs.os.login.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ILoginServiceAsync {

	void login(String token, AsyncCallback<String> callback);

}
