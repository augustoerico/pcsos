package epusp.pcs.os.login.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;

import epusp.pcs.os.shared.client.SharedResources;

public interface LoginResources extends SharedResources {
	
	public static final LoginResources INSTANCE = GWT.create(LoginResources.class);
	
	@Source("images/logo.jpg")
	ImageResource logo();
	
	@Source("images/sign-in-with-google.png")
	ImageResource signIn();
	
}
