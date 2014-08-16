package epusp.pcs.os.model.person.user;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.gwt.user.client.rpc.IsSerializable;

import epusp.pcs.os.model.licence.Licence;

@PersistenceCapable
public class Agent extends User implements IsSerializable {

	@NotPersistent
	public static final AccountTypes accountType = AccountTypes.Agent;
	
	@Persistent
	private List<Licence> licences = new ArrayList<Licence>();
	
	public Agent(String name, String surname){
		super(name, surname);
	}
	
	public Agent(String name, String secondName, String surname){
		super(name, secondName, surname);
	}
	
	public void addLicence(Licence licence){
		licences.add(licence);
	}
	
	public List<Licence> getLicences(){
		return licences;
	}
	
	@Override
	public AccountTypes getType() {
		return accountType;
	}
	
	/*
	 * Seen by IsSerializable
	 */
	public Agent(){
		super();
	}
}
