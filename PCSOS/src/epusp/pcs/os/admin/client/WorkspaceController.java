package epusp.pcs.os.admin.client;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;

import epusp.pcs.os.shared.client.presenter.Presenter;

public class WorkspaceController implements Presenter{

	public WorkspaceController(){
		
	}
	
	@Override
	public void go(HasWidgets container) {
		container.add(new Label("Hello World"));
	}

}
