package epusp.pcs.os.superuser.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;

import epusp.pcs.os.shared.client.constants.CommonWorkspaceConstants;
import epusp.pcs.os.shared.client.rpc.IConnectionServiceAsync;
import epusp.pcs.os.shared.model.person.user.Monitor;

public class UpdateMonitorPresenter extends CreateMonitorPresenter{

	private final Monitor monitor;
	
	public UpdateMonitorPresenter(IConnectionServiceAsync rpcService,
			Display view, CommonWorkspaceConstants constants, Monitor monitor) {
		super(rpcService, view, constants);
		this.monitor = monitor;
	}

	@Override
	public void go(HasWidgets container){
		super.go(container);

		getSurnameTextBox().setText(monitor.getSurname());
		getSurnameTextBox().setReadOnly(true);

		getGivenNameTextBox().setText(monitor.getName() + " " + monitor.getSecondName());
		getGivenNameTextBox().setReadOnly(true);

		getEmailTextBox().setText(monitor.getEmail());
		getEmailTextBox().setReadOnly(true);

		getGoogleIdTextBox().setText(monitor.getGoogleUserId());
		getGoogleIdTextBox().setReadOnly(true);


		getActiveCheckBox().setValue(monitor.isActive());

		setPictureUrl(monitor.getPictureURL());
		getView().setPictureUrl(monitor.getPictureURL());
		getView().showPicture();
	}
}
