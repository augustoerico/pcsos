package epusp.pcs.os.login.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("loginService")
public interface ILoginService extends RemoteService {
	String login(String token);
}
