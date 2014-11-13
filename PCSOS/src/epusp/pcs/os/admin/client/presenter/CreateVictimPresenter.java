package epusp.pcs.os.admin.client.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.TextBox;

import epusp.pcs.os.admin.client.rpc.IAdminWorkspaceServiceAsync;
import epusp.pcs.os.shared.client.constants.CommonWorkspaceConstants;
import epusp.pcs.os.shared.client.event.ClosePopupEvent;
import epusp.pcs.os.shared.client.event.EventBus;
import epusp.pcs.os.shared.client.presenter.CreateUpdatePresenter;
import epusp.pcs.os.shared.model.person.Victim;

public class CreateVictimPresenter extends CreateUpdatePresenter{
	
	public CreateVictimPresenter(IAdminWorkspaceServiceAsync rpcService,
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
		super.go(container);
		bind();
	}
	
	private void bind(){
		final Display view = getView();
		view.setSaveEnabled(true);
		view.addSaveClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				String[] names = givenName.getText().split("\\s+");
				String firstName = names[0], secondName = "";
				for(int i = 1; i < names.length; i++){
					secondName = secondName.concat(names[i]);
					if(i+1 < names.length)
						secondName = secondName.concat(" ");
				}
				
				Victim victim = null;
				if(!secondName.equals(""))
					victim = new Victim(firstName, surname.getText(), email.getText());
				else
					victim = new Victim(firstName, secondName, surname.getText(), email.getText());
				
				victim.setGoogleUserId(googleId.getText());
				victim.setIsActive(active.getValue());
				
				victim.setPictureURL(getPictureUrl());
				
				getRpcService().createVictim(victim, new AsyncCallback<Void>() {
					
					@Override
					public void onSuccess(Void result) {
						EventBus.get().fireEvent(new ClosePopupEvent());
					}
					
					@Override
					public void onFailure(Throwable caught) {
					}
				});
			}
		});
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

	@Override
	protected IAdminWorkspaceServiceAsync getRpcService(){
		return (IAdminWorkspaceServiceAsync) super.getRpcService();
	}
}
