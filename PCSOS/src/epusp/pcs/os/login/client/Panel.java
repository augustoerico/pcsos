package epusp.pcs.os.login.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;

import epusp.pcs.os.login.client.presenter.LoginPresenter;
import epusp.pcs.os.login.client.rpc.ILoginService;
import epusp.pcs.os.login.client.rpc.ILoginServiceAsync;
import epusp.pcs.os.login.client.view.LoginPanel;

public class Panel implements EntryPoint {

	RootPanel panel = RootPanel.get();
	
	@Override
	public void onModuleLoad() {
		ILoginServiceAsync loginService = GWT.create(ILoginService.class);
		LoginPresenter loginController = new LoginPresenter(loginService, new LoginPanel());
		loginController.go(RootLayoutPanel.get());
	}




}
