package epusp.pcs.os.shared.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event;

import epusp.pcs.os.shared.client.event.ClosePopupEvent.ClosePopupHandler;

public class ClosePopupEvent  extends Event<ClosePopupHandler> {
	public static final Type<ClosePopupHandler> TYPE = new Type<ClosePopupHandler>();

	public interface ClosePopupHandler extends EventHandler {
		public void onClosingPopup(ClosePopupEvent closePopupEvent);
	}
	
	public ClosePopupEvent(){
		
	}
	
	@Override
	public Type<ClosePopupHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ClosePopupHandler handler) {
		handler.onClosingPopup(this);
	}
}