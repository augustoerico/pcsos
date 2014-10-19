package epusp.pcs.os.shared.model.exception;

public class AttributeCastException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public AttributeCastException(){
	}
	
	public AttributeCastException(String message){
		super(message);
	}

}
