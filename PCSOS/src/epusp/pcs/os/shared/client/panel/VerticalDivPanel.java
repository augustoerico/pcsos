package epusp.pcs.os.shared.client.panel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * Creates a vertical panel
 * @author PCSOS
 */
@SuppressWarnings("deprecation")
public class VerticalDivPanel extends ComplexPanel implements HasClickHandlers {

	private final UnorderedList ulElem = new UnorderedList();

	/**     
	 * Creates an empty flow panel.
	 */    
	public VerticalDivPanel() {
		final Element divElement = DOM.createDiv();          
		setElement(divElement);                    
		divElement.setClassName("vertical");  

		super.add(ulElem, getElement());
	}


	/**
	 * Adds a new child widget to the panel.     
	 *      
	 * @param w the widget to be added    
	 */    
	public void add(Widget w) { 
		ListItem li = new ListItem();
		li.add(w);        
		ulElem.add(li);
	} 


	/**
	 * Removes all child widgets
	 */
	public void clear() {
		ulElem.clear();
	}



	/**
	 * Adds a secondary or dependent style name to this object.
	 * @param style the new style name
	 */
	public void setStyleNameName(String style) {
		super.addStyleName(style);
	}


	/** Gets the child widget at the specified index
	 * @param index Index of the widget to retrieve
	 * @return The child widget
	 * @throws IndexOutOfBoundsException
	 */
	public Widget getWidget(int index) {
		Widget widget = null;

		if (index < ulElem.getWidgetCount()) {
			ListItem li = (ListItem)ulElem.getWidget(index);
			if (li.getWidgetCount() > 0) {
				widget = li.getWidget(0);                
			}
		}
		else {
			throw new IndexOutOfBoundsException("The index value" + index + " cannot be greater than the number of widgets of " + ulElem.getWidgetCount());            
		}

		return widget;
	}


	/**
	 * Gets the number of child widgets in the panel
	 * @return The number of children
	 */
	public int getWidgetCount() {
		return ulElem.getWidgetCount();
	}

    @Override
    public int getOffsetHeight(){
    	return ulElem.getOffsetHeight();
    }
    
    @Override
    public int getOffsetWidth(){
    	return ulElem.getOffsetWidth();
    }


	/**
	 * Gets the index of the specified Widget
	 * @param child The widget to be found
	 * @return the widget's index, or -1 if it is not a child of this panel
	 */
	public int getWidgetIndex(IsWidget child) {
		int index = -1;

		int i = 0;
		boolean found = false;
		while (i < ulElem.getWidgetCount() && !found) {
			ListItem li = (ListItem)ulElem.getWidget(i);
			if (li.getWidgetCount() > 0) {
				if (child.equals(li.getWidget(0))) {
					found = true;
					index = i;
				}
			}
			i++;
		}

		return index;
	}


	/**
	 * Gets the index of the specified Widget
	 * @param child The widget to be found
	 * @return the widget's index, or -1 if it is not a child of this panel
	 */
	public int getWidgetIndex(Widget child) {
		int index = -1;

		int i = 0;
		boolean found = false;
		while (i < ulElem.getWidgetCount() && !found) {
			ListItem li = (ListItem)ulElem.getWidget(i);
			if (li.getWidgetCount() > 0) {
				if (child.equals(li.getWidget(0))) {
					found = true;
					index = i;
				}
			}
			i++;
		}

		return index;
	}



	/**     
	 * Inserts a widget before the specified index.     
	 *      
	 * @param w the widget to be inserted     
	 * @param beforeIndex the index before which it will be inserted     
	 * @throws IndexOutOfBoundsException if <code>beforeIndex</code> is out of range    
	 */    
	public void insert(Widget w, int beforeIndex) { 
		ListItem li = new ListItem();
		li.add(w);
		ulElem.insert(li, beforeIndex);
	}



	/**
	 * 
	 */
	public boolean remove(int index) {
		return ulElem.remove(index);        
	}



	/**
	 * 
	 */
	public boolean remove(Widget w) {
		return ulElem.remove(w);
	}


	/**
	 * 
	 */
	public boolean remove(IsWidget child) {
		return ulElem.remove(child);
	}



	/**
	 * 
	 * @param event
	 * @return
	 */
	public ListItem getItemForEvent(ClickEvent event) {
		Element li = getEventTargetItem(Event.as(event.getNativeEvent()));

		if (li == null) {
			return null;
		}

		return new ListItem(li);
	}



	/**
	 * 
	 * @param event
	 * @return
	 */
	protected Element getEventTargetItem(Event event) {
		Element li = DOM.eventGetTarget(event);

		for (; li != null; li = DOM.getParent(li)) {
			// If it's a LI, it might be the one we're looking for.
			if (DOM.getElementProperty(li, "tagName").equalsIgnoreCase("li")) {
				// Make sure it's directly a part of this table before returning it.
				Element ul = DOM.getParent(li);
				if (ul == ulElem.getElement()) {
					return li;
				}
			}
			// If we run into this table's body, we're out of options.
			if (li == ulElem.getElement()) {
				return null;
			}
		}

		return null;
	}



	/**
	 * 
	 */
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}



	/**
	 * 
	 * @author PCSOS
	 */
	public class UnorderedList extends ComplexPanel {
		public UnorderedList() {
			setElement(DOM.createElement("ul"));
		}

		public UnorderedList(Element ul) {
			setElement(ul);
		}

		public void add(Widget w) {
			super.add(w, getElement());
		}

		public void insert(Widget w, int beforeIndex) {
			super.insert(w, getElement(), beforeIndex, true);
		}
	}


	/**
	 * 
	 * @author PCSOS
	 */
	public class ListItem extends ComplexPanel implements HasText {
		int index = -1;

		public ListItem() {
			setElement(DOM.createElement("li"));
		}

		public ListItem(Element li) {
			setElement(li);
			index = DOM.getChildIndex(DOM.getParent(li), li);
		}

		public void add(Widget w) {
			super.add(w, getElement());
		}

		public void insert(Widget w, int beforeIndex) {
			super.insert(w, getElement(), beforeIndex, true);
		}

		public String getText() {
			return DOM.getInnerText(getElement());
		}

		public void setText(String text) {
			DOM.setInnerText(getElement(), (text == null) ? "" : text);
		}

		public int getIndex() {
			if (this.getParent() == null) {
				return index;
			}
			return DOM.getChildIndex(this.getParent().getElement(), this.getElement());
		}
	}
}
