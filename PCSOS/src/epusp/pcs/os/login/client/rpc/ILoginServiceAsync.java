package epusp.pcs.os.login.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

import epusp.pcs.os.login.shared.URLConfig;

public interface ILoginServiceAsync {

	void login(String token, AsyncCallback<URLConfig> callback);

}
