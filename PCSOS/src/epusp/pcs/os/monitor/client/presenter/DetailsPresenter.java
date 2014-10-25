package epusp.pcs.os.monitor.client.presenter;

import java.util.HashMap;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import epusp.pcs.os.monitor.client.constants.MonitorWorkspaceConstants;
import epusp.pcs.os.monitor.client.event.LoadedAttributeInfoEvent;
import epusp.pcs.os.monitor.client.event.LoadedAttributeInfoEvent.LoadedAttributeInfoHandler;
import epusp.pcs.os.monitor.client.rpc.IMonitorWorkspaceServiceAsync;
import epusp.pcs.os.shared.client.event.EventBus;
import epusp.pcs.os.shared.client.presenter.Presenter;
import epusp.pcs.os.shared.general.RPCRequestTracker;
import epusp.pcs.os.shared.model.SystemObject;
import epusp.pcs.os.shared.model.attribute.AttributeInfo;
import epusp.pcs.os.shared.model.attribute.Category;
import epusp.pcs.os.shared.model.attribute.IAttribute;
import epusp.pcs.os.shared.model.person.Victim;

public class DetailsPresenter implements Presenter, LoadedAttributeInfoHandler{
	
	public interface Display{
		void setHeader(String text);
		void setPicture(String url);
		void addStaticAttribute(String label, String text);
		void addDynamicAttribute(Category category, String label, String value);
		Widget asWidget();
	}
	
	private Display view;
	private SystemObject item;
	private MonitorWorkspaceConstants constants;
	private IMonitorWorkspaceServiceAsync rpcService;
	
	private HashMap<String, AttributeInfo> attributeInfo = new HashMap<String, AttributeInfo>();
	
	private RPCRequestTracker tracker = new RPCRequestTracker(new LoadedAttributeInfoEvent(), this);
	
	private String locale = Window.Location.getParameter("locale");
	
	public DetailsPresenter(SystemObject item, Display view, IMonitorWorkspaceServiceAsync rpcService, MonitorWorkspaceConstants constants){
		this.item = item;
		this.view = view;
		this.constants = constants;
		this.rpcService = rpcService;
		
		EventBus.get().addHandlerToSource(LoadedAttributeInfoEvent.TYPE, this, this);
	}

	@Override
	public void go(HasWidgets container) {		
		container.clear();
		container.add(view.asWidget());
		
		for(IAttribute attribute : item.getAllAttributes()){
			if(!attributeInfo.containsKey(attribute.getAttributeName())){
				AsyncCallback<AttributeInfo> propertyInfoCall= new AsyncCallback<AttributeInfo>() {

					@Override
					public void onSuccess(AttributeInfo result) {
						if(tracker.hasCall(this)){
							attributeInfo.put(result.getAttributeName(), result);
							tracker.remove(this);
						}
					}

					@Override
					public void onFailure(Throwable caught) {
					}
				};
				
				tracker.add(propertyInfoCall);

				rpcService.getAttributeInfo(attribute.getAttributeName(), propertyInfoCall);
			}
		}
		
		if(tracker.isEmpty())
			tracker.fire();
		
		if(item instanceof Victim){
			Victim victim = (Victim) item;
			view.addStaticAttribute(constants.givenName(), victim.getName());
			view.addStaticAttribute(constants.surname(), victim.getSurname());
			view.setPicture(victim.getPictureURL());
		}
		
		
		bind();
	}

	private void bind(){
		
	}

	@Override
	public void onAttributeInfoLoaded(LoadedAttributeInfoEvent loadedAttributeInfoEvent) {
		for(IAttribute attribute : item.getAllAttributes()){
			AttributeInfo info = attributeInfo.get(attribute.getAttributeName());
			view.addDynamicAttribute(info.getCategory(), info.getLabel(locale), attribute.getValue().toString());
		}
	}
	
}
