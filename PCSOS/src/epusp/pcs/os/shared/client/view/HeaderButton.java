package epusp.pcs.os.shared.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class HeaderButton extends Composite {

	private static HeaderButtonUiBinder uiBinder = GWT
			.create(HeaderButtonUiBinder.class);

	interface HeaderButtonUiBinder extends UiBinder<Widget, HeaderButton> {
	}

	@UiField
	Label label;
	
	@UiField
	Image image;
	
	private Boolean enabled = true;
	
	public HeaderButton(String text, SafeUri imageUri) {
		initWidget(uiBinder.createAndBindUi(this));
		label.setText(text);
		image.setUrl(imageUri);
	}

	public void addClickHandler(ClickHandler clickHandler){
		image.addClickHandler(clickHandler);
	}
	
	public Boolean isEnabled(){
		return enabled;
	}
	
	public void disable(){
		enabled = false;
		image.addStyleDependentName("disabled");
	}
	
	public void enable(){
		enabled = true;
		image.removeStyleDependentName("disabled");
	}
}
