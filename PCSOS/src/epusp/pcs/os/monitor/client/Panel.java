package epusp.pcs.os.monitor.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;

import epusp.pcs.os.monitor.client.constants.MonitorWorkspaceContants;

public class Panel implements EntryPoint {
	
	 private MonitorWorkspaceContants constants = GWT.create(MonitorWorkspaceContants.class);

	@Override
	public void onModuleLoad() {
		RootLayoutPanel.get().add(new Label(constants.welcome()));
	}

}
