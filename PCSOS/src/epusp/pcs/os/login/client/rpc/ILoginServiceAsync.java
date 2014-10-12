package epusp.pcs.os.login.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

import epusp.pcs.os.login.shared.URLConfig;
import epusp.pcs.os.shared.client.rpc.IConnectionServiceAsync;

public interface ILoginServiceAsync extends IConnectionServiceAsync{

	void login(String token, AsyncCallback<URLConfig> callback);

}
