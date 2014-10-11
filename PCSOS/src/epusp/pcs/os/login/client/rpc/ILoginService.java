package epusp.pcs.os.login.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import epusp.pcs.os.login.shared.URLConfig;

@RemoteServiceRelativePath("loginService")
public interface ILoginService extends RemoteService {
	URLConfig login(String token);
}
