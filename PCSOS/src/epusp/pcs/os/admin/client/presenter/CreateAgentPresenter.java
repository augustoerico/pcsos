package epusp.pcs.os.admin.client.presenter;

import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DatePicker;

import epusp.pcs.os.admin.client.rpc.IAdminWorkspaceServiceAsync;
import epusp.pcs.os.shared.client.constants.CommonWorkspaceConstants;
import epusp.pcs.os.shared.client.event.ClosePopupEvent;
import epusp.pcs.os.shared.client.event.EventBus;
import epusp.pcs.os.shared.client.presenter.CreatePersonPresenter;
import epusp.pcs.os.shared.model.attribute.AttributeInfo;
import epusp.pcs.os.shared.model.licence.DrivingCategories;
import epusp.pcs.os.shared.model.licence.DrivingLicence;
import epusp.pcs.os.shared.model.licence.HelicopterLicense;
import epusp.pcs.os.shared.model.licence.HelicopterLicenseTypes;
import epusp.pcs.os.shared.model.licence.License;
import epusp.pcs.os.shared.model.licence.LicenseTypes;
import epusp.pcs.os.shared.model.person.user.agent.Agent;

public class CreateAgentPresenter extends CreatePersonPresenter {

	public CreateAgentPresenter(IAdminWorkspaceServiceAsync rpcService,
			Display view, CommonWorkspaceConstants constants, List<AttributeInfo> customAttributes) {
		super(rpcService, view, constants, customAttributes);
	}
	
	private TextBox registerCode;
	private CheckBox isAcategory;
	private DatePicker effectiveUntil;
	private ListBox licence;
	private ListBox category;
	
	@Override
	public void go(HasWidgets container){		
		final Display view = getView();
		final CommonWorkspaceConstants constants = getConstants();
		
		super.go(container);
		
		registerCode = new TextBox();
		effectiveUntil = new DatePicker();
		licence = new ListBox();
		
		view.addSecondaryAttribute(constants.registerCode(), false, registerCode);
		view.addSecondaryAttribute(constants.effectiveUntil(), false, effectiveUntil);
		licence.addItem("");
		for(LicenseTypes type : LicenseTypes.values()){
			licence.addItem(type.getText(), type.name());
		}
		view.addSecondaryAttribute(constants.licence(), false, licence);
		licence.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				String l = licence.getValue(licence.getSelectedIndex());
				if(l.equals("")){
					view.removeSecondaryAttribute(category);
					view.removeSecondaryAttribute(isAcategory);
				}else{
					switch (LicenseTypes.valueOf(l)) {
					case DrivingLicence:
						category = new ListBox();
						isAcategory = new CheckBox();
						category.addItem("");
						for(DrivingCategories drivingCategory : DrivingCategories.values()){
							category.addItem(drivingCategory.name());
						}
						view.addSecondaryAttribute(constants.drivingCategory(), false, category);
						view.addSecondaryAttribute(constants.isA(), false, isAcategory);
						break;
					case HelicopterLicence:
						category = new ListBox();
						category.addItem("");
						for(HelicopterLicenseTypes helicopterCategory : HelicopterLicenseTypes.values()){
							category.addItem(helicopterCategory.getText());
						}
						view.addSecondaryAttribute(constants.drivingCategory(), false, category);
						break;
					default:
						break;
					}
				}
			}
		});
		
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
				
				if(licence.getSelectedIndex() != 0){
					 
					String licenceType = licence.getValue(licence.getSelectedIndex());
					License l = null;
					
					switch (LicenseTypes.valueOf(licenceType)) {
					case DrivingLicence:
						l = new DrivingLicence(agent, registerCode.getText(), effectiveUntil.getValue());
						break;
					case HelicopterLicence:
						l = new HelicopterLicense(agent, registerCode.getText(), effectiveUntil.getValue());
						break;
					default:
						break;
					}
					
					if(l != null)
						agent.addLicence(l);
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
	
	protected TextBox getRegisterCodeTextBox(){
		return registerCode;
	}
	
	protected CheckBox getIsAcategoryCheckBox(){
		return isAcategory;
	}
	
	protected DatePicker getEffectiveUntilDatePicker(){
		return effectiveUntil;
	}
	
	protected ListBox getLicenceListBox(){
		return licence;
	}
	
	protected ListBox getCategoryListBox(){
		return category;
	}
	
	@Override
	protected IAdminWorkspaceServiceAsync getRpcService(){
		return (IAdminWorkspaceServiceAsync) super.getRpcService();
	}
}
