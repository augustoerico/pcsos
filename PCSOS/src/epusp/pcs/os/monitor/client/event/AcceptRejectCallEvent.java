package epusp.pcs.os.monitor.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event;

import epusp.pcs.os.monitor.client.event.AcceptRejectCallEvent.AcceptRejectCallHandler;

public class AcceptRejectCallEvent extends Event<AcceptRejectCallHandler> {
	public static final Type<AcceptRejectCallHandler> TYPE = new Type<AcceptRejectCallHandler>();

	public interface AcceptRejectCallHandler extends EventHandler {
		public void onAcceptingRejectingCalls(AcceptRejectCallEvent acceptRejectCallEvent);
	}

	private Boolean isAccepting;
	
	public AcceptRejectCallEvent(Boolean isAccepting){
		this.isAccepting = isAccepting;
	}
	
	public Boolean isAccepting(){
		return isAccepting;
	}
	
	@Override
	public Type<AcceptRejectCallHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AcceptRejectCallHandler handler) {
		handler.onAcceptingRejectingCalls(this);
	}
}
