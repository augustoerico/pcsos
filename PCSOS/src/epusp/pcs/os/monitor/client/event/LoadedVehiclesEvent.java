package epusp.pcs.os.monitor.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event;

import epusp.pcs.os.monitor.client.event.LoadedVehiclesEvent.LoadedVehiclesHandler;
import epusp.pcs.os.shared.model.oncall.EmergencyCall;

public class LoadedVehiclesEvent extends Event<LoadedVehiclesHandler> {
	public static final Type<LoadedVehiclesHandler> TYPE = new Type<LoadedVehiclesHandler>();

	
	public interface LoadedVehiclesHandler extends EventHandler {
		public void onVehiclesLoaded(LoadedVehiclesEvent loadedVehiclesEvent);
	}

	private EmergencyCall call;

	public LoadedVehiclesEvent(EmergencyCall emergencyCall){
		this.call = emergencyCall;
	}
	
	public EmergencyCall getEmergencyCall(){
		return call;
	}

	@Override
	public Type<LoadedVehiclesHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LoadedVehiclesHandler handler) {
		handler.onVehiclesLoaded(this);
	}
}
