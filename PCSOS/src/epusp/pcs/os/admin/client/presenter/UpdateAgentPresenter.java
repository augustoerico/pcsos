package epusp.pcs.os.admin.client.presenter;

import java.util.List;

import com.google.gwt.user.client.ui.HasWidgets;

import epusp.pcs.os.admin.client.rpc.IAdminWorkspaceServiceAsync;
import epusp.pcs.os.shared.client.constants.CommonWorkspaceConstants;
import epusp.pcs.os.shared.model.attribute.AttributeInfo;
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

		getGivenNameTextBox().setText(agent.getName() + " " + agent.getSecondName());
		getGivenNameTextBox().setReadOnly(true);

		getEmailTextBox().setText(agent.getEmail());
		getEmailTextBox().setReadOnly(true);

		getGoogleIdTextBox().setText(agent.getGoogleUserId());
		getGoogleIdTextBox().setReadOnly(true);


		getActiveCheckBox().setValue(agent.isActive());

		setPictureUrl(agent.getPictureURL());
		getView().setPictureUrl(agent.getPictureURL());
		getView().showPicture();

		if(!agent.getLicences().isEmpty()){
			getRegisterCodeTextBox().setText(agent.getLicences().get(0).getRegisterCode());
			getRegisterCodeTextBox().setReadOnly(true);
			
			getEffectiveUntilDatePicker().setValue(agent.getLicences().get(0).validUntil());
			
			getLicenceListBox().setSelectedIndex(1);
		}
		
		addValuesToCustomWidgets(agent);
	}

}
