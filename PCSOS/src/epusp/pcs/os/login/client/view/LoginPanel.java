package epusp.pcs.os.login.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import epusp.pcs.os.login.client.LoginResources;
import epusp.pcs.os.login.client.presenter.LoginController.Display;

public class LoginPanel extends Composite implements Display {

	private static LoginPanelUiBinder uiBinder = GWT.create(LoginPanelUiBinder.class);
	
	interface LoginPanelUiBinder extends UiBinder<Widget, LoginPanel> {
	}
	
	@UiField
	Image logo, loginButton;
	
	@UiField
	AbsolutePanel panel;
	
	@UiField
	Label unauthorized;
	
	interface MyStyle extends CssResource {
	    String showUnauthorizedAcess();
	  }
	
	@UiField
	MyStyle style;
	
	LoginResources resources = LoginResources.INSTANCE;
	
	public LoginPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		logo.setResource(resources.logo());
		loginButton.setResource(resources.signIn());
	}

	@Override
	public void showUnauthorizedAcess(){
		unauthorized.addStyleName(style.showUnauthorizedAcess());
	}
	
	@Override
	public void hideUnauthorizedAcess(){
		unauthorized.removeStyleName(style.showUnauthorizedAcess());
	}
	
	@Override
	public HasClickHandlers getLoginButton() {
		return loginButton;
	}
	
	@Override
	public Widget asWidget() {
		return this;
	}

}
