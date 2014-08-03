package epusp.pcs.os.shared.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IConnectionServiceAsync {
	
	void Hello(AsyncCallback<String> callback);

}
