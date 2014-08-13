package epusp.pcs.os.login.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import epusp.pcs.os.model.person.user.User;

@RemoteServiceRelativePath("loginService")
public interface ILoginService extends RemoteService {

	User loginDetails(String token);

}
