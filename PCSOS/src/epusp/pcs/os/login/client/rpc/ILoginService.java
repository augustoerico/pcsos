package epusp.pcs.os.login.client.rpc;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import epusp.pcs.os.login.shared.LoginConfig;
import epusp.pcs.os.shared.client.rpc.IConnectionService;
import epusp.pcs.os.shared.exception.DeniedAccess;

@RemoteServiceRelativePath("loginService")
public interface ILoginService extends IConnectionService {
	LoginConfig login(String token) throws DeniedAccess;
}
