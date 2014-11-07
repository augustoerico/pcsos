package epusp.pcs.os.shared.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class PictureTagItem extends Composite {

	private static PictureTagItemUiBinder uiBinder = GWT
			.create(PictureTagItemUiBinder.class);

	interface PictureTagItemUiBinder extends UiBinder<Widget, PictureTagItem> {
	}
	
	@UiField
	FlowPanel panel;
	
	@UiField
	Image image;
	
	@UiField
	Label label;

	public PictureTagItem() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void addPanelStyleName(String styleName){
		panel.addStyleName(styleName);
	}
	
	@Override
	public void addStyleDependentName(String styleSuffix){
		panel.addStyleDependentName(styleSuffix);
	}
	
	@Override
	public void removeStyleDependentName(String styleSuffix){
		panel.removeStyleDependentName(styleSuffix);
	}
	
	public void addImageStyleName(String styleName){
		image.addStyleName(styleName);
	}
	
	public void addLabelStyleName(String styleName){
		label.addStyleName(styleName);
	}
	
	public void setImage(String imageUrl){
		image.setUrl(imageUrl);
	}
	
	public void setText(String text){
		label.setText(text);
	}

	public void addClickHandler(ClickHandler handler){
		image.addClickHandler(handler);
		label.addClickHandler(handler);
	}
}
