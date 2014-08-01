package epusp.pcs.os.model.user;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import epusp.pcs.os.model.licence.Licence;

public class Agent extends User implements IsSerializable {

	public static final AccountTypes accountType = AccountTypes.Agent;
	
	private List<Licence> licences = new ArrayList<Licence>();
	
	public void addLicence(Licence licence){
		licences.add(licence);
	}
	
	public List<Licence> getLicences(){
		return licences;
	}
	
	
	/*
	 * Seen by IsSerializable
	 */
	public Agent(){
		super();
	}

}
