package epusp.pcs.os.monitor.client.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

import epusp.pcs.os.monitor.client.rpc.IMonitorWorkspaceServiceAsync;
import epusp.pcs.os.shared.client.presenter.Presenter;

public class WorkspacePresenter implements Presenter{

	public interface Display {
		void showMap();
		void showCallInfo();
		void showAvailableReinforcements();
		RadioButton getMapButton();
		RadioButton getInfoButton();
		RadioButton getReinforcementsButton();
		Widget asWidget();
	}

	private final IMonitorWorkspaceServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final Display display;

	public WorkspacePresenter(IMonitorWorkspaceServiceAsync rpcService, HandlerManager eventBus, Display view) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.display = view;
	}

	@Override
	public void go(HasWidgets container) {
	    bind();
	    container.clear();
	    container.add(display.asWidget());
	}

	public void bind() {
		display.getMapButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				display.showMap();
			}
		});
		
		display.getInfoButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				display.showCallInfo();
			}
		});
		
		display.getReinforcementsButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				display.showAvailableReinforcements();
			}
		});
	}

}
