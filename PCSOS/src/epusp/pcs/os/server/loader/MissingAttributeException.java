package epusp.pcs.os.server.loader;

public class MissingAttributeException extends Exception{

	private static final long serialVersionUID = 1L;

	public MissingAttributeException(){
		super();
	}
	
	public MissingAttributeException(String message){
		super(message);
	}
}
