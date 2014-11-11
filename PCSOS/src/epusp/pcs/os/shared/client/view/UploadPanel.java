package epusp.pcs.os.shared.client.view;

import gwtupload.client.IUploader.OnFinishUploaderHandler;
import gwtupload.client.SingleUploader;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class UploadPanel extends Composite {

	private static UploadPanelUiBinder uiBinder = GWT
			.create(UploadPanelUiBinder.class);

	interface UploadPanelUiBinder extends UiBinder<Widget, UploadPanel> {
	}

	@UiField
	SingleUploader singleUploader;
	
	@UiField
	Button cancel;
	
	public UploadPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void addOnFinishUploaderHandler(OnFinishUploaderHandler handler){
		singleUploader.addOnFinishUploadHandler(handler);
	}
	
	public void addClickHandler(ClickHandler handler){
		cancel.addClickHandler(handler);
	}
	
	public void setButtonText(String text){
		cancel.setText(text);
	}
	
}
