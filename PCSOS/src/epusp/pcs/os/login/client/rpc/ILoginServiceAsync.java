package epusp.pcs.os.login.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

import epusp.pcs.os.shared.client.LoginInfo;

public interface ILoginServiceAsync {

	void loginDetails(String token, AsyncCallback<LoginInfo> callback);

}
