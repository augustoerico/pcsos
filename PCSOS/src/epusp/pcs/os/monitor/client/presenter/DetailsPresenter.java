package epusp.pcs.os.monitor.client.presenter;

import java.util.Date;
import java.util.HashMap;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
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
import epusp.pcs.os.shared.model.attribute.IAttribute;
import epusp.pcs.os.shared.model.person.user.agent.Agent;
import epusp.pcs.os.shared.model.person.victim.Victim;
import epusp.pcs.os.shared.model.vehicle.Vehicle;

public class DetailsPresenter implements Presenter, LoadedAttributeInfoHandler{
	
	public interface Display{
		void setPicture(String url);
		void addDynamicAttribute(String category, String label, String value);
		Widget asWidget();
		void addHeaderAttribute(String label, String text);
	}
	
	private Display view;
	private SystemObject item;
	private MonitorWorkspaceConstants constants;
	private IMonitorWorkspaceServiceAsync rpcService;
	
	private static final HashMap<String, AttributeInfo> attributeInfo = new HashMap<String, AttributeInfo>();
	
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
			view.addHeaderAttribute(constants.givenName(), victim.getName());
			view.addHeaderAttribute(constants.surname(), victim.getSurname());
			if(victim.getPictureURL() != null)
				view.setPicture(victim.getPictureURL());
		}else if(item instanceof Vehicle){
			Vehicle vehicle = (Vehicle) item;
			view.addHeaderAttribute(constants.priority(), vehicle.getPriority().getText());
			if(vehicle.getImageURL() != null)
				view.setPicture(vehicle.getImageURL());
		}else if(item instanceof Agent){
			Agent agent = (Agent) item;
			view.addHeaderAttribute(constants.givenName(), agent.getName());
			view.addHeaderAttribute(constants.surname(), agent.getSurname());
			if(agent.getPictureURL() != null)
				view.setPicture(agent.getPictureURL());
		}
		
		container.clear();
		container.add(view.asWidget());
		
		bind();
	}

	private void bind(){
		
	}

	@Override
	public void onAttributeInfoLoaded(LoadedAttributeInfoEvent loadedAttributeInfoEvent) {
		for(IAttribute attribute : item.getAllAttributes()){
			AttributeInfo info = attributeInfo.get(attribute.getAttributeName());
			if(info.isVisable(DetailsPresenter.class.getSimpleName())){
				switch(info.getDataType()){
				case BOOLEAN:
					String text;
					if((Boolean) attribute.getValue()){
						text = constants.yes();
					}else{
						text = constants.no();
					}
					view.addDynamicAttribute(info.getCategory().getText(), info.getLabel(locale), text);
					break;
				case DATE:
					DateTimeFormat ft = 
				      DateTimeFormat.getFormat(PredefinedFormat.DATE_LONG);
					view.addDynamicAttribute(info.getCategory().getText(), info.getLabel(locale), ft.format((Date) attribute.getValue()));
					break;
				default:
					view.addDynamicAttribute(info.getCategory().getText(), info.getLabel(locale), attribute.toString());
					break;
				
				}
			}
		}
	}	
}
