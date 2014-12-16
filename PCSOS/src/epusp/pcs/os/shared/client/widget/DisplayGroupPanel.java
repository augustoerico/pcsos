package epusp.pcs.os.shared.client.widget;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.Widget;

import epusp.pcs.os.shared.client.panel.HorizontalDivPanel;
import epusp.pcs.os.shared.client.panel.VerticalDivPanel;

public class DisplayGroupPanel extends HorizontalDivPanel{

	private int maxHeight;

	private List<VerticalDivPanel> verticalPanels = new ArrayList<VerticalDivPanel>();

	public DisplayGroupPanel(int maxHeight){
		super();
		this.maxHeight = maxHeight;
		VerticalDivPanel verticalPanel = new VerticalDivPanel();
		verticalPanels.add(verticalPanel);
		super.add(verticalPanel);
	}

	public void add(final Widget w) {
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				VerticalDivPanel lastVerticalPanel = verticalPanels.get(verticalPanels.size()-1);
				lastVerticalPanel.add(w);
				if(lastVerticalPanel.getOffsetHeight() > maxHeight){
					lastVerticalPanel.remove(w);
					VerticalDivPanel newVerticalPanel = new VerticalDivPanel();
					add(newVerticalPanel);
					verticalPanels.add(newVerticalPanel);
					newVerticalPanel.add(w);
				}
			}
		});
	}

	private void add(VerticalDivPanel verticalPanelDiv){
		super.add(verticalPanelDiv);
	}

	public void doLayout(){
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				Boolean ruleViolated = false;
				for(VerticalDivPanel verticalPanel : verticalPanels){
					if(verticalPanel.getOffsetHeight() > maxHeight){
						ruleViolated = true;
						break;
					}
				}

				if(ruleViolated){
					final List<Widget> widgets = new ArrayList<Widget>();
					for(VerticalDivPanel verticalPanel : verticalPanels){
						for(int i = 0; i < verticalPanel.getWidgetCount(); i++){
							Widget w = verticalPanel.getWidget(i);
							if(w != null)
								widgets.add(w);
						}
					}
					
					for(VerticalDivPanel verticalPanel : verticalPanels){
						verticalPanel.clear();
					}
					
					for(Widget w : widgets)
						add(w);
				}
			}
		});
	}
}
