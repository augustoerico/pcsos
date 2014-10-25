package epusp.pcs.os.monitor.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event;

import epusp.pcs.os.monitor.client.event.LoadedAttributeInfoEvent.LoadedAttributeInfoHandler;

public class LoadedAttributeInfoEvent extends Event<LoadedAttributeInfoHandler> {
	public static final Type<LoadedAttributeInfoHandler> TYPE = new Type<LoadedAttributeInfoHandler>();

	
	public interface LoadedAttributeInfoHandler extends EventHandler {
		public void onAttributeInfoLoaded(LoadedAttributeInfoEvent loadedAttributeInfoEvent);
	}

	public LoadedAttributeInfoEvent(){
	}
	
	@Override
	public Type<LoadedAttributeInfoHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LoadedAttributeInfoHandler handler) {
		handler.onAttributeInfoLoaded(this);
	}
}
