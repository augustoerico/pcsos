package epusp.pcs.os.superuser.client.presenter;

import java.util.List;

import com.google.gwt.user.client.ui.HasWidgets;

import epusp.pcs.os.shared.client.constants.CommonWorkspaceConstants;
import epusp.pcs.os.shared.client.rpc.IConnectionServiceAsync;
import epusp.pcs.os.shared.model.attribute.AttributeInfo;
import epusp.pcs.os.shared.model.person.user.superuser.SuperUser;

public class UpdateSuperUserPresenter extends CreateSuperUserPresenter {

	private final SuperUser superUser;
	
	public UpdateSuperUserPresenter(IConnectionServiceAsync rpcService,
			Display view, CommonWorkspaceConstants constants, List<AttributeInfo> customAttributes, SuperUser superUser) {
		super(rpcService, view, constants, customAttributes);
		this.superUser = superUser;
	}
	
	@Override
	public void go(HasWidgets container){
		super.go(container);

		getSurnameTextBox().setText(superUser.getSurname());
		getSurnameTextBox().setReadOnly(true);

		getGivenNameTextBox().setText(superUser.getName() + " " + superUser.getSecondName());
		getGivenNameTextBox().setReadOnly(true);

		getEmailTextBox().setText(superUser.getEmail());
		getEmailTextBox().setReadOnly(true);

		getGoogleIdTextBox().setText(superUser.getGoogleUserId());
		getGoogleIdTextBox().setReadOnly(true);


		getActiveCheckBox().setValue(superUser.isActive());

		setPictureUrl(superUser.getPictureURL());
		getView().setPictureUrl(superUser.getPictureURL());
		getView().showPicture();
		
		addValuesToCustomWidgets(superUser);
	}

}
