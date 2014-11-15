package epusp.pcs.os.superuser.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;

import epusp.pcs.os.shared.client.SharedResources;

public interface SuperUserResources extends SharedResources {
	
	public static final SuperUserResources INSTANCE = GWT.create(SuperUserResources.class);
	
	@Source("images/admin.png")
	ImageResource admin();
	
	@Source("images/monitor.png")
	ImageResource monitor();
	
	@Source("images/superuser.png")
	ImageResource superuser();
}