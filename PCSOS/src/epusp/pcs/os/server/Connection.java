package epusp.pcs.os.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import epusp.pcs.os.shared.client.rpc.IConnectionService;

public class Connection extends RemoteServiceServlet implements IConnectionService{

	private static final long serialVersionUID = 1L;

	public String Hello() throws Exception {
		return "Helo World!";
	}
	
}
