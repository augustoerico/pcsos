package epusp.pcs.os.admin.client.presenter;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import epusp.pcs.os.admin.client.rpc.IAdminWorkspaceServiceAsync;
import epusp.pcs.os.shared.client.constants.CommonWorkspaceConstants;
import epusp.pcs.os.shared.client.event.ClosePopupEvent;
import epusp.pcs.os.shared.client.event.EventBus;
import epusp.pcs.os.shared.client.presenter.CreatePersonPresenter;
import epusp.pcs.os.shared.model.attribute.AttributeInfo;
import epusp.pcs.os.shared.model.person.victim.Victim;

public class CreateVictimPresenter extends CreatePersonPresenter{
	
	public CreateVictimPresenter(IAdminWorkspaceServiceAsync rpcService,
			Display view, CommonWorkspaceConstants constants, List<AttributeInfo> customAttributes) {
		super(rpcService, view, constants, customAttributes);
	}
	
	@Override
	public void go(HasWidgets container){
		super.go(container);
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
				
				Victim victim = null;
				if(!secondName.equals(""))
					victim = new Victim(firstName, getSurnameTextBox().getText(), getEmailTextBox().getText());
				else
					victim = new Victim(firstName, secondName, getSurnameTextBox().getText(), getEmailTextBox().getText());
				
				victim.setGoogleUserId(getGoogleIdTextBox().getText());
				victim.setIsActive(getActiveCheckBox().getValue());
				
				victim.setPictureURL(getPictureUrl());
				
				readValuesAndSaveOnObject(victim);
				
				getRpcService().createVictim(victim, new AsyncCallback<Void>() {
					
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
	protected IAdminWorkspaceServiceAsync getRpcService(){
		return (IAdminWorkspaceServiceAsync) super.getRpcService();
	}
}
