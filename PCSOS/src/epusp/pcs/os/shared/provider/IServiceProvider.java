package epusp.pcs.os.shared.provider;

import java.util.Collection;

import com.google.gwt.user.client.rpc.AsyncCallback;

import epusp.pcs.os.shared.general.MoveCursor;

public interface IServiceProvider<T> {
	public void serviceCall(MoveCursor move, AsyncCallback<Collection<T>> callback);
}
