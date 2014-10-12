package epusp.pcs.os.login.client.rpc;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import epusp.pcs.os.login.shared.URLConfig;
import epusp.pcs.os.shared.client.rpc.IConnectionService;

@RemoteServiceRelativePath("loginService")
public interface ILoginService extends IConnectionService {
	URLConfig login(String token);
}
