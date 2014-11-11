package epusp.pcs.os.shared.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class Input extends Composite {

	private static InputUiBinder uiBinder = GWT.create(InputUiBinder.class);

	interface InputUiBinder extends UiBinder<Widget, Input> {
	}
	
	@UiField
	Label label;
	
	@UiField(provided=true)
	Widget widget;

	public Input(String label, Widget w) {
		widget = w;
		initWidget(uiBinder.createAndBindUi(this));
		this.label.setText(label);
	}

	public void setLabelText(String text){
		label.setText(text);
	}
}
