package epusp.pcs.os.superuser.client.presenter;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import epusp.pcs.os.shared.client.constants.CommonWorkspaceConstants;
import epusp.pcs.os.shared.client.event.ClosePopupEvent;
import epusp.pcs.os.shared.client.event.EventBus;
import epusp.pcs.os.shared.client.presenter.CreatePersonPresenter;
import epusp.pcs.os.shared.client.rpc.IConnectionServiceAsync;
import epusp.pcs.os.shared.model.attribute.AttributeInfo;
import epusp.pcs.os.shared.model.person.user.superuser.SuperUser;
import epusp.pcs.os.superuser.client.rpc.ISuperUserWorkspaceServiceAsync;

public class CreateSuperUserPresenter extends CreatePersonPresenter{

	public CreateSuperUserPresenter(IConnectionServiceAsync rpcService,
			Display view, CommonWorkspaceConstants constants,  List<AttributeInfo> customAttributes) {
		super(rpcService, view, constants, customAttributes);
	}
	
	@Override
	public void go(HasWidgets container){
		super.go(container);
		addCustomAttributesToView();
		bind();
	}
	
	public void bind(){
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
				
				SuperUser superUser = null;
				if(!secondName.equals(""))
					superUser = new SuperUser(firstName, getSurnameTextBox().getText(), getEmailTextBox().getText());
				else
					superUser = new SuperUser(firstName, secondName, getSurnameTextBox().getText(), getEmailTextBox().getText());
				
				superUser.setGoogleUserId(getGoogleIdTextBox().getText());
				superUser.setIsActive(getActiveCheckBox().getValue());
				
				superUser.setPictureURL(getPictureUrl());
				
				readValuesAndSaveOnObject(superUser);
				
				getRpcService().createSuperUser(superUser, new AsyncCallback<Void>() {
					
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
	
	@Override
	protected ISuperUserWorkspaceServiceAsync getRpcService(){
		return (ISuperUserWorkspaceServiceAsync) super.getRpcService();
	}

}
