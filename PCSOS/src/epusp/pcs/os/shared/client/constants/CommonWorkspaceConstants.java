package epusp.pcs.os.shared.client.constants;

import com.google.gwt.i18n.client.Constants;

public interface CommonWorkspaceConstants extends Constants{

	@DefaultStringValue("Given Name")
	String name();
	
	@DefaultStringValue("Family Name")
	String surname();
	
	@DefaultStringValue("Email")
	String email();
	
	@DefaultStringValue("Picture")
	String picture();
	
	@DefaultStringValue("Plate")
	String plate();
	
	@DefaultStringValue("Priority")
	String priority();
	
	@DefaultStringValue("Tag")
	String tag();	
}
