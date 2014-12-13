package epusp.pcs.os.shared.model.person.user;

import com.google.gwt.user.client.rpc.IsSerializable;

public enum AccountTypes implements IsSerializable {
	Admin, Auditor, Monitor, SuperUser, Agent;
	
	public Class<? extends User> getTargetClass(){
		switch (this) {
		case Admin:
			return epusp.pcs.os.shared.model.person.user.admin.Admin.class;
		case Agent:
			return epusp.pcs.os.shared.model.person.user.agent.Agent.class;
		case Auditor:
			return epusp.pcs.os.shared.model.person.user.Auditor.class;
		case Monitor:
			return epusp.pcs.os.shared.model.person.user.monitor.Monitor.class;
		case SuperUser:
			return epusp.pcs.os.shared.model.person.user.superuser.SuperUser.class;
		default:
			return null;
		}	
	}
	
	/*
	 * Seen by IsSerializable
	 */
	AccountTypes(){
	}
}
