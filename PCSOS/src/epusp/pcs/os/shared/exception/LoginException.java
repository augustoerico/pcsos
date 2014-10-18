package epusp.pcs.os.shared.exception;

import java.io.Serializable;

public class LoginException extends Exception implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public LoginException(){
		super("Login action failed.");
	}

}
