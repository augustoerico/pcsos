package epusp.pcs.os.shared.client.panel;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

@SuppressWarnings("deprecation")
public class HorizontalDivPanel extends ComplexPanel {
    
    UnorderedList ul = new UnorderedList();

     /**     
      * Creates an empty flow panel.     
      */    
    public HorizontalDivPanel() {
        final Element divElement = DOM.createDiv();          
        setElement(divElement);                    
        divElement.setClassName("horizontal");  
        
        super.add(ul, getElement());
    }
    
    
    /**
     * Adds a new child widget to the panel.     
     *      
     * @param w the widget to be added    
     */    
    public void add(Widget w) {         
        ListItem li = new ListItem();
        li.add(w);
        ul.add(li);   
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
        ul.add(li);
        ul.insert(li, beforeIndex);
    }


    /**
     * Gets the child widget at the specified index
     * @param index Index of the widget to retrieve
     * @return The child widget
     * @throws IndexOutOfBoundsException
     */
    public Widget getWidget(int index) {
        Widget widget = null;
        
        if (index < ul.getWidgetCount()) {
            ListItem li = (ListItem)ul.getWidget(index);
            if (li.getWidgetCount() > 0) {
                widget = li.getWidget(0);                
            }
        }
        else {
            throw new IndexOutOfBoundsException("The index value" + index + " cannot be greater than the number of widgets of " + ul.getWidgetCount());            
        }
        
        return widget;
    }
    
        
    /**
     * Gets the number of child widgets in the panel
     * @return The number of children
     */
    public int getWidgetCount() {
        return ul.getWidgetCount();
    }
    
    @Override
    public int getOffsetHeight(){
    	return ul.getOffsetHeight();
    }
    
    @Override
    public int getOffsetWidth(){
    	return ul.getOffsetWidth();
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
        while (i < ul.getWidgetCount() && !found) {
            ListItem li = (ListItem)ul.getWidget(i);
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
        while (i < ul.getWidgetCount() && !found) {
            ListItem li = (ListItem)ul.getWidget(i);
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
     * Removes all child widgets
     */
    public void clear() {
        ul.clear();
    }
    
    
    
    /**
     * Adds a secondary or dependent style name to this object.
     * @param style the new style name
     */
    public void setStyleName(String style) {
        super.addStyleName(style);
    }
    
    /**
     * 
     * @author PCSOS
     */
    public class OrderedList extends ComplexPanel {
        public OrderedList() {
            setElement(DOM.createElement("ol"));
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
    public class UnorderedList extends ComplexPanel {
        public UnorderedList() {
            setElement(DOM.createElement("ul"));
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
        public ListItem() {
            setElement(DOM.createElement("li"));
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

    }
}
