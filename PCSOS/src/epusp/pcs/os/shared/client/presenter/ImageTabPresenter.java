package epusp.pcs.os.shared.client.presenter;

import java.util.HashMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import epusp.pcs.os.shared.client.view.PictureTagItem;

public class ImageTabPresenter implements Presenter{

	private HashMap<String, Widget> info = new HashMap<String, Widget>();
	
	private HashMap<Integer, Widget> controlPanel = new HashMap<Integer, Widget>();
	
	public interface Display{
		void addControl(Widget w);
		void addInfo(Widget w);
		void clear();
		FlowPanel getControlPanel();
		DeckLayoutPanel getInfoPanel();
		Widget asWidget();
	}
	
	private Display view;
	
	public ImageTabPresenter(Display view){
		this.view = view;
	}
	
	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
		bind();
	}

	private void bind(){

	}
	
	public Boolean hasInfo(String infoTag){
		return info.containsKey(infoTag);
	}

	public void addInfo(String infoTag, String infoImageUrl, Widget info){
		this.info.put(infoTag, info);
		
		PictureTagItem controlItem = new PictureTagItem();
		controlItem.setText(infoTag);
		controlItem.setImage(infoImageUrl);
		controlItem.addPanelStyleName("controlPanel");
		controlItem.addImageStyleName("controlPicture");
		controlItem.addLabelStyleName("controlIdText");
		
		view.addControl(controlItem);
		
		view.addInfo(info);
		
		final int i = view.getControlPanel().getWidgetCount()-1;
		
		controlPanel.put(i, controlItem);
		
		controlItem.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				view.getInfoPanel().showWidget(i);
				Widget pti = controlPanel.get(i);
				for(Widget p : controlPanel.values()){
					p.removeStyleDependentName("view");
				}
				pti.addStyleDependentName("view");
			}
		});
	}
	
}
