package epusp.pcs.os.admin.client.presenter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasWidgets;

import epusp.pcs.os.admin.client.rpc.IAdminWorkspaceServiceAsync;
import epusp.pcs.os.shared.client.constants.CommonWorkspaceConstants;
import epusp.pcs.os.shared.client.event.ClosePopupEvent;
import epusp.pcs.os.shared.client.event.EventBus;
import epusp.pcs.os.shared.client.presenter.CreatePersonPresenter;
import epusp.pcs.os.shared.client.view.LicenseInput;
import epusp.pcs.os.shared.model.attribute.AttributeInfo;
import epusp.pcs.os.shared.model.licence.DrivingCategories;
import epusp.pcs.os.shared.model.licence.DrivingLicense;
import epusp.pcs.os.shared.model.licence.HelicopterLicense;
import epusp.pcs.os.shared.model.licence.HelicopterLicenseTypes;
import epusp.pcs.os.shared.model.licence.License;
import epusp.pcs.os.shared.model.licence.LicenseCategory;
import epusp.pcs.os.shared.model.licence.LicenseTypes;
import epusp.pcs.os.shared.model.person.user.agent.Agent;

public class CreateAgentPresenter extends CreatePersonPresenter {

	public CreateAgentPresenter(IAdminWorkspaceServiceAsync rpcService,
			Display view, CommonWorkspaceConstants constants, List<AttributeInfo> customAttributes) {
		super(rpcService, view, constants, customAttributes);
	}

	private HashMap<LicenseTypes, LicenseInput> categoriesMap = new HashMap<LicenseTypes, LicenseInput>();

	@Override
	public void go(HasWidgets container){		
		final Display view = getView();
		final CommonWorkspaceConstants constants = getConstants();

		super.go(container);

		for(LicenseTypes license : LicenseTypes.values()){
			LicenseInput licenseInput = new LicenseInput();
			licenseInput.setHeader(license.getText());
			licenseInput.setEffectiveUntilLabel(constants.effectiveUntil());
			licenseInput.setCategoryLabel(constants.license());
			licenseInput.setRegisterCodeLabel(constants.registerCode());

			licenseInput.addCategoryItem("");
			for(LicenseCategory category : license.getLicenseCategories()){
				licenseInput.addCategoryItem(category.getText(), category.name());
			}

			view.addSecondaryAttribute("", false, licenseInput);

			switch (license) {
			case DrivingLicence:
				licenseInput.addWidget("A", constants.isA(), new CheckBox());
				break;
			case HelicopterLicense:
				break;
			default:
				break;
			}

			categoriesMap.put(license, licenseInput);
		}

		addCustomAttributesToView();

		bind();
	}

	private void bind(){
		getView().addSaveClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String[] names = getGivenNameTextBox().getText().split("\\s+");
				String firstName = names[0], secondName = "";
				for(int i = 1; i < names.length; i++){
					secondName = secondName.concat(names[i]);
					if(i+1 < names.length)
						secondName = secondName.concat(" ");
				}

				Agent agent = null;
				if(!secondName.equals(""))
					agent = new Agent(firstName, getSurnameTextBox().getText(), getEmailTextBox().getText());
				else
					agent = new Agent(firstName, secondName, getSurnameTextBox().getText(), getEmailTextBox().getText());

				agent.setGoogleUserId(getGoogleIdTextBox().getText());
				agent.setIsActive(getActiveCheckBox().getValue());

				agent.setPictureURL(getPictureUrl());

				for(Entry<LicenseTypes, LicenseInput> entry : categoriesMap.entrySet()){
					LicenseTypes licenseType = entry.getKey();
					License license = null;
					LicenseInput licenseInput = entry.getValue();
					String registerCode = licenseInput.getRegisterCode();
					Date effectiveUntil = licenseInput.getEffectiveUntil();
					String category = licenseInput.getSelectedCategoty();
					if(!category.isEmpty()){
						switch(licenseType){
						case DrivingLicence:
							if(effectiveUntil != null)
								license = new DrivingLicense(agent, registerCode, effectiveUntil);
							else
								license = new DrivingLicense(agent, registerCode);

							Boolean  isA = ((CheckBox) licenseInput.getWidget("A")).getValue();
							((DrivingLicense) license).setHasAcategory(isA);
							
							DrivingCategories drivingCategory = DrivingCategories.valueOf(category);
							((DrivingLicense) license).setCategory(drivingCategory);
							break;
						case HelicopterLicense:
							if(effectiveUntil != null)
								license = new HelicopterLicense(agent, registerCode, effectiveUntil);
							else
								license = new HelicopterLicense(agent, registerCode);
							HelicopterLicenseTypes helicopterLicenseTypes = HelicopterLicenseTypes.valueOf(category);
							((HelicopterLicense) license).setCategory(helicopterLicenseTypes);
							break;
						default:
							break;
						}
						if(license != null){
							System.out.println("added license: " + license.getLicenceType().name());
							agent.addLicense(license);
						}
					}					
				}

				readValuesAndSaveOnObject(agent);

				getRpcService().createAgent(agent, new AsyncCallback<Void>() {

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

	protected LicenseInput getLicenseInput(LicenseTypes licenseType){
		return categoriesMap.get(licenseType);
	}

	@Override
	protected IAdminWorkspaceServiceAsync getRpcService(){
		return (IAdminWorkspaceServiceAsync) super.getRpcService();
	}
}
