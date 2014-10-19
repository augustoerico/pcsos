package epusp.pcs.os.shared.model.person.user;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;

import com.google.gwt.user.client.rpc.IsSerializable;

@PersistenceCapable(identityType=IdentityType.APPLICATION, detachable="true")
public class Auditor extends User implements IsSerializable {

	@NotPersistent
	private static final long serialVersionUID = 1L;
	
	public Auditor(String name, String surname, String email){
		super(name, surname, email);
	}
	
	public Auditor(String name, String secondName, String surname, String email){
		super(name, secondName, surname, email);
	}
	
	@Override
	public AccountTypes getType() {
		return AccountTypes.Auditor;
	}
	
	/*
	 * Seen by Serializable
	 */
	public Auditor(){
		super();
	}
}
