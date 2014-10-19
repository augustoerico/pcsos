package epusp.pcs.os.monitor.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event;

import epusp.pcs.os.monitor.client.event.LoadedVehiclesEvent.LoadedVehiclesHandler;

public class LoadedVehiclesEvent extends Event<LoadedVehiclesHandler> {
	public static final Type<LoadedVehiclesHandler> TYPE = new Type<LoadedVehiclesHandler>();

	
	public interface LoadedVehiclesHandler extends EventHandler {
		public void onVehiclesLoaded(LoadedVehiclesEvent loadedVehiclesEvent);
	}

	public LoadedVehiclesEvent(){
	
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
