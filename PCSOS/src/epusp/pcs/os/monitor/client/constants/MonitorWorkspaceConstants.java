package epusp.pcs.os.monitor.client.constants;

import epusp.pcs.os.shared.client.constants.CommonWorkspaceConstants;

public interface MonitorWorkspaceConstants extends CommonWorkspaceConstants{

	@DefaultStringValue("Welcome to Monitor Workspace!")
	String welcome();
	
	@DefaultStringValue("Victim")
	String victim();
	
	@DefaultStringValue("Show or hide traffic details")
	String showHideTraffic();
	
	@DefaultStringValue("Reinforcements request")
	String reinforcements();
	
	@DefaultStringValue("Map")
	String map();
	
	@DefaultStringValue("Emergency call info")
	String info();
	
	@DefaultStringValue("Preferred language")
	String preferredLanguage();
	
	@DefaultStringValue("Cancel")
	String cancel();
	
	@DefaultStringValue("Save")
	String save();
	
	@DefaultStringValue("Victim Details")
	String victimHeader();
	
	@DefaultStringValue("Given Name")
	String givenName();
	
	@DefaultStringValue("Surname")
	String surname();
}
