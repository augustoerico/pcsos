package epusp.pcs.os.monitor.client.view;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import epusp.pcs.os.monitor.client.presenter.DetailsPresenter.Display;
import epusp.pcs.os.shared.model.attribute.Category;

public class Details extends Composite implements Display{

	private static DetailsUiBinder uiBinder = GWT.create(DetailsUiBinder.class);
	
	@UiField
	Label infoHeader;
	
	@UiField
	Image picture;
	
	@UiField
	VerticalPanel panel, staticAttributes, dynamicAttributes;
	
	HashMap<Category, FlowPanel> categories = new HashMap<Category, FlowPanel>();
	
	public interface Style extends CssResource{
		String label();
		String text();
		String panel();
	}
	
	@UiField
	Style style;

	interface DetailsUiBinder extends UiBinder<Widget, Details> {
	}

	public Details() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	private FlowPanel addAttribute(String label, String text){
		FlowPanel panel = new FlowPanel();
		panel.addStyleName(style.panel());
		Label l = new Label();
		l.setText(label);
		l.addStyleName(style.label());
		panel.add(l);
		Label t = new Label();
		t.setText(text);
		t.addStyleName(style.text());
		panel.add(t);
		return panel;
	}
	
	@Override
	public void addDynamicAttribute(Category category, String label,
			String value) {
		dynamicAttributes.add(addAttribute(label, value));
	}
	
	@Override
	public void addStaticAttribute(String label, String text){
		staticAttributes.add(addAttribute(label, text));
	}

	@Override
	public void setHeader(String text) {
		infoHeader.setText(text);
	}

	@Override
	public void setPicture(String url) {
		picture.setUrl(url);
	}
	
	@Override
	public Widget asWidget(){
		return this;
	}
}
