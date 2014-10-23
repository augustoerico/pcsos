package epusp.pcs.os.monitor.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import epusp.pcs.os.monitor.client.presenter.CallInfoPresenter.Display;

public class CallInfo extends Composite implements Display {

	private static CallInfoUiBinder uiBinder = GWT
			.create(CallInfoUiBinder.class);

	interface CallInfoUiBinder extends UiBinder<Widget, CallInfo> {
	}
	
	@UiField
	AbsolutePanel victimPanel;
	
	public CallInfo(){
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	public HasWidgets getVictimPanel(){
		return victimPanel;
	}
	
	@Override
	public Widget asWidget(){
		return this;
	}
}
