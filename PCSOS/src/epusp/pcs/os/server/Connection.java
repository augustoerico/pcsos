package epusp.pcs.os.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import epusp.pcs.os.client.rpc.IConnectionService;
import epusp.pcs.os.model.person.user.Monitor;
import epusp.pcs.os.model.person.user.User;

public class Connection extends RemoteServiceServlet implements IConnectionService{

	private static final long serialVersionUID = 1L;

	public User Hello() throws Exception {
		return new Monitor();
	}
	
}
