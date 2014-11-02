package epusp.pcs.os.admin.client.presenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.ImageCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

import epusp.pcs.os.admin.client.AdminResources;
import epusp.pcs.os.admin.client.constants.AdminWorkspaceConstants;
import epusp.pcs.os.admin.client.rpc.IAdminWorkspaceServiceAsync;
import epusp.pcs.os.shared.client.presenter.Presenter;
import epusp.pcs.os.shared.general.MoveCursor;
import epusp.pcs.os.shared.model.person.Victim;
import epusp.pcs.os.shared.model.person.user.Agent;
import epusp.pcs.os.shared.model.person.user.User;
import epusp.pcs.os.shared.model.vehicle.Vehicle;

public class WorkspacePresenter implements Presenter {

	public interface Display {
		void setUserImage(String url);
		void setUserImage(ImageResource resource);
		void setUsername(String username);
		Widget asWidget();
		Image getLogout();
		Image getPreferences();
		Boolean hasType(String type);
		void addType(String type, Widget header, Widget body);
		void addControl(String type, Widget control);
	}

	private final IAdminWorkspaceServiceAsync rpcService;
	private final Display display;
	private final AdminWorkspaceConstants constants;

	private final AdminResources resources = AdminResources.INSTANCE;

	private final CellTable<Victim> victimTable = new CellTable<Victim>();

	private final CellTable<Vehicle> vehicleTable = new CellTable<Vehicle>();

	private final CellTable<Agent> agentTable = new CellTable<Agent>();
	
	private SimplePager victimPager = new SimplePager();
	
//	private DynamicPagerPanel agentPager = new DynamicPagerPanel();
	
	private SimplePager agentPager = new SimplePager();
	
	private SimplePager vehiclePager = new SimplePager();
	
	private int agentStartRange = -1;

	public WorkspacePresenter(IAdminWorkspaceServiceAsync rpcService, Display view, AdminWorkspaceConstants constants) {
		this.rpcService = rpcService;
		this.display = view;
		this.constants = constants;
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());

