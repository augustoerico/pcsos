package epusp.pcs.os.superuser.client;

import com.google.gwt.core.client.GWT;

import epusp.pcs.os.shared.client.SharedResources;

public interface SuperUserResources extends SharedResources {
	
	public static final SuperUserResources INSTANCE = GWT.create(SuperUserResources.class);
	
	
}