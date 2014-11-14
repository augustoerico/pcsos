package epusp.pcs.os.admin.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;

import epusp.pcs.os.admin.client.rpc.IAdminWorkspaceServiceAsync;
import epusp.pcs.os.shared.client.constants.CommonWorkspaceConstants;
import epusp.pcs.os.shared.model.person.user.Agent;

public class UpdateAgentPresenter extends CreateAgentPresenter{

	private Agent agent;

	public UpdateAgentPresenter(IAdminWorkspaceServiceAsync rpcService,
			Display view, CommonWorkspaceConstants constants, Agent agent) {
		super(rpcService, view, constants);
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

		if(!agent.getLicences().isEmpty()){
			getRegisterCodeTextBox().setText(agent.getLicences().get(0).getRegisterCode());
			getRegisterCodeTextBox().setReadOnly(true);
			
			getEffectiveUntilDatePicker().setValue(agent.getLicences().get(0).validUntil());
			
			getLicenceListBox().setSelectedIndex(1);
		}

	}

}