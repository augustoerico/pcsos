package epusp.pcs.os.admin.client.presenter;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.ImageCell;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ImageResource;
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
import epusp.pcs.os.shared.client.SharedResources;
import epusp.pcs.os.shared.client.presenter.Presenter;
import epusp.pcs.os.shared.general.SelectedRowHandler;
import epusp.pcs.os.shared.model.vehicle.motorcycle.Motorcycle;
import epusp.pcs.os.shared.provider.AsyncMotorcycleProvider;

public class MotorcycleTablePresenter implements Presenter{

	private final IAdminWorkspaceServiceAsync rpcService;
	private final AdminWorkspaceConstants constants;
	private final int pageSize;
	private final SharedResources resources;
	
	private final CellTable<Motorcycle> table = new CellTable<Motorcycle>();
	
	private final SimplePager pager = new SimplePager();
	
	private final SingleSelectionModel<Motorcycle> selectionModel = new SingleSelectionModel<Motorcycle>(new ProvidesKey<Motorcycle>() {
		@Override
		public Object getKey(Motorcycle item) {
			return item == null ? null : item.getId();
		}
	});
	
	private final SelectedRowHandler<Motorcycle> handler;
	
	private final DockLayoutPanel dockLayoutPanel = new DockLayoutPanel(Unit.PX);
	
	public MotorcycleTablePresenter(IAdminWorkspaceServiceAsync rpcService, AdminWorkspaceConstants constants, SharedResources resources,
			int pageSize, SelectedRowHandler<Motorcycle> handler){
		this.rpcService = rpcService;
		this.constants = constants;
		this.pageSize = pageSize;
		this.handler = handler;
		this.resources = resources;
	}

	@Override
	public void go(HasWidgets container) {		
		AsyncMotorcycleProvider motorcycleProvider = new AsyncMotorcycleProvider(table, pager, rpcService, pageSize);
		table.setPageSize(pageSize);
		motorcycleProvider.addDataDisplay(table);
		pager.setDisplay(table);
		
		TextColumn<Motorcycle> idTagColumn = new TextColumn<Motorcycle>() {
			@Override
			public String getValue(Motorcycle object) {
				return object.getIdTag();
			}
		};

		TextColumn<Motorcycle> plateColumn = new TextColumn<Motorcycle>() {
			@Override
			public String getValue(Motorcycle object) {
				return object.getPlate();
			}
		};



		TextColumn<Motorcycle> priorityColumn = new TextColumn<Motorcycle>() {
			@Override
			public String getValue(Motorcycle object) {
				return object.getPriority().getText();
			}
		};

		Column<Motorcycle, ImageResource> motorcycleActiveColumn = new Column<Motorcycle, ImageResource>(new ImageResourceCell()) {
			@Override
			public ImageResource getValue(Motorcycle object) {
				if(object.isActive())
					return resources.active();
				else
					return resources.inactive();
			}
		};

		Column<Motorcycle, String> pictureColumn = new Column<Motorcycle, String>(new ImageCell()) {
			@Override
			public String getValue(Motorcycle object) {
				return "";      
			}

			@Override
			public void render(Context context, Motorcycle object,
					SafeHtmlBuilder sb) {
				super.render(context, object, sb);
				sb.appendHtmlConstant("<img src = '"+object.getImageURL()+"' height = '50px' />");
			}

		};
		
		table.setSelectionModel(selectionModel);
		
		pictureColumn.setCellStyleNames("picture");
		
		table.addColumn(idTagColumn, constants.tag());
		
		table.addColumn(plateColumn, constants.plate());
		
		table.addColumn(priorityColumn, constants.priority());

		table.addColumn(pictureColumn, constants.picture());
		
		table.addColumn(motorcycleActiveColumn, constants.active());
		
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
