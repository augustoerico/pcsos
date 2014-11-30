package epusp.pcs.os.superuser.client.presenter;

import java.util.List;

import com.google.gwt.user.client.ui.HasWidgets;

import epusp.pcs.os.shared.client.constants.CommonWorkspaceConstants;
import epusp.pcs.os.shared.client.rpc.IConnectionServiceAsync;
import epusp.pcs.os.shared.model.attribute.AttributeInfo;
import epusp.pcs.os.shared.model.person.user.admin.Admin;

public class UpdateAdminPresenter extends CreateAdminPresenter {

	private final Admin admin;
	
	public UpdateAdminPresenter(IConnectionServiceAsync rpcService,
			Display view, CommonWorkspaceConstants constants, List<AttributeInfo> customAttributes, Admin admin) {
		super(rpcService, view, constants, customAttributes);
		this.admin = admin;
	}
	
	@Override
	public void go(HasWidgets container){
		super.go(container);

		getSurnameTextBox().setText(admin.getSurname());
		getSurnameTextBox().setReadOnly(true);

		getGivenNameTextBox().setText(admin.getName() + " " + admin.getSecondName());
		getGivenNameTextBox().setReadOnly(true);

		getEmailTextBox().setText(admin.getEmail());
		getEmailTextBox().setReadOnly(true);

		getGoogleIdTextBox().setText(admin.getGoogleUserId());
		getGoogleIdTextBox().setReadOnly(true);


		getActiveCheckBox().setValue(admin.isActive());

		setPictureUrl(admin.getPictureURL());
		getView().setPictureUrl(admin.getPictureURL());
		getView().showPicture();
		
		addValuesToCustomWidgets(admin);
	}

}
