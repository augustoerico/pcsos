package epusp.pcs.os.shared.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import epusp.pcs.os.shared.client.presenter.ImageTabPresenter.Display;

public class ImageTabPanel extends Composite implements Display {

	private static ImageTabPanelUiBinder uiBinder = GWT
			.create(ImageTabPanelUiBinder.class);

	interface ImageTabPanelUiBinder extends UiBinder<Widget, ImageTabPanel> {
	}
	
	@UiField
	FlowPanel control;
	
	@UiField
	DeckLayoutPanel infoPanel;
	
	public ImageTabPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	

	@Override
	public void addControl(Widget w) {
		AbsolutePanel p = new AbsolutePanel();
		p.add(w);
		p.addStyleName("controlItem");
		control.add(p);
	}

	@Override
	public void addInfo(Widget w) {
		AbsolutePanel p = new AbsolutePanel();
		p.setSize("100%", "100%");
		p.addStyleName("detailsPanel");
		p.add(w);
		infoPanel.add(p);
	}

	@Override
	public void clear() {
		control.clear();
		infoPanel.clear();
	}
	
	@Override
	public FlowPanel getControlPanel() {
		return control;
	}
	
	@Override
	public DeckLayoutPanel getInfoPanel() {
		return infoPanel;
	}
	
	@Override
	public Widget asWidget(){
		return this;
	}
}
