package epusp.pcs.os.login.client;

import com.google.api.gwt.oauth2.client.Auth;
import com.google.api.gwt.oauth2.client.AuthRequest;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import epusp.pcs.os.login.client.rpc.ILoginService;
import epusp.pcs.os.login.client.rpc.ILoginServiceAsync;
import epusp.pcs.os.model.person.user.User;

public class Panel implements EntryPoint {

	private ILoginServiceAsync loginService = GWT.create(ILoginService.class);
	
	private final static String AUTH_URL = "https://accounts.google.com/o/oauth2/auth";
	private final static String CLIENT_ID = "1043837675219-q4pthfju82obio990mn3l5adnuajo97b.apps.googleusercontent.com"; // available from the APIs console
	private final static String EMAIL_SCOPE = "https://www.googleapis.com/auth/userinfo.email";

	@Override
	public void onModuleLoad() {

		AuthRequest req = new AuthRequest(AUTH_URL, CLIENT_ID)
		.withScopes(EMAIL_SCOPE); // Can specify multiple scopes here

		Auth.get().login(req, new Callback<String, Throwable>() {
			@Override
			public void onSuccess(String token) {
				loginService.loginDetails(token, new AsyncCallback<User>() {
					
					@Override
					public void onSuccess(User result) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}
				});
			}
			@Override
			public void onFailure(Throwable caught) {
				// The authentication process failed for some reason, see caught.getMessage()
			}
		});
	}



}
