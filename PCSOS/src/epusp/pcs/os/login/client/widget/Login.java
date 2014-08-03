package epusp.pcs.os.login.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import epusp.pcs.os.login.client.LoginResources;
import epusp.pcs.os.login.client.event.AuthenticationEvent;
import epusp.pcs.os.login.client.event.EventBus;

public class Login extends Composite {

	private static LoginUiBinder uiBinder = GWT.create(LoginUiBinder.class);

	LoginResources images = LoginResources.INSTANCE;

	@UiField
	Image sendIcon;
	
	@UiField
	Image lock;

	@UiField
	Image user;

	@UiField
	AbsolutePanel usernamePanel, passwordPanel;

	@UiField
	TextBox usernameBox;

	@UiField
	PasswordTextBox passwordBox;

	private Boolean clickedOnUsernameBox = true;

	private Boolean clickedOnPasswordBox = true;

	interface LoginUiBinder extends UiBinder<Widget, Login> {
	}

	public Login() {
		initWidget(uiBinder.createAndBindUi(this));

		sendIcon.setResource(images.send());
		lock.setResource(images.lock());
		lock.setPixelSize(60, 36);
		lock.setTitle("password");
		passwordPanel.setWidgetPosition(lock, 145, 1);
		user.setResource(images.user());
		user.setPixelSize(26, 26);
		user.setTitle("username");
		usernamePanel.setWidgetPosition(user, 162, 3);

	}

	@UiHandler("usernameBox")
	void handleUsernameBoxClick(ClickEvent event){
		if(clickedOnUsernameBox){
			usernameBox.setText("");
			clickedOnUsernameBox = false;
		}
	}

	@UiHandler("passwordBox")
	void handlePasswordBoxClick(ClickEvent event){
		if(clickedOnPasswordBox){
			passwordBox.setText("");
			clickedOnPasswordBox = false;
		}
	}

	
	@UiHandler("send")
	void handleSendClick(ClickEvent event){
		EventBus.get().fireEvent(new AuthenticationEvent(usernameBox.getValue(), passwordBox.getValue()));
	}
}
