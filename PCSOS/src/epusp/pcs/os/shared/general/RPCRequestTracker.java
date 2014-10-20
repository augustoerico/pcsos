package epusp.pcs.os.shared.general;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.Event;

import epusp.pcs.os.shared.client.event.EventBus;

public class RPCRequestTracker {
	
	private final List<AsyncCallback<?>> tracker = new ArrayList<AsyncCallback<?>>();
	
	private final Event<?> event;
	private final Object source;

	public RPCRequestTracker(Event<?> event, Object source){
		this.event = event;
		this.source = source;
	}
	
	public RPCRequestTracker(Event<?> event){
		this.event = event;
		source = null;
	}
	
	public RPCRequestTracker(){
		event = null;
		source = null;
	}	
	
	public void fire(){
		if(event != null){
			if(source == null)
				EventBus.get().fireEvent(event);
			else
				EventBus.get().fireEventFromSource(event, source);
		}
	}
	
	public void add(AsyncCallback<?>... calls){
		for (AsyncCallback<?> call : calls) {
			tracker.add(call);
        }
	}
	
	public void remove(AsyncCallback<?> call){
		tracker.remove(call);
		
		if(tracker.isEmpty() && event != null){
			if(source == null)
				EventBus.get().fireEvent(event);
			else
				EventBus.get().fireEventFromSource(event, source);
		}
	}
	
	public Boolean hasCall(AsyncCallback<?> call){
		return tracker.contains(call);
	}
	
	public Boolean isEmpty(){
		return tracker.isEmpty();
	}
	
	public void clear(){
		tracker.clear();
	}
	
	public List<AsyncCallback<?>> getPendingRequests(){
		return tracker;
	}
	
	@Override
	public String toString() {
		return tracker.toString();
	}
}
