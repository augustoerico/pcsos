package epusp.pcs.os.login.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LoginPanel extends Composite {

	private static LoginPanelUiBinder uiBinder = GWT.create(LoginPanelUiBinder.class);
	
	interface LoginPanelUiBinder extends UiBinder<Widget, LoginPanel> {
	}
	
	@UiField
	HorizontalPanel hp;
	
	public LoginPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		
		hp.setSize(Integer.toString(Window.getClientWidth()).concat("px"), Integer.toString(Window.getClientHeight()).concat("px"));
	}

}
