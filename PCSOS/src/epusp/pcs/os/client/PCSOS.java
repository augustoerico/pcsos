package epusp.pcs.os.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

import epusp.pcs.os.client.rpc.IConnectionService;
import epusp.pcs.os.client.rpc.IConnectionServiceAsync;
import epusp.pcs.os.model.User;

public class PCSOS implements EntryPoint {
	
	IConnectionServiceAsync connection = (IConnectionServiceAsync) GWT.create(IConnectionService.class);

	public void onModuleLoad() {
		final RootPanel root = RootPanel.get();
		
		connection.Hello(new AsyncCallback<User>() {
			
			public void onSuccess(User result) {
				root.add(new Label("Hello, " + result.getName()));
				
			}
			
			public void onFailure(Throwable caught) {
				root.add(new Label("I couldn't connect... ;("));
			}
		});
		

	}

}
