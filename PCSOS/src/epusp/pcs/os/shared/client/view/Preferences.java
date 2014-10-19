package epusp.pcs.os.shared.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import epusp.pcs.os.shared.client.presenter.PreferencesPresenter.Display;

public class Preferences extends Composite implements Display{

	private static PreferencesUiBinder uiBinder = GWT
			.create(PreferencesUiBinder.class);

	interface PreferencesUiBinder extends UiBinder<Widget, Preferences> {
	}
	
	@UiField
	Button cancel, ok;
	
	@UiField
	Label languageLabel;
	
	@UiField
	ListBox languages;

	public Preferences() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public Button getCancelButton() {
		return cancel;
	}

	@Override
	public Button getOkButton() {
		return ok;
	}

	@Override
	public void setLanguageLabel(String text) {
		languageLabel.setText(text);
	}

	@Override
	public ListBox getLanguages() {
		return languages;
	}
	
	@Override
	public Widget asWidget(){
		return this;
	}
}
