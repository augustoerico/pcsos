package epusp.pcs.os.admin.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;

import epusp.pcs.os.admin.client.rpc.IAdminWorkspaceServiceAsync;
import epusp.pcs.os.shared.client.constants.CommonWorkspaceConstants;
import epusp.pcs.os.shared.model.person.Victim;

public class UpdateVictimPresenter extends CreateVictimPresenter {

	private final Victim victim;
	
	public UpdateVictimPresenter(IAdminWorkspaceServiceAsync rpcService,
			Display view, CommonWorkspaceConstants constants, Victim victim) {
		super(rpcService, view, constants);
		this.victim = victim;
	}

	@Override
	public void go(HasWidgets container){
		super.go(container);
		
		getGivenNameTextBox().setReadOnly(false);
		getGivenNameTextBox().setText(victim.getName() + " " + victim.getSecondName());
		
		getActiveCheckBox().setValue(victim.isActive());
		
		getEmailTextBox().setReadOnly(false);
		getEmailTextBox().setText(victim.getEmail());
		
		getSurnameTextBox().setReadOnly(false);
		getSurnameTextBox().setText(victim.getSurname());
		
		getGoogleIdTextBox().setReadOnly(false);
		getGoogleIdTextBox().setText(victim.getGoogleUserId());
		
		getView().setPictureUrl(victim.getPictureURL());
		getView().showPicture();
		setPictureUrl(victim.getPictureURL());
	}
	
}
