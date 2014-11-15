package epusp.pcs.os.shared.client.presenter;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.TextBox;

import epusp.pcs.os.shared.client.constants.CommonWorkspaceConstants;
import epusp.pcs.os.shared.client.rpc.IConnectionServiceAsync;

public class CreatePersonPresenter extends CreateUpdatePresenter{

	public CreatePersonPresenter(IConnectionServiceAsync rpcService,
			Display view, CommonWorkspaceConstants constants) {
		super(rpcService, view, constants);
	}
	
	private TextBox givenName, surname, email, googleId;
	private CheckBox active;

	@Override
	public void go(HasWidgets container){
		givenName = new TextBox();
		surname = new TextBox();
		email = new TextBox();
		googleId = new TextBox();
		active = new CheckBox();
		Display view = getView();
		CommonWorkspaceConstants constants = getConstants();
		view.addPrimaryAttribute(constants.surname(), true, surname);
		view.addPrimaryAttribute(constants.name(), true, givenName);
		view.addPrimaryAttribute(constants.email(), true, email);
		view.addPrimaryAttribute(constants.googleId(), false, googleId);
		view.addPrimaryAttribute(constants.active(), true, active);
		view.setSaveEnabled(true);
		super.go(container);
		bind();
	}
	
	private void bind(){
		
	}
	
	
	protected TextBox getGivenNameTextBox(){
		return givenName;
	}
	
	protected TextBox getSurnameTextBox(){
		return surname;
	}
	
	protected TextBox getEmailTextBox(){
		return email;
	}
	
	protected TextBox getGoogleIdTextBox(){
		return googleId;
	}
	
	protected CheckBox getActiveCheckBox(){
		return active;
	}

}
