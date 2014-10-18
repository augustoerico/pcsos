package epusp.pcs.os.shared.exception;

import java.io.Serializable;

public class DeniedAccess extends Exception implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public DeniedAccess(){
		super("Denied access.");
	}

}
