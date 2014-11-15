package epusp.pcs.os.superuser.client.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import epusp.pcs.os.shared.client.constants.CommonWorkspaceConstants;
import epusp.pcs.os.shared.client.event.ClosePopupEvent;
import epusp.pcs.os.shared.client.event.EventBus;
import epusp.pcs.os.shared.client.presenter.CreatePersonPresenter;
import epusp.pcs.os.shared.client.rpc.IConnectionServiceAsync;
import epusp.pcs.os.shared.model.person.user.Admin;
import epusp.pcs.os.superuser.client.rpc.ISuperUserWorkspaceServiceAsync;

public class CreateAdminPresenter extends CreatePersonPresenter{

	public CreateAdminPresenter(IConnectionServiceAsync rpcService,
			Display view, CommonWorkspaceConstants constants) {
		super(rpcService, view, constants);
	}
	
	@Override
	public void go(HasWidgets container){
		super.go(container);
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
				
				Admin admin = null;
				if(!secondName.equals(""))
					admin = new Admin(firstName, getSurnameTextBox().getText(), getEmailTextBox().getText());
				else
					admin = new Admin(firstName, secondName, getSurnameTextBox().getText(), getEmailTextBox().getText());
				
				admin.setGoogleUserId(getGoogleIdTextBox().getText());
				admin.setIsActive(getActiveCheckBox().getValue());
				
				admin.setPictureURL(getPictureUrl());
				
				getRpcService().createAdmin(admin, new AsyncCallback<Void>() {
					
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
