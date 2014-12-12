package epusp.pcs.os.monitor.client.presenter;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import epusp.pcs.os.monitor.client.constants.MonitorWorkspaceConstants;
import epusp.pcs.os.shared.client.presenter.Presenter;

public class NewCallPresenter implements Presenter{
	
	public interface Display{
		void setMessage(String text);
		void addCloseHanlder(ClickHandler handler);
		Widget asWidget();
		void setCloseButtonText(String text);
	}
	
	private final Display view;
	private final MonitorWorkspaceConstants constants;
	
	public NewCallPresenter(Display view, MonitorWorkspaceConstants constants){
		this.view = view;
		this.constants = constants;
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		view.setMessage(constants.warningMessage());
		view.setCloseButtonText(constants.answerIt());
		container.add(view.asWidget());		
		bind();
	}

	private void bind(){
		
	}
	
	public void addCloseHandler(ClickHandler handler){
		view.addCloseHanlder(handler);
	}
}
