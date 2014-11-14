package epusp.pcs.os.admin.client.presenter;

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
import epusp.pcs.os.shared.client.presenter.CreateUpdatePresenter;
import epusp.pcs.os.shared.model.licence.DrivingCategories;
import epusp.pcs.os.shared.model.licence.DrivingLicence;
import epusp.pcs.os.shared.model.licence.Licence;
import epusp.pcs.os.shared.model.licence.LicenceTypes;
import epusp.pcs.os.shared.model.person.user.Agent;

public class CreateAgentPresenter extends CreateUpdatePresenter {

	
	public CreateAgentPresenter(IAdminWorkspaceServiceAsync rpcService,
			Display view, CommonWorkspaceConstants constants) {
		super(rpcService, view, constants);
	}
	
	private TextBox registerCode, givenName, surname, email, googleId;
	private CheckBox isAcategory;
	private DatePicker effectiveUntil;
	private ListBox licence;
	private ListBox category;
	private CheckBox active;
	
	@Override
	public void go(HasWidgets container){
		final Display view = getView();
		final CommonWorkspaceConstants constants = getConstants();
		
		givenName = new TextBox();
		surname = new TextBox();
		email = new TextBox();
		googleId = new TextBox();
		active = new CheckBox();

		view.addPrimaryAttribute(constants.surname(), true, surname);
		view.addPrimaryAttribute(constants.name(), true, givenName);
		view.addPrimaryAttribute(constants.email(), true, email);
		view.addPrimaryAttribute(constants.googleId(), false, googleId);
		view.addPrimaryAttribute(constants.active(), true, active);
		
		registerCode = new TextBox();
		effectiveUntil = new DatePicker();
		licence = new ListBox();
		
		view.addSecondaryAttribute(constants.registerCode(), false, registerCode);
		view.addSecondaryAttribute(constants.effectiveUntil(), false, effectiveUntil);
		licence.addItem("");
		for(LicenceTypes type : LicenceTypes.values()){
			licence.addItem(type.name());
		}
		view.addSecondaryAttribute(constants.licence(), false, licence);
		licence.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				String l = licence.getItemText(licence.getSelectedIndex());
				if(l.equals("")){
					view.removeSecondaryAttribute(category);
					view.removeSecondaryAttribute(isAcategory);
				}else{
					switch (LicenceTypes.valueOf(l)) {
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
					default:
						break;
					}
				}
			}
		});		
		super.go(container);
		bind();
	}
	
	private void bind(){
		Display view = getView();
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
				
				Agent agent = null;
				if(!secondName.equals(""))
					agent = new Agent(firstName, surname.getText(), email.getText());
				else
					agent = new Agent(firstName, secondName, surname.getText(), email.getText());
				
				agent.setGoogleUserId(googleId.getText());
				agent.setIsActive(active.getValue());
				
				agent.setPictureURL(getPictureUrl());
				
				if(licence.getSelectedIndex() != 0){
					 
					String licenceType = licence.getItemText(licence.getSelectedIndex());
					Licence l = null;
					
					switch (LicenceTypes.valueOf(licenceType)) {
					case DrivingLicence:
						l = new DrivingLicence(registerCode.getText(), effectiveUntil.getValue());
						((DrivingLicence)l).setAgent(agent);
						break;
					default:
						break;
					}
					
					if(l != null)
						agent.addLicence(l);
				}
				
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
	
	protected CheckBox getActiveCheckBox(){
		return active;
	}
	
	@Override
	protected IAdminWorkspaceServiceAsync getRpcService(){
		return (IAdminWorkspaceServiceAsync) super.getRpcService();
	}
}
