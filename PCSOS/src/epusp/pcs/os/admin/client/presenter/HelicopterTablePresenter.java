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
import epusp.pcs.os.shared.model.vehicle.Helicopter;
import epusp.pcs.os.shared.provider.AsyncHelicopterProvider;

public class HelicopterTablePresenter implements Presenter{

	private final IAdminWorkspaceServiceAsync rpcService;
	private final AdminWorkspaceConstants constants;
	private final int pageSize;
	
	private final CellTable<Helicopter> table = new CellTable<Helicopter>();
	
	private final SimplePager pager = new SimplePager();
	
	private final DockLayoutPanel dockLayoutPanel = new DockLayoutPanel(Unit.PX);
	
	private final SingleSelectionModel<Helicopter> selectionModel = new SingleSelectionModel<Helicopter>(new ProvidesKey<Helicopter>() {
		@Override
		public Object getKey(Helicopter item) {
			return item == null ? null : item.getId();
		}
	});
	
	private final SelectedRowHandler<Helicopter> handler;
	
	public HelicopterTablePresenter(IAdminWorkspaceServiceAsync rpcService, AdminWorkspaceConstants constants, int pageSize, SelectedRowHandler<Helicopter> handler){
		this.rpcService = rpcService;
		this.constants = constants;
		this.pageSize = pageSize;
		this.handler = handler;
	}

	@Override
	public void go(HasWidgets container) {		
		AsyncHelicopterProvider helicopterProvider = new AsyncHelicopterProvider(table, pager, rpcService, pageSize);
		table.setPageSize(pageSize);
		helicopterProvider.addDataDisplay(table);
		pager.setDisplay(table);
		
		TextColumn<Helicopter> idTagColumn = new TextColumn<Helicopter>() {
			@Override
			public String getValue(Helicopter object) {
					return object.getIdTag();
			}
		};

		TextColumn<Helicopter> priorityColumn = new TextColumn<Helicopter>() {
			@Override
			public String getValue(Helicopter object) {
				return object.getPriority().name();
			}
		};

		Column<Helicopter, String> pictureColumn = new Column<Helicopter, String>(new ImageCell()) {
			@Override
			public String getValue(Helicopter object) {
				return "";      
			}

			@Override
			public void render(Context context, Helicopter object,
					SafeHtmlBuilder sb) {
				super.render(context, object, sb);
				sb.appendHtmlConstant("<img src = '"+object.getImageURL()+"' height = '50px' />");
			}

		};
		
		table.setSelectionModel(selectionModel);
		
		pictureColumn.setCellStyleNames("picture");
		
		table.addColumn(idTagColumn, constants.tag());
		
		table.addColumn(priorityColumn, constants.priority());
		
		table.addColumn(pictureColumn, constants.picture());
		
		dockLayoutPanel.setSize("100%", "100%");
		dockLayoutPanel.addSouth(pager, 35);
		dockLayoutPanel.add(table);
		
		container.add(dockLayoutPanel);
		
		bind();
	}
	
	private void bind(){
		selectionModel.addSelectionChangeHandler(new Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				handler.onSelectedRow(selectionModel.getSelectedObject());
			}
		});
	}

}
