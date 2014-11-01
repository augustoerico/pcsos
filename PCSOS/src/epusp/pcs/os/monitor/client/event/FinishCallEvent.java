package epusp.pcs.os.monitor.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event;

import epusp.pcs.os.monitor.client.event.FinishCallEvent.FinishCallHandler;

public class FinishCallEvent extends Event<FinishCallHandler> {
	public static final Type<FinishCallHandler> TYPE = new Type<FinishCallHandler>();

	public interface FinishCallHandler extends EventHandler {
		public void onFinishCall(FinishCallEvent finishCallEvent);
	}

	public FinishCallEvent(){
	}
	
	@Override
	public Type<FinishCallHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(FinishCallHandler handler) {
		handler.onFinishCall(this);
	}
}