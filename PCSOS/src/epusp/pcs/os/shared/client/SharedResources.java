package epusp.pcs.os.shared.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface SharedResources extends ClientBundle {
	
	public static final SharedResources INSTANCE = GWT.create(SharedResources.class);
	
	@Source("images/logo-white.png")
	ImageResource darkLogo();
	
	@Source("images/logo.png")
	ImageResource logo();
	
	@Source("images/user.png")
	ImageResource user();
	
	@Source("images/preferences.png")
	ImageResource preferences();
	
	@Source("images/logout.png")
	ImageResource logout();

	@Source("images/add.png")
	ImageResource add();
}
