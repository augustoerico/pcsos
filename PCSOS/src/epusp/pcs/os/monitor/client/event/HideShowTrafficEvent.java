package epusp.pcs.os.monitor.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event;

import  epusp.pcs.os.monitor.client.event.HideShowTrafficEvent.HideShowTrafficHandler;

public class HideShowTrafficEvent extends Event<HideShowTrafficHandler> {
	public static final Type<HideShowTrafficHandler> TYPE = new Type<HideShowTrafficHandler>();

	
	public interface HideShowTrafficHandler extends EventHandler {
		public void onHideShowRequest(HideShowTrafficEvent hideShowTrafficEvent);
	}

	private Boolean hide;

	public HideShowTrafficEvent(Boolean hide){
		this.hide = hide;
	}
	
	public Boolean hide(){
		return hide;
	}
	
	public Boolean show(){
		return !hide;
	}
	
	@Override
	public Type<HideShowTrafficHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(HideShowTrafficHandler handler) {
		handler.onHideShowRequest(this);
	}
}
