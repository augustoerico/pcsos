package epusp.pcs.os.admin.client.constants;

import epusp.pcs.os.shared.client.constants.CommonWorkspaceConstants;

public interface AdminWorkspaceConstants extends CommonWorkspaceConstants{
	
	@DefaultStringValue("Client")
	String client();
	
	@DefaultStringValue("Agent")
	String agent();
	
	@DefaultStringValue("Vehicle")
	String vehicle();
	
}
