package epusp.pcs.os.superuser.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;

import epusp.pcs.os.shared.client.SharedResources;

public interface SuperUserResources extends SharedResources {
	
	public static final SuperUserResources INSTANCE = GWT.create(SuperUserResources.class);
	
	@Source("images/new-admin.png")
	ImageResource newAdmin();
	
	@Source("images/new-monitor.png")
	ImageResource newMonitor();
	
	@Source("images/new-superuser.png")
	ImageResource newSuperUser();
}