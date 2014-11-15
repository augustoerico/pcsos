package epusp.pcs.os.superuser.client.presenter;

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

import epusp.pcs.os.shared.client.presenter.Presenter;
import epusp.pcs.os.shared.model.person.user.User;
import epusp.pcs.os.superuser.client.constants.SuperUserWorkspaceConstants;
import epusp.pcs.os.superuser.client.rpc.ISuperUserWorkspaceServiceAsync;

public class UserTablePresenter implements Presenter {
	
	private final ISuperUserWorkspaceServiceAsync rpcService;
	private final SuperUserWorkspaceConstants constants;
	private final int pageSize;
	
	private final CellTable<User> table = new CellTable<User>();
	
	private final SimplePager pager = new SimplePager();
	
	private final DockLayoutPanel dockLayoutPanel = new DockLayoutPanel(Unit.PX);

	public UserTablePresenter(ISuperUserWorkspaceServiceAsync rpcService, SuperUserWorkspaceConstants constants, int pageSize){
		this.rpcService = rpcService;
		this.constants = constants;
		this.pageSize = pageSize;
	}
	
	@Override
	public void go(HasWidgets container) {
		table.setPageSize(pageSize);
		pager.setDisplay(table);
		
		TextColumn<User> agentNameColumn = new TextColumn<User>() {
			@Override
			public String getValue(User object) {
				if(object.getSecondName() != null)
					return object.getName() + " " + object.getSecondName();
				else
					return object.getName();
			}
		};
		
		TextColumn<User> agentSurnameColumn = new TextColumn<User>() {
			@Override
			public String getValue(User object) {
				return object.getSurname();
			}
		};
		
		TextColumn<User> agentEmailColumn = new TextColumn<User>() {
			@Override
			public String getValue(User object) {
				return object.getEmail();
			}
		};
		
		Column<User, String> agentPictureColumn = new Column<User, String>(new ImageCell()) {
			@Override
			public String getValue(User object) {
				return "";      
			}

			@Override
			public void render(Context context, User object,
					SafeHtmlBuilder sb) {
				super.render(context, object, sb);
				sb.appendHtmlConstant("<img src = '"+object.getPictureURL()+"' height = '50px' />");
			}
		};
		
		agentPictureColumn.setCellStyleNames("picture");
		
		table.addColumn(agentSurnameColumn, constants.surname());
		
		table.addColumn(agentNameColumn, constants.name());
		
		table.addColumn(agentEmailColumn, constants.email());
		
		table.addColumn(agentPictureColumn, constants.picture());
		
		dockLayoutPanel.setSize("100%", "100%");
		dockLayoutPanel.addSouth(pager, 35);
		dockLayoutPanel.add(table);
		
		container.add(dockLayoutPanel);
		
		bind();
	}
	
	private void bind(){
		
	}
	
	protected ISuperUserWorkspaceServiceAsync getRpcService(){
		return rpcService;
	}
	
	protected CellTable<User> getTable(){
		return table;
	}
	
	protected int getPageSize(){
		return pageSize;
	}
	
	protected SimplePager getPager(){
		return pager;
	}


}
