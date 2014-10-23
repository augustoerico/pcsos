package epusp.pcs.os.monitor.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import epusp.pcs.os.monitor.client.constants.MonitorWorkspaceConstants;
import epusp.pcs.os.shared.client.presenter.Presenter;
import epusp.pcs.os.shared.model.SystemObject;
import epusp.pcs.os.shared.model.attribute.Category;
import epusp.pcs.os.shared.model.person.Victim;

public class DetailsPresenter implements Presenter {
	
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
	
	public DetailsPresenter(SystemObject item, Display view, MonitorWorkspaceConstants constants){
		this.item = item;
		this.view = view;
		this.constants = constants;
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
		
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
	
}
