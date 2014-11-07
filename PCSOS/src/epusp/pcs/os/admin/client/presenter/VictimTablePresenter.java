package epusp.pcs.os.admin.client.presenter;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.ImageCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasWidgets;

import epusp.pcs.os.admin.client.constants.AdminWorkspaceConstants;
import epusp.pcs.os.admin.client.rpc.IAdminWorkspaceServiceAsync;
import epusp.pcs.os.shared.client.presenter.Presenter;
import epusp.pcs.os.shared.model.person.Victim;
import epusp.pcs.os.shared.provider.AsyncVictimProvider;

public class VictimTablePresenter implements Presenter{
	
	private final IAdminWorkspaceServiceAsync rpcService;
	private final AdminWorkspaceConstants constants;
	private final int pageSize;
	
	private final CellTable<Victim> table = new CellTable<Victim>();
	
	private final SimplePager pager = new SimplePager();
	
	private final DockLayoutPanel dockLayoutPanel = new DockLayoutPanel(Unit.PX);
	
	public VictimTablePresenter(IAdminWorkspaceServiceAsync rpcService, AdminWorkspaceConstants constants, int pageSize){
		this.rpcService = rpcService;
		this.constants = constants;
		this.pageSize = pageSize;
	}

	@Override
	public void go(HasWidgets container) {		
		AsyncVictimProvider victimProvider = new AsyncVictimProvider(table, pager, rpcService, pageSize);
		table.setPageSize(pageSize);
		victimProvider.addDataDisplay(table);
		pager.setDisplay(table);
		
		TextColumn<Victim> victimNameColumn = new TextColumn<Victim>() {
			@Override
			public String getValue(Victim object) {
				if(object.getSecondName() != null)
					return object.getName() + " " + object.getSecondName();
				else
					return object.getName();
			}
		};

		TextColumn<Victim> victimSurnameColumn = new TextColumn<Victim>() {
			@Override
			public String getValue(Victim object) {
				return object.getSurname();
			}
		};



		TextColumn<Victim> victimEmailColumn = new TextColumn<Victim>() {
			@Override
			public String getValue(Victim object) {
				return object.getEmail();
			}
		};



		Column<Victim, String> victimPictureColumn = new Column<Victim, String>(new ImageCell()) {
			@Override
			public String getValue(Victim object) {
				return "";      
			}

			@Override
			public void render(Context context, Victim object,
					SafeHtmlBuilder sb) {
				super.render(context, object, sb);
				sb.appendHtmlConstant("<img src = '"+object.getPictureURL()+"' height = '50px' />");
			}

		};
		
		victimPictureColumn.setCellStyleNames("picture");
		
		table.addColumn(victimSurnameColumn, constants.surname());
		
		table.addColumn(victimNameColumn, constants.name());
		
		table.addColumn(victimEmailColumn, constants.email());

		table.addColumn(victimPictureColumn, constants.picture());
		
		dockLayoutPanel.setSize("100%", "100%");
		dockLayoutPanel.addSouth(pager, 35);
		dockLayoutPanel.add(table);
		
		container.add(dockLayoutPanel);
		
		bind();
	}
	
	private void bind(){
		
	}
}