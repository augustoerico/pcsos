package epusp.pcs.os.shared.model.vehicle;

import com.google.gwt.core.client.GWT;

import epusp.pcs.os.shared.client.constants.CommonWorkspaceConstants;

public enum Priority {
	PRIMARY, SUPPORT;
	
	public String getText(){
		CommonWorkspaceConstants constants = GWT.create(CommonWorkspaceConstants.class);
		switch (this) {
		case PRIMARY:
			return constants.primary();
		case SUPPORT:
			return constants.support();
		default:
			return "";
		}
	}
}
