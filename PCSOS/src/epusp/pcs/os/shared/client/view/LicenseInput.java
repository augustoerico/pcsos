package epusp.pcs.os.shared.client.view;

import java.util.Date;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class LicenseInput extends Composite {

	private static LicenseInputUiBinder uiBinder = GWT
			.create(LicenseInputUiBinder.class);

	interface LicenseInputUiBinder extends UiBinder<Widget, LicenseInput> {
	}
	
	@UiField
	Label categoriesLabel, effectiveUntilLabel, registerCodeLabel;
	
	@UiField
	ListBox categories;
	
	@UiField
	DateBox effectiveUntil;
	
	@UiField
	CaptionPanel header;
	
	@UiField
	FlowPanel panel;
	
	@UiField
	TextBox registerCode;
	
	private HashMap<String, Widget> widgets = new HashMap<String, Widget>();

	public LicenseInput() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setHeader(String text){
		header.setCaptionText(text);
	}
	
	public void setEffectiveUntil(Date date){
		effectiveUntil.setValue(date);
	}
	
	public Date getEffectiveUntil(){
		return effectiveUntil.getValue();
	}
	
	public void addCategoryItem(String item, String value){
		categories.addItem(item, value);
	}
	
	public void addCategoryItem(String item){
		categories.addItem(item);
	}
	
	public void setCategoryLabel(String text){
		categoriesLabel.setText(text);
	}
	
	public void addWidget(String key, String label, Widget w){
		FlowPanel panel = new FlowPanel();
		panel.add(new Label(label));
		panel.add(w);
		this.panel.add(panel);
		widgets.put(key, w);
	}
	
	public Widget getWidget(String key){
		return widgets.get(key);
	}
	
	public void setEffectiveUntilLabel(String text){
		effectiveUntilLabel.setText(text);
	}
	
	public void setRegisterCode(String text){
		registerCode.setText(text);
	}
	
	public String getRegisterCode(){
		return registerCode.getText();
	}
	
	public void setRegisterCodeLabel(String text){
		registerCodeLabel.setText(text);
	}
	
	public String getSelectedCategoty(){
		int i = categories.getSelectedIndex();
		
		if(i > 0){
			return categories.getValue(i);
		}
		
		return "";
	}
	
	public void setSelectedCategory(String value){
		for(int i = 0; i < categories.getItemCount(); i++){
			if(categories.getValue(i).equals(value)){
				categories.setSelectedIndex(i);
				break;
			}
		}
	}
}