		rpcService.getUserInfo(new AsyncCallback<User>() {

			@Override
			public void onSuccess(User result) {
				if(result != null){
					display.setUserImage(result.getPictureURL());
					display.setUsername(result.getName());
				}else{
					display.setUserImage(resources.user());
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				System.out.println(caught.getMessage());
			}
		});

		rpcService.getVictims(new AsyncCallback<Collection<Victim>>() {

			@Override
			public void onSuccess(Collection<Victim> result) {
				List<Victim> victims = new ArrayList<Victim>();
				victims.addAll(result);
				victimTable.setRowCount(result.size(), true);
				victimTable.setRowData(0, victims);				
			}

			@Override
			public void onFailure(Throwable caught) {
			}
		});
		
//		rpcService.getAgents(null, new AsyncCallback<Collection<Agent>>() {
//
//			@Override
//			public void onSuccess(Collection<Agent> result) {
//				List<Agent> agents = new ArrayList<Agent>();
//				agents.addAll(result);
//				agentTable.setRowCount(result.size(), true);
//				agentTable.setRowData(0, agents);				
//			}
//
//			@Override
//			public void onFailure(Throwable caught) {
//			}
//		});
		
		
		//Make this provider generic and move to shared package
		AsyncDataProvider<Agent> dataProvider = new AsyncDataProvider<Agent>() {			
			@Override
			protected void onRangeChanged(HasData<Agent> display) {
				final Range range = display.getVisibleRange();
				final int start = range.getStart();
				int end = start + range.getLength();
				final int totalNumberOfRows = display.getRowCount();
				
				System.out.println("**************************************");

				System.out.println("start: " + start);
				System.out.println("end: " + end);
				System.out.println("row count: " + totalNumberOfRows);
				
				MoveCursor move = null;
				
				if(start == 0){
					move = MoveCursor.FIRST;
				}/*else if(end == totalNumberOfRows){
					move = MoveCursor.LAST;
				}*/else if(start > agentStartRange){
					move = MoveCursor.FORWARD;
				}else{
					move = MoveCursor.BACKWARD;
				}
				
				agentStartRange = start;

				rpcService.getAgents(move, new AsyncCallback<Collection<Agent>>() {

					@Override
					public void onSuccess(Collection<Agent> result) {
						List<Agent> agents = new ArrayList<Agent>();
						agents.addAll(result);
						
						for(Agent agent : agents){
							System.out.println("agent: " + agent);
						}
						
						//I have to take care when the last page has excatly the same number as it fits at the page
						
						if(result.size() < range.getLength()){
							agentTable.setRowCount(8, true); //it has to be multiple of page size
						}
						
						if(!result.isEmpty())
							agentTable.setRowData(start, agents);
						else
							agentPager.firstPage(); //better change to last one.
						
						System.out.println(agentTable.getRowCount());
						
						System.out.println("**************************************");
					}

					@Override
					public void onFailure(Throwable caught) {
					}
				});
			}
		};
		
		agentTable.setPageSize(2);
		dataProvider.addDataDisplay(agentTable);
		
		victimPager.setDisplay(vehicleTable);
		agentPager.setDisplay(agentTable);
		vehiclePager.setDisplay(vehicleTable);
		
		display.addType(Victim.class.getName(), new Label(constants.client()), victimTable);
		display.addControl(Victim.class.getName(), victimPager);
		display.addType(Agent.class.getName(), new Label(constants.agent()), agentTable);
		display.addControl(Agent.class.getName(), agentPager);
		display.addType(Vehicle.class.getName(), new Label(constants.vehicle()), vehicleTable);
		display.addControl(Vehicle.class.getName(), vehiclePager);

		TextColumn<Victim> victimNameColumn = new TextColumn<Victim>() {
			@Override
			public String getValue(Victim object) {
				if(object.getSecondName() != null)
					return object.getName() + " " + object.getSecondName();
				else
					return object.getName();
			}
		};
		
		TextColumn<Agent> agentNameColumn = new TextColumn<Agent>() {
			@Override
			public String getValue(Agent object) {
				try{
					System.out.println(object);
				if(object.getSecondName() != null)
					return object.getName() + " " + object.getSecondName();
				else
					return object.getName();
			}catch(NullPointerException e){
				e.printStackTrace();
				return "";
			}
			}
		};

		TextColumn<Victim> victimSurnameColumn = new TextColumn<Victim>() {
			@Override
			public String getValue(Victim object) {
				return object.getSurname();
			}
		};
		
		TextColumn<Agent> agentSurnameColumn = new TextColumn<Agent>() {
			@Override
			public String getValue(Agent object) {
				try{
					System.out.println(object);
				return object.getSurname();
			}catch(NullPointerException e){
				e.printStackTrace();
				return "";
			}
			}
		};
		
		TextColumn<Victim> victimEmailColumn = new TextColumn<Victim>() {
			@Override
			public String getValue(Victim object) {
				try{
					System.out.println(object);
					return object.getEmail();
				}catch(NullPointerException e){
					e.printStackTrace();
					return "";
				}
				
			}
		};
		
		TextColumn<Agent> agentEmailColumn = new TextColumn<Agent>() {
			@Override
			public String getValue(Agent object) {
				try{
					System.out.println(object);
					return object.getEmail();
				}catch(NullPointerException e){
					e.printStackTrace();
					return "";
				}
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
		
		Column<Agent, String> agentPictureColumn = new Column<Agent, String>(new ImageCell()) {
		    @Override
		    public String getValue(Agent object) {
		        return "";      
		    }
		    
		    @Override
		    public void render(Context context, Agent object,
		    		SafeHtmlBuilder sb) {
				try{
					System.out.println(object);
				super.render(context, object, sb);
				 sb.appendHtmlConstant("<img src = '"+object.getPictureURL()+"' height = '50px' />");
			}catch(NullPointerException e){
				e.printStackTrace();
			}
		    }
		};
		
		agentPictureColumn.setCellStyleNames("picture");
		victimPictureColumn.setCellStyleNames("picture");
		
		agentTable.addColumn(agentSurnameColumn, constants.surname());
		victimTable.addColumn(victimSurnameColumn, constants.surname());

		agentTable.addColumn(agentNameColumn, constants.name());
		victimTable.addColumn(victimNameColumn, constants.name());
		
		agentTable.addColumn(agentEmailColumn, constants.email());
		victimTable.addColumn(victimEmailColumn, constants.email());
		
		agentTable.addColumn(agentPictureColumn, constants.picture());
		victimTable.addColumn(victimPictureColumn, constants.picture());

		bind();
	}

	private void bind() {		
		display.getLogout().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				rpcService.logout(new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						String path = Window.Location.getProtocol().concat("//").concat(Window.Location.getHost()).concat("/")
								.concat("PCSOS.html").concat(Window.Location.getQueryString());
						Window.Location.replace(path);
					}

					@Override
					public void onFailure(Throwable caught) {	
					}
				});
			}
		});

		//		CellTable<T>
		//		
		//		display.addType("SoBaiano", new Label("Só Baiano"));
		//		display.addType("SoPaulista", new Label("Só Paulista"));
		//		display.addItem("SoBaiano", new Label("Baianão"));
		//		display.addItem("SoPaulista", new Label("Seca da Morte"));

	}

	public Image getPreferencesButton() {
		return display.getPreferences();
	}


}
