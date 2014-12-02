package epusp.pcs.os.admin.client.presenter;

import java.util.List;

import com.google.gwt.user.client.ui.HasWidgets;

import epusp.pcs.os.admin.client.rpc.IAdminWorkspaceServiceAsync;
import epusp.pcs.os.shared.client.constants.CommonWorkspaceConstants;
import epusp.pcs.os.shared.model.attribute.AttributeInfo;
import epusp.pcs.os.shared.model.person.victim.Victim;

public class UpdateVictimPresenter extends CreateVictimPresenter {

	private final Victim victim;
	
	public UpdateVictimPresenter(IAdminWorkspaceServiceAsync rpcService,
			Display view, CommonWorkspaceConstants constants,  List<AttributeInfo> customAttributes, Victim victim) {
		super(rpcService, view, constants, customAttributes);
		this.victim = victim;
	}

	@Override
	public void go(HasWidgets container){
		super.go(container);
		
		getGivenNameTextBox().setReadOnly(true);
		
		if(victim.getSecondName() != null)
			getGivenNameTextBox().setText(victim.getName() + " " + victim.getSecondName());
		else
			getGivenNameTextBox().setText(victim.getName());
		
		getActiveCheckBox().setValue(victim.isActive());
		
		getEmailTextBox().setReadOnly(true);
		getEmailTextBox().setText(victim.getEmail());
		
		getSurnameTextBox().setReadOnly(true);
		getSurnameTextBox().setText(victim.getSurname());
		
		getGoogleIdTextBox().setReadOnly(true);
		getGoogleIdTextBox().setText(victim.getGoogleUserId());
		
		getView().setPictureUrl(victim.getPictureURL());
		getView().showPicture();
		setPictureUrl(victim.getPictureURL());
		
		addValuesToCustomWidgets(victim);
	}
	
}
