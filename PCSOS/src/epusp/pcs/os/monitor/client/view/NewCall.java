package epusp.pcs.os.monitor.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import epusp.pcs.os.monitor.client.presenter.NewCallPresenter.Display;

public class NewCall extends Composite implements Display {

	private static NewCallUiBinder uiBinder = GWT.create(NewCallUiBinder.class);
	
	@UiField
	Label message;

	@UiField
	Button ok;
	
	interface NewCallUiBinder extends UiBinder<Widget, NewCall> {
	}

	public NewCall() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setMessage(String text) {
		message.setText(text);
	}

	@Override
	public void addCloseHanlder(ClickHandler handler) {
		ok.addClickHandler(handler);
	}
	
	@Override
	public void setCloseButtonText(String text){
		ok.setText(text);
	}

}
