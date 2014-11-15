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
import epusp.pcs.os.shared.model.vehicle.Car;
import epusp.pcs.os.shared.provider.AsyncCarProvider;

public class CarTablePresenter implements Presenter{

	private final IAdminWorkspaceServiceAsync rpcService;
	private final AdminWorkspaceConstants constants;
	private final int pageSize;
	
	private final CellTable<Car> table = new CellTable<Car>();
	
	private final SimplePager pager = new SimplePager();
	
	private final SingleSelectionModel<Car> selectionModel = new SingleSelectionModel<Car>(new ProvidesKey<Car>() {
		@Override
		public Object getKey(Car item) {
			return item == null ? null : item.getId();
		}
	});
	
	private final SelectedRowHandler<Car> handler;
	
	private final DockLayoutPanel dockLayoutPanel = new DockLayoutPanel(Unit.PX);
	
	public CarTablePresenter(IAdminWorkspaceServiceAsync rpcService, AdminWorkspaceConstants constants, int pageSize, SelectedRowHandler<Car> handler){
		this.rpcService = rpcService;
		this.constants = constants;
		this.pageSize = pageSize;
		this.handler = handler;
	}

	@Override
	public void go(HasWidgets container) {		
		AsyncCarProvider carProvider = new AsyncCarProvider(table, pager, rpcService, pageSize);
		table.setPageSize(pageSize);
		carProvider.addDataDisplay(table);
		pager.setDisplay(table);
		
		TextColumn<Car> idTagColumn = new TextColumn<Car>() {
			@Override
			public String getValue(Car object) {
				return object.getIdTag();
			}
		};

		TextColumn<Car> plateColumn = new TextColumn<Car>() {
			@Override
			public String getValue(Car object) {
				return object.getPlate();
			}
		};



		TextColumn<Car> priorityColumn = new TextColumn<Car>() {
			@Override
			public String getValue(Car object) {
				return object.getPriority().name();
			}
		};



		Column<Car, String> pictureColumn = new Column<Car, String>(new ImageCell()) {
			@Override
			public String getValue(Car object) {
				return "";      
			}

			@Override
			public void render(Context context, Car object,
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
