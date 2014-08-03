package epusp.pcs.os.login.client;

import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;

import epusp.pcs.os.login.client.event.AuthenticationEvent;
import epusp.pcs.os.login.client.event.AuthenticationEvent.AuthenticationHandler;
import epusp.pcs.os.login.client.event.EventBus;
import epusp.pcs.os.login.client.rpc.ILoginService;
import epusp.pcs.os.login.client.rpc.ILoginServiceAsync;
import epusp.pcs.os.login.client.widget.LoginPanel;
import epusp.pcs.os.model.person.user.User;

public class Panel implements EntryPoint, AuthenticationHandler {

	private static final Logger logger = Logger.getLogger(Panel.class
			.getName());

	private ILoginServiceAsync loginService = GWT.create(ILoginService.class);

	final RootLayoutPanel rp = RootLayoutPanel.get();

	@Override
	public void onModuleLoad() {

		EventBus.get().addHandler(AuthenticationEvent.TYPE, this);

		loginService.Hello(new AsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println(result);
				rp.add(new LoginPanel());
			}

			@Override
			public void onFailure(Throwable caught) {
				logger.severe("Unable to greet - " + caught.getMessage());
			}
		});

	}

	@Override
	public void onAuthenticationRequest(AuthenticationEvent authenticationEvent) {

		loginService.login("giovanni.gatti@usp.br", "epusp.pcsos", new AsyncCallback<User>() {

			@Override
			public void onSuccess(User result) {
				System.out.println(result.getLogin() + ", " + result.getName());
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stu
			}
		});
	}

}
