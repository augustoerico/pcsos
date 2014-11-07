package epusp.pcs.os.admin.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;

import epusp.pcs.os.shared.client.SharedResources;

public interface AdminResources extends SharedResources {
	
	public static final AdminResources INSTANCE = GWT.create(AdminResources.class);
	
	@Source("images/helicopter.png")
	ImageResource helicopter();
	
	@Source("images/police-vehicle.png")
	ImageResource car();
	
}
