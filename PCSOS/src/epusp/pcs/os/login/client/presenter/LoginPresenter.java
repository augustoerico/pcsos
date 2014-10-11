package epusp.pcs.os.login.client.presenter;

import com.google.api.gwt.oauth2.client.Auth;
import com.google.api.gwt.oauth2.client.AuthRequest;
import com.google.gwt.core.client.Callback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import epusp.pcs.os.login.client.rpc.ILoginServiceAsync;
import epusp.pcs.os.login.shared.URLConfig;
import epusp.pcs.os.shared.client.presenter.Presenter;

public class LoginPresenter implements Presenter{
	
	private final static String AUTH_URL = "https://accounts.google.com/o/oauth2/auth";
	private final static String CLIENT_ID = "1043837675219-q4pthfju82obio990mn3l5adnuajo97b.apps.googleusercontent.com"; // available from the APIs console
	private final static String EMAIL_SCOPE = "https://www.googleapis.com/auth/userinfo.email";
	
	private final ILoginServiceAsync loginService;
	private final Display view;
	
	public interface Display {
		HasClickHandlers getLoginButton();
		Widget asWidget();
		void showUnauthorizedAcess();
		void hideUnauthorizedAcess();
	}

	public LoginPresenter(ILoginServiceAsync loginService, Display view){
		this.loginService = loginService;
		this.view = view;
	}

	@Override
	public void go(final HasWidgets container) {
		bind();
		container.clear();
		container.add(view.asWidget());
	}
	
	private void bind(){
		view.getLoginButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				authenticate();
			}
		});
	}
	
	private void authenticate(){
		AuthRequest req = new AuthRequest(AUTH_URL, CLIENT_ID)
		.withScopes(EMAIL_SCOPE); // Can specify multiple scopes here

		Auth.get().login(req, new Callback<String, Throwable>() {
			@Override
			public void onSuccess(String token) {
				loginService.login(token, new AsyncCallback<URLConfig>() {

					@Override
					public void onSuccess(URLConfig result) {
						redirect(result);
					}

					@Override
					public void onFailure(Throwable caught) {
						//to do
					}
				});
			}
			@Override
			public void onFailure(Throwable caught) {
				// The authentication process failed for some reason, see caught.getMessage()
			}
		});
	}
	
	private void redirect(URLConfig url){
		if(url == null)
			view.showUnauthorizedAcess();
		else{
			String path = Window.Location.getProtocol().concat("//").concat(Window.Location.getHost()).concat("/").concat(url.getUrlPath()).concat(Window.Location.getQueryString());
			if(!path.contains("?"))
				path = path.concat("?").concat("locale=").concat(url.getLocale());
			else
				path = path.concat("&").concat("locale=").concat(url.getLocale());
			Window.Location.replace(path);
		}
	}
}
