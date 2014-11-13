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
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;

import epusp.pcs.os.admin.client.constants.AdminWorkspaceConstants;
import epusp.pcs.os.admin.client.rpc.IAdminWorkspaceServiceAsync;
import epusp.pcs.os.shared.client.presenter.Presenter;
import epusp.pcs.os.shared.general.SelectedRowHandler;
import epusp.pcs.os.shared.model.person.user.Agent;
import epusp.pcs.os.shared.provider.AsyncAgentProvider;

public class AgentTablePresenter implements Presenter{
	
	private final IAdminWorkspaceServiceAsync rpcService;
	private final AdminWorkspaceConstants constants;
	private final int pageSize;
	
	private final CellTable<Agent> table = new CellTable<Agent>();
	
	private final SimplePager pager = new SimplePager();
	
	private final DockLayoutPanel dockLayoutPanel = new DockLayoutPanel(Unit.PX);
	
	private final SingleSelectionModel<Agent> selectionModel = new SingleSelectionModel<Agent>(new ProvidesKey<Agent>() {
		@Override
		public Object getKey(Agent item) {
			return item == null ? null : item.getId();
		}
	});
	
	private final SelectedRowHandler<Agent> handler;
	
	public AgentTablePresenter(IAdminWorkspaceServiceAsync rpcService, AdminWorkspaceConstants constants, int pageSize, SelectedRowHandler<Agent> handler){
		this.rpcService = rpcService;
		this.constants = constants;
		this.pageSize = pageSize;
		this.handler = handler;
	}

	@Override
	public void go(HasWidgets container) {		
		AsyncAgentProvider agentProvider = new AsyncAgentProvider(table, pager, rpcService, pageSize);		
		table.setPageSize(pageSize);
		agentProvider.addDataDisplay(table);
		pager.setDisplay(table);
		
		TextColumn<Agent> agentNameColumn = new TextColumn<Agent>() {
			@Override
			public String getValue(Agent object) {
				if(object.getSecondName() != null)
					return object.getName() + " " + object.getSecondName();
				else
					return object.getName();
			}
		};
		
		TextColumn<Agent> agentSurnameColumn = new TextColumn<Agent>() {
			@Override
			public String getValue(Agent object) {
				return object.getSurname();
			}
		};
		
		TextColumn<Agent> agentEmailColumn = new TextColumn<Agent>() {
			@Override
			public String getValue(Agent object) {
				return object.getEmail();
			}
		};
		
		Column<Agent, String> agentPictureColumn = new Column<Agent, String>(new ImageCell()) {
			@Override
			public String getValue(Agent object) {
				return "";      
			}

			@Override
			public void render(Context context, Agent object,
					SafeHtmlBuilder sb) {
				super.render(context, object, sb);
				sb.appendHtmlConstant("<img src = '"+object.getPictureURL()+"' height = '50px' />");
			}
		};
		
		agentPictureColumn.setCellStyleNames("picture");
		
		table.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				handler.onSelectedRow(selectionModel.getSelectedObject());
			}
		});
		
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
}
