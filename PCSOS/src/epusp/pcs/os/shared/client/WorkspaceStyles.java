package epusp.pcs.os.shared.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

public interface WorkspaceStyles extends ClientBundle {
	
	public static final WorkspaceStyles INSTANCE = GWT.create(WorkspaceStyles.class);

	@Source("CssResources/BackgroundStyles.css")
	BackgroundStyles backgroundStyles();

	@Source("images/admin-background.jpg")
	ImageResource adminBackground();
	
	@Source("images/superuser-background.jpg")
	ImageResource superuserBackgroung();

	interface BackgroundStyles extends CssResource {
		String adminBackground();
		String superuserBackground();
	}
}
