package epusp.pcs.os.superuser.client.constants;

import epusp.pcs.os.shared.client.constants.CommonWorkspaceConstants;

public interface SuperUserWorkspaceConstants extends CommonWorkspaceConstants{
	
	@DefaultStringValue("Admin")
	String admin();
	
	@DefaultStringValue("Monitor")
	String monitor();
	
	@DefaultStringValue("SuperUser")
	String superUser();
}
