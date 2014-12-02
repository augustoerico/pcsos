package epusp.pcs.os.shared.client.view;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import epusp.pcs.os.shared.client.presenter.CreateUpdatePresenter.Display;

public class CreateUpdate extends Composite implements Display {

	private static CreateUpdateUiBinder uiBinder = GWT
			.create(CreateUpdateUiBinder.class);

	interface CreateUpdateUiBinder extends UiBinder<Widget, CreateUpdate> {
	}

	@UiField
	Image addPicture, picture;

	@UiField
	Button save, cancel;

	@UiField
	VerticalPanel primaryAttributes, secondaryAttributes;

	private HashMap<Widget, Widget> map = new HashMap<Widget, Widget>();

	public CreateUpdate() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPictureUrl(String safeUri){
		if(safeUri != null)
			picture.setUrl(safeUri);
	}

	@Override
	public void hideAdd(){
		addPicture.setVisible(false);
	}

	@Override
	public void showAdd(){
		addPicture.setVisible(true);
	}

	@Override
	public void hidePicture(){
		picture.setVisible(false);
	}

	@Override
	public void showPicture(){
		picture.setVisible(true);
	}

	@Override
	public void addPictureAddHandler(ClickHandler handler){
		addPicture.addClickHandler(handler);
	}

	@Override
	public void addMouseOverHandler(MouseOverHandler handler){
		picture.addMouseOverHandler(handler);
		addPicture.addMouseOverHandler(handler);
	}

	@Override
	public void addMouseOutHandler(MouseOutHandler handler){
		picture.addMouseOutHandler(handler);
	}

	@Override
	public Boolean isPictureVisible(){
		return !picture.getUrl().equals("");
	}

	@Override
	public void setSaveText(String text){
		save.setText(text);
	}

	@Override
	public void setCancelText(String text){
		cancel.setText(text);
	}

	@Override
	public void addSaveClickHandler(ClickHandler handler){
		save.addClickHandler(handler);
	}

	@Override
	public void setSaveEnabled(Boolean enabled){
		save.setEnabled(enabled);
	}
	
	@Override
	public void addCancelClickHandler(ClickHandler handler){
		cancel.addClickHandler(handler);
	}

	@Override
	public void addPrimaryAttribute(String label, Boolean required, Widget w){
		if(required){
			label = label.concat("*");
		}
		Input input = new Input(label, w);
		primaryAttributes.add(input);
		map.put(w, input);
	}

	@Override
	public void addSecondaryAttribute(String label, Boolean required, Widget w){
		if(required){
			label = label.concat("*");
		}
		Input input = new Input(label, w);
		secondaryAttributes.add(input);
		map.put(w, input);
	}

	@Override
	public void removeSecondaryAttribute(Widget w){
		Widget widget = map.remove(w);
		if(widget != null)
			secondaryAttributes.remove(widget);
	}

	@Override
	public void clearSecondaryAttributes(){
		secondaryAttributes.clear();
	}
	
	@Override
	public Widget asWidget(){
		return this;
	}

}
