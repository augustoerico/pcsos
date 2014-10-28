package epusp.pcs.os.monitor.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event;

import epusp.pcs.os.monitor.client.event.LoadedInfoEvent.LoadedInfoHandler;
import epusp.pcs.os.shared.model.oncall.EmergencyCall;

public class LoadedInfoEvent extends Event<LoadedInfoHandler> {
	public static final Type<LoadedInfoHandler> TYPE = new Type<LoadedInfoHandler>();

	
	public interface LoadedInfoHandler extends EventHandler {
		public void onInfoLoaded(LoadedInfoEvent loadedInfoEvent);
	}

	private EmergencyCall call;

	public LoadedInfoEvent(EmergencyCall emergencyCall){
		this.call = emergencyCall;
	}
	
	public EmergencyCall getEmergencyCall(){
		return call;
	}

	@Override
	public Type<LoadedInfoHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LoadedInfoHandler handler) {
		handler.onInfoLoaded(this);
	}
}
