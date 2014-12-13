package epusp.pcs.os.admin.client.presenter;

import java.util.List;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasWidgets;

import epusp.pcs.os.admin.client.rpc.IAdminWorkspaceServiceAsync;
import epusp.pcs.os.shared.client.constants.CommonWorkspaceConstants;
import epusp.pcs.os.shared.client.view.LicenseInput;
import epusp.pcs.os.shared.model.attribute.AttributeInfo;
import epusp.pcs.os.shared.model.licence.DrivingLicense;
import epusp.pcs.os.shared.model.licence.License;
import epusp.pcs.os.shared.model.person.user.agent.Agent;

public class UpdateAgentPresenter extends CreateAgentPresenter{

	private Agent agent;

	public UpdateAgentPresenter(IAdminWorkspaceServiceAsync rpcService,
			Display view, CommonWorkspaceConstants constants, List<AttributeInfo> customAttributes, Agent agent) {
		super(rpcService, view, constants, customAttributes);
		this.agent = agent;
	}

	@Override
	public void go(HasWidgets container){
		super.go(container);

		getSurnameTextBox().setText(agent.getSurname());
		getSurnameTextBox().setReadOnly(true);

		if(agent.getSecondName() != null)
			getGivenNameTextBox().setText(agent.getName() + " " + agent.getSecondName());
		else
			getGivenNameTextBox().setText(agent.getName());
		getGivenNameTextBox().setReadOnly(true);

		getEmailTextBox().setText(agent.getEmail());
		getEmailTextBox().setReadOnly(true);

		getGoogleIdTextBox().setText(agent.getGoogleUserId());
		getGoogleIdTextBox().setReadOnly(true);


		getActiveCheckBox().setValue(agent.isActive());

		setPictureUrl(agent.getPictureURL());
		getView().setPictureUrl(agent.getPictureURL());
		getView().showPicture();

		List<License> licenses = agent.getLicenses();
		
		for(License license : licenses){
			System.out.println("license: " + license.getLicenceType().name());
			LicenseInput licenseInput = getLicenseInput(license.getLicenceType());
			licenseInput.setEffectiveUntil(license.validUntil());
			licenseInput.setRegisterCode(license.getRegisterCode());
			licenseInput.setSelectedCategory(license.getLicenseCategory().name());
			switch (license.getLicenceType()) {
			case DrivingLicence:
				DrivingLicense drivingLicense = (DrivingLicense) license;
				if(drivingLicense.hasAcategory()){
					CheckBox checkBox = (CheckBox) licenseInput.getWidget("A");
					checkBox.setValue(true);
				}
				break;
			case HelicopterLicense:
				break;
			default:
				break;
			}
		}
		
		
		addValuesToCustomWidgets(agent);
	}

}