package epusp.pcs.os.monitor.client.view;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import epusp.pcs.os.monitor.client.presenter.DetailsPresenter.Display;
import epusp.pcs.os.shared.client.panel.HorizontalDivPanel;
import epusp.pcs.os.shared.client.panel.VerticalDivPanel;
import epusp.pcs.os.shared.client.widget.DisplayGroupPanel;
import epusp.pcs.os.shared.model.attribute.Category;

public class Details extends Composite implements Display{

	private static DetailsUiBinder uiBinder = GWT.create(DetailsUiBinder.class);
	
	@UiField
	Image picture;
	
	@UiField
	VerticalDivPanel header;
	
	@UiField
	HorizontalDivPanel categoriesPanel;
	
	HashMap<Category, DisplayGroupPanel> categories = new HashMap<Category, DisplayGroupPanel>();
	
	public interface Style extends CssResource{
		String label();
		String text();
		String panel();
		String captionPanel();
		String categoriesPanel();
	}
	
	@UiField
	Style style;

	interface DetailsUiBinder extends UiBinder<Widget, Details> {
	}

	public Details() {
		initWidget(uiBinder.createAndBindUi(this));
		categoriesPanel.addStyleName(style.categoriesPanel());
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
		if(!categories.containsKey(category)){
			DisplayGroupPanel categoryPanel = new DisplayGroupPanel(200);
			CaptionPanel captionPanel = new CaptionPanel(category.toString());
			captionPanel.add(categoryPanel);
			categoriesPanel.add(captionPanel);
			categories.put(category, categoryPanel);
		}
		DisplayGroupPanel categoryPanel = categories.get(category);
		categoryPanel.add(addAttribute(label, value));
	}
	
	@Override
	public void addHeaderAttribute(String label, String text){
		header.add(addAttribute(label, text));
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
