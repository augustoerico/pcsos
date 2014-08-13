package epusp.pcs.os.login.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import epusp.pcs.os.shared.client.LoginInfo;

@RemoteServiceRelativePath("loginService")
public interface ILoginService extends RemoteService {

	LoginInfo loginDetails(String token);

}
