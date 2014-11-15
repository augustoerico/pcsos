package epusp.pcs.os.shared.general;

import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public interface Display {
	Widget asWidget();
	void setUserImage(String url);
	void setUserImage(ImageResource resource);
	void setUsername(String username);
	Image getLogout();
	Image getPreferences();
	HasWidgets addType(String type, Widget header);
	Boolean hasType(String type);
	void addSelectionHandler(SelectionHandler<Integer> handler);
	void setBackgroundStyleName(String style);
}
