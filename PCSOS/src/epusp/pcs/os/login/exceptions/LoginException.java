package epusp.pcs.os.login.exceptions;

import com.google.gwt.user.client.rpc.IsSerializable;

public class LoginException extends Exception implements IsSerializable {

	private static final long serialVersionUID = 1L;

	public LoginException(String message) {
		super(message);
	}

	public LoginException(Throwable cause) {
		super(cause);
	}

	public LoginException(String message, Throwable cause) {
		super(message, cause);
	}

//	public LoginException(String message, Throwable cause,
//			boolean enableSuppression, boolean writableStackTrace) {
//		super(message, cause, enableSuppression, writableStackTrace);
//	}
//	
	/*
	 * Seen by IsSerializable
	 */
	public LoginException() {
		super();
	}
}
