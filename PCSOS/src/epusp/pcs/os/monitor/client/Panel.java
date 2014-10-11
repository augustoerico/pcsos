package epusp.pcs.os.monitor.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class Panel implements EntryPoint {

	@Override
	public void onModuleLoad() {
		RootLayoutPanel.get().add(new Label("Welcome to Monitor Workspace!"));
	}

}
