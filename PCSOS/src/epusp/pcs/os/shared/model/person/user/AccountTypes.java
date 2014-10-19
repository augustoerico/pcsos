package epusp.pcs.os.shared.model.person.user;

import com.google.gwt.user.client.rpc.IsSerializable;

public enum AccountTypes implements IsSerializable {
	Admin, Auditor, Monitor, SuperUser, Agent;
	
	/*
	 * Seen by IsSerializable
	 */
	AccountTypes(){
	}
}
