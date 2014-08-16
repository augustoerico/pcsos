package epusp.pcs.os.model.person.user;

import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;

import com.google.gwt.user.client.rpc.IsSerializable;

@PersistenceCapable
public class Admin extends User implements IsSerializable {

	@NotPersistent
	public static final AccountTypes accountType = AccountTypes.Admin;
	
	public Admin(String name, String surname){
		super(name, surname);
	}
	
	public Admin(String name, String secondName, String surname){
		super(name, secondName, surname);
	}
	
	@Override
	public AccountTypes getType() {
		return accountType;
	}

	/*
	 * Seen by IsSerializable
	 */
	public Admin(){
		super();
	}
}
