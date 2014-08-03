package epusp.pcs.os.login.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event;

import epusp.pcs.os.login.client.event.AuthenticationEvent.AuthenticationHandler;

public class AuthenticationEvent extends Event<AuthenticationHandler> {
	public static final Type<AuthenticationHandler> TYPE = new Type<AuthenticationHandler>();

	
	public interface AuthenticationHandler extends EventHandler {
		public void onAuthenticationRequest(AuthenticationEvent authenticationEvent);
	}

	private String password, username;

	public AuthenticationEvent(String username, String password){
		this.password = password;
		this.username = username;
	}
	
	public String getPassword(){
		return password;
	}
	
	public String getUsername(){
		return username;
	}
	
	@Override
	public Type<AuthenticationHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AuthenticationHandler handler) {
		handler.onAuthenticationRequest(this);
	}
}
