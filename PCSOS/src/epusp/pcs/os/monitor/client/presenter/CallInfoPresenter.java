package epusp.pcs.os.monitor.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import epusp.pcs.os.monitor.client.constants.MonitorWorkspaceConstants;
import epusp.pcs.os.monitor.client.rpc.IMonitorWorkspaceServiceAsync;
import epusp.pcs.os.monitor.client.view.Details;
import epusp.pcs.os.shared.client.presenter.Presenter;
import epusp.pcs.os.shared.model.person.Victim;

public class CallInfoPresenter implements Presenter{

	public interface Display{
		HasWidgets getVictimPanel();
		Widget asWidget();
	}

	private IMonitorWorkspaceServiceAsync rpcService; 
	private Display view; 
	private MonitorWorkspaceConstants constants;
	private Boolean hasVictim = false;
	private DetailsPresenter victim;
	
	
	public CallInfoPresenter(IMonitorWorkspaceServiceAsync rpcService, Display view, MonitorWorkspaceConstants constants) {
		this.constants = constants;
		this.rpcService = rpcService;
		this.view = view;
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
		bind();
	}

	private void bind(){

	}

	public void showVictim(Victim victim){
		hasVictim = true;
		this.victim = new DetailsPresenter(victim, new Details(), rpcService, constants);
		this.victim.go(view.getVictimPanel());
	}

	public Boolean hasVictim(){
		return hasVictim;
	}
}
