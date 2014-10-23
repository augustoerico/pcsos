package epusp.pcs.os.server.monitor;

import javax.servlet.ServletException;

import epusp.pcs.os.monitor.client.rpc.IMonitorWorkspaceService;
import epusp.pcs.os.monitor.shared.EmergencyCallSpecs;
import epusp.pcs.os.server.Connection;
import epusp.pcs.os.shared.model.oncall.EmergencyCall;
import epusp.pcs.os.shared.model.oncall.Position;
import epusp.pcs.os.shared.model.person.user.AccountTypes;
import epusp.pcs.os.shared.model.person.user.Monitor;
import epusp.pcs.os.shared.model.person.user.User;

public class MonitorWorkspaceConnection extends Connection implements IMonitorWorkspaceService{

	private static final long serialVersionUID = 1L;
	
	@Override
	public void init() throws ServletException{}

	@Override
	protected Boolean isLoggedIn() {
		if(super.isLoggedIn()){
			User user = (User) getSessionAttibute(userSessionAttribute);
			if(user.getType().equals(AccountTypes.Monitor)){
				return true;
			}
		}
		return false;
	}

	@Override
	public EmergencyCall getEmergencyCallDetails(EmergencyCallSpecs specs){
		if(isLoggedIn()){
			Monitor monitor = (Monitor) getSessionAttibute(userSessionAttribute);
			
//			test();
			
			if(specs.getVictimLastPositionIndex() == -1 && specs.getVehiclesLastPositionsIndex().isEmpty())
				return workflow.getMonitorEmergencyCall(monitor.getId());
			else
				return workflow.getMonitorEmergencyCall(monitor.getId(), specs.getVehiclesLastPositionsIndex(), specs.getVictimLastPositionIndex());
		}
		return null;
	}
	
	//test method
	//remover
	public static int test = 0;
	private void test(){
		/*************************************************************/
		Position ATLANTA = new Position(33.7814790, -84.3880580);
		Position STONE_MOUNTAIN_PARK = new Position(33.80653802509606, -84.15252685546875);
		Position CYCLORAMA = new Position(33.741185330333956, -84.35834884643555);
		Position GEORGIA_AQUARIUM = new Position(33.761443931868925, -84.39432263374329);
		Position UNDERGROUND_ATLANTA = new Position(33.75134645137294, -84.39026713371277);
		
		Position[] p = new Position[]{ATLANTA,STONE_MOUNTAIN_PARK,CYCLORAMA, GEORGIA_AQUARIUM, UNDERGROUND_ATLANTA};
		
	
		workflow.addVehiclePosition("TAG001", p[test%5]);
		workflow.addVictimPosition("augusto.ericosilva@gmail.com", p[test++%5]);
		System.out.println(test%5);
		/*************************************************************/
	}

	@Override
	public void addFreeMonitor(){
		if(isLoggedIn()){
			Monitor monitor = (Monitor) getSessionAttibute(userSessionAttribute);
			workflow.addFreeMonitor(monitor.getId());
		}
	}

	@Override
	public Boolean isMonitorOnCall(){
		if(isLoggedIn()){
			Monitor monitor = (Monitor) getSessionAttibute(userSessionAttribute);
			Boolean onCall = workflow.isMonitorOnCall(monitor.getId());
			if(onCall)
				workflow.monitorOnCallAcknowledgment(monitor.getId());
			return onCall;
		}
		return null;
	}

	//TODO: Override métodos getVehicle e getVictim para pegar direto do workflow!
}
