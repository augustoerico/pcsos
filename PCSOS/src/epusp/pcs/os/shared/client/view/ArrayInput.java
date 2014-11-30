package epusp.pcs.os.shared.client.view;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class ArrayInput<T> extends Composite {

	private static ArrayInputUiBinder uiBinder = GWT
			.create(ArrayInputUiBinder.class);

	interface ArrayInputUiBinder extends UiBinder<Widget, ArrayInput<?>> {
	}
	
	public interface WidgetFactory<T>{
		Widget newInstace();
		Widget newInstace(T value);
		T getValueFrom(Widget w);
	}
	
	@UiField
	FlowPanel values;
	
	@UiField
	Image add, remove;
	
	private Boolean isEnabled = true;

	private List<Widget> widgets = new ArrayList<Widget>();
	
	public interface Styles extends CssResource{
		public String values();
	}
	
	@UiField
	Styles style;
	
	private final WidgetFactory<T> factory;

	public ArrayInput(WidgetFactory<T> factory) {
		this.factory = factory;
		initWidget(uiBinder.createAndBindUi(this));
		addInput();
	}

	public void addInput(){
		Widget w = factory.newInstace();
		appendToInterface(w);
	}
	
	public void addInput(T value){
		Widget w = factory.newInstace(value);
		appendToInterface(w);
	}
	
	private void appendToInterface(Widget w){
		if(w instanceof HasEnabled)
			((HasEnabled) w).setEnabled(isEnabled);
		w.addStyleName(style.values());
		values.add(w);
		widgets.add(w);
	}
	
	@UiHandler("add")
	void onAddClick(ClickEvent clickEvent){
		addInput();
	}
	
	@UiHandler("remove")
	void onRemoveClick(ClickEvent clickEvent){
		int i = widgets.size()-1;
		if(i >= 0){
			values.remove(i);
			widgets.remove(i);
		}
	}
	
	public void setEnabled(Boolean isEnabled){
		this.isEnabled = isEnabled;
		for(Widget w : widgets)
			if(w instanceof HasEnabled)
				((HasEnabled) w).setEnabled(isEnabled);
		add.setVisible(isEnabled);
		remove.setVisible(isEnabled);
	}
	
	public void setValues(T[] values){
		for(T value : values)
			addInput(value);
	}

	public List<T> getValues(){
		List<T> l = new ArrayList<T>();
		for(Widget w : widgets){
			T value = factory.getValueFrom(w);
			if(value != null)
				l.add(value);
		}
		return l;
	}
	
	public void clear(){
		values.clear();
	}
}
