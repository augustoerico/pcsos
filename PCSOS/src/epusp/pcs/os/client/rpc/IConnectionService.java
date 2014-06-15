package epusp.pcs.os.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import epusp.pcs.os.model.User;

@RemoteServiceRelativePath("connection")
public interface IConnectionService extends RemoteService {
	
	public User Hello() throws Exception;
}
