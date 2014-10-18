package epusp.pcs.os.server;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import epusp.pcs.os.model.person.user.AvailableLanguages;
import epusp.pcs.os.model.person.user.Monitor;
import epusp.pcs.os.model.person.user.User;
import epusp.pcs.os.server.login.AuthenticationManager;
import epusp.pcs.os.server.workflow.EmergencyCallWorkflow;
import epusp.pcs.os.shared.client.rpc.IConnectionService;
import epusp.pcs.os.shared.exception.LoginException;

public class Connection extends RemoteServiceServlet implements IConnectionService{

	private static final long serialVersionUID = 1L;
	
	protected EmergencyCallWorkflow workflow = EmergencyCallWorkflow.getInstance();
	
	protected AuthenticationManager authenticationManager = AuthenticationManager.getInstance();
	
	protected String userSessionAttribute = "userSessionAttribute";
	
	public void init() throws ServletException
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();

		System.out.println("Initializing with test data");
		Monitor monitor = new Monitor("Giovanni", "Gatti Pinheiro", "giovanni.gatti.pinheiro@gmail.com");
		monitor.setIsActive(true);
		monitor.setGoogleUserId("115057125698280242918");
		monitor.setPictureURL("https://lh5.googleusercontent.com/--PBV1HBWVsc/AAAAAAAAAAI/AAAAAAAAAFQ/O57isBLRtRA/photo.jpg");
		monitor.setPreferedLanguage(AvailableLanguages.ENGLISH);
	
		try{
			pm.currentTransaction().begin();
			pm.makePersistent(monitor);
			pm.currentTransaction().commit();
		}catch (Exception e){
			e.printStackTrace();
			if(pm.currentTransaction().isActive())
				pm.currentTransaction().rollback();
		}finally{
			pm.close();
		}
		
//		System.out.println("Create Monitor...");
//		Monitor monitor = new Monitor("Monitor", "da Silva", "monitor.silva@gmail.com");
//		monitor.setIsActive(true);
//		monitor.setGoogleUserId("0000001");
//		
//		System.out.println("Create Cars...");
//		Car car = new Car("PCS-0505");
//		car.setPrioraty(Priority.PRIMARY);
//
//		Car supportCar = new Car("PCS-0506");
//		supportCar.setPrioraty(Priority.SUPPORT);
//		
//		System.out.println("Create Agents...");
//		Agent agent1 = new Agent("David", "Starsky", "david.starsk@gmail.com");
//		agent1.setIsActive(true);
//		agent1.setGoogleUserId("0000002");
//		
//		Agent agent2 = new Agent("Ken", "Hutchinson", "ken.hutchinson@gmail.com");
//		agent2.setIsActive(true);
//		agent2.setGoogleUserId("0000003");
//		
//		Agent agent3 = new Agent("Alex", "J. Murphy", "ken.hutchinson@gmail.com");
//		agent3.setIsActive(true);
//		agent3.setGoogleUserId("0000004");
//		
//		System.out.println("Create Victim");
//		Victim victim = new Victim("Zezinho", "da Silva", "zezinho.silva@gmail.com");
//		victim.setIsActive(true);
//		victim.setGoogleUserId("0000004");
//		
//		Victim victim2 = new Victim("Luizinho", "da Silva", "luizinho.silva@gmail.com");
//		victim2.setIsActive(true);
//		victim2.setGoogleUserId("0000005");
//		
//		Car carX = new Car("PCS-0507");
//		carX.setPrioraty(Priority.PRIMARY);
//		
//		Agent agentX = new Agent("Mr. Satan", "Jr.", "satan.junior@gmail.com");
//		agentX.setIsActive(true);
//		agentX.setGoogleUserId("0000006");
//		
//		System.out.println("Persisting and detaching objects");
//		Agent detachedAgent1 = null, detachedAgent2 = null, detachedAgent3 = null;
//		Car detachedCar = null, detachedSupportCar = null;
//		Monitor detachedMonitor = null;
//		Victim detachedVictim = null, detachedVictim2 = null;
//		
//		try{
//			pm.currentTransaction().begin();
//			
////			pm.makePersistent(admin);
//			pm.makePersistent(monitor);
//			detachedMonitor = pm.detachCopy(monitor);
//			pm.makePersistent(car);
//			detachedCar = pm.detachCopy(car);
//			pm.makePersistent(agent1);
//			detachedAgent1 = pm.detachCopy(agent1);
//			pm.makePersistent(agent2);
//			detachedAgent2 = pm.detachCopy(agent2);
//			pm.makePersistent(agent3);
//			detachedAgent3 = pm.detachCopy(agent3);
//			pm.makePersistent(victim);
//			detachedVictim = pm.detachCopy(victim);
//			
//			pm.currentTransaction().commit();
//		}catch (Exception e){
//			e.printStackTrace();
//			if(pm.currentTransaction().isActive())
//				pm.currentTransaction().rollback();
//		}finally{
//			pm.close();
//		}
//		
//		pm = PMF.get().getPersistenceManager();
//		
//		try{
//			pm.currentTransaction().begin();
//			pm.makePersistent(supportCar);
//			detachedSupportCar = pm.detachCopy(supportCar);
//			pm.makePersistent(victim2);
//			detachedVictim2 = pm.detachCopy(victim2);
//			pm.makePersistent(carX);
//			pm.makePersistent(agentX);
//			pm.currentTransaction().commit();
//		}catch (Exception e){
//			e.printStackTrace();
//			if(pm.currentTransaction().isActive())
//				pm.currentTransaction().rollback();
//		}finally{
//			pm.close();
//		}
//		
//		System.out.println("Victim starts call");
//		workflow.addWaitingCall(detachedVictim.getEmail());
//		System.out.println(workflow.getEmergencyCallLifecycle(detachedVictim.getEmail()));
//		
//		workflow.addVictimPosition(detachedVictim.getEmail(), new Position(3082.109,-313.1312));
//		
//		System.out.println("Adding free suport vehicle to workflow");
//		List<Agent> l = new ArrayList<Agent>();
//		l.add(detachedAgent3);
//		workflow.addFreeVehicle(detachedSupportCar.getId(), l);
//		System.out.println("Support car state: " + workflow.isVehicleOnCall(detachedSupportCar.getId()));
//		
//		workflow.addVehiclePosition(detachedSupportCar.getId(), new Position(22222.22222, 33333.33333));
//		
//		System.out.println("Adding monitor to workflow");
//		workflow.addFreeMonitor(detachedMonitor.getId());
//		System.out.println("Support monitor state: " + workflow.isMonitorOnCall(detachedMonitor.getId()));
//		
//		System.out.println(workflow.getEmergencyCallLifecycle(detachedVictim.getEmail()));
//		
//		System.out.println("Adding vehicle to workflow");
//		
//		l.clear();
//		l.add(detachedAgent1);
//		l.add(detachedAgent2);
//		workflow.addFreeVehicle(detachedCar.getId(), l);
//		System.out.println("Support monitor state: " + workflow.isVehicleOnCall(detachedCar.getId()));
//		
//		System.out.println("Adding vehicle position");
//		workflow.addVehiclePosition(detachedCar.getId(), new Position(12312.1231, 213213.331231));
//		
//		System.out.println(workflow.getEmergencyCallLifecycle(detachedVictim.getEmail()));
//		
//		System.out.println("--------------------------Association Phase Completed--------------------------");
//		
//		workflow.addFreeMonitor(detachedMonitor.getId());
//		System.out.println("Support monitor state: " + workflow.isMonitorOnCall(detachedMonitor.getId()));
//		
//	
//		System.out.println("Support car state: " + workflow.isVehicleOnCall(detachedSupportCar.getId()));
//		
//		workflow.monitorOnCallAcknowledgment(detachedMonitor.getId());
//		System.out.println(workflow.getEmergencyCallLifecycle(detachedVictim.getEmail()));
//		
//		workflow.vehicleOnCallAcknowledgment(detachedCar.getId());
//		System.out.println(workflow.getEmergencyCallLifecycle(detachedVictim.getEmail()));
//		
//		System.out.println("--------------------------Acknoledment Phase Completed--------------------------");
//		EmergencyCall monitorEmergencyCall = workflow.getMonitorEmergencyCall(detachedMonitor.getId());
//		EmergencyCall vehicleEmergencyCall = workflow.getVehicleEmergencyCall(detachedCar.getId());
//		
//		Victim v = workflow.getVictim(monitorEmergencyCall.getVictimEmail());
//		System.out.println("Monitor view:");
//		System.out.println("Victim name: " + v.getName() + " " + v.getSurname() +", id:" +  v.getId());
//		System.out.println("Victim Position: " + monitorEmergencyCall.getLastVictimPosition().toString());
//		for(VehicleOnCall vehicle : monitorEmergencyCall.getVehicles()){
//			Vehicle vhc = workflow.getVehicle(vehicle.getVehicleId());
//			Position vehiclePosition = vehicleEmergencyCall.getLastVehiclePosition(vehicle.getVehicleId());
//			if(!vehiclePosition.isEmpty())
//				System.out.println("Vehicle id: " + vehicle.getVehicleId() + ", position: " + vehiclePosition.toString());
//			else
//				System.out.println("Vehicle id: " + vehicle.getVehicleId());
//			for(Agent agent : vhc.getAgents()){
//				System.out.println("   - Agent: " + agent.getName() +  " " + agent.getSurname());
//			}
//		}
//		
//		System.out.println("Vehicle View:");
//		Position victimPosition = vehicleEmergencyCall.getLastVictimPosition();
//		v = workflow.getVictim(vehicleEmergencyCall.getVictimEmail());
//		Monitor m = workflow.getMonitor(monitor.getId());
//		if(!victimPosition.isEmpty())
//			System.out.println("Victim name: " + v.getName() + " " + v.getSurname() + ", position: " + victimPosition.toString());
//		else
//			System.out.println("Victim name: " + v.getName() + " " + v.getSurname());
//		System.out.println("Monitor name: " + m.getName() + " " + m.getSurname()+
//							", id: " + m.getId());
//		
//		
//		System.out.println("Available support vehicles: " + workflow.getAvailableSupportVehicles());
//		
//		//Adding some more positions...
//		workflow.addVictimPosition(detachedVictim.getEmail(), new Position(721.390128, -32871.3123));
//		workflow.addVehiclePosition(detachedCar.getId(), new Position(38129.312231, 312321.312));
//		
//		monitorEmergencyCall = workflow.getMonitorEmergencyCall(detachedMonitor.getId());
//		vehicleEmergencyCall = workflow.getVehicleEmergencyCall(detachedCar.getId());
//
//		System.out.println("Monitor view:");
//		System.out.println("Victim Position: " + monitorEmergencyCall.getLastVictimPosition().toString());
//		for(VehicleOnCall vehicle : monitorEmergencyCall.getVehicles()){
//			Vehicle vhc = workflow.getVehicle(vehicle.getVehicleId());
//			Position vehiclePosition = vehicleEmergencyCall.getLastVehiclePosition(vehicle.getVehicleId());
//			if(!vehiclePosition.isEmpty())
//				System.out.println("Vehicle id: " + vehicle.getVehicleId() + ", position: " + vehiclePosition.toString());
//			else
//				System.out.println("Vehicle id: " + vehicle.getVehicleId());
//			for(Agent agent : vhc.getAgents()){
//				System.out.println("   - Agent: " + agent.getName() +  " " + agent.getSurname());
//			}
//		}
//		
//		System.out.println("Vehicle View:");
//		victimPosition = vehicleEmergencyCall.getLastVictimPosition();
//		victimPosition = vehicleEmergencyCall.getLastVictimPosition();
//		v = workflow.getVictim(vehicleEmergencyCall.getVictimEmail());
//		m = workflow.getMonitor(monitor.getId());
//		if(!victimPosition.isEmpty())
//			System.out.println("Victim name: " + v.getName() + " " + v.getSurname() + ", position: " + victimPosition.toString());
//		else
//			System.out.println("Victim name: " + v.getName() + " " + v.getSurname());
//		System.out.println("Monitor name: " + m.getName() + " " + m.getSurname()+
//							", id: " + m.getId());
//		
//		//Adding new vehicle to Call
//		v = workflow.getVictim(monitorEmergencyCall.getVictimEmail());
//		workflow.addVehicleToCall(v.getEmail(), detachedSupportCar.getId());
//		
//		System.out.println("Available support vehicles: " + workflow.getAvailableSupportVehicles());
//		
//		workflow.addVictimPosition(detachedVictim.getEmail(), new Position(312231.123132, -321321.31312));
//		workflow.addVehiclePosition(detachedCar.getId(), new Position(379132.31231, 312231.312321));
//		workflow.addVehiclePosition(detachedSupportCar.getId(), new Position(31.21, 22.22));
//		
//		System.out.println("Monitor view:");
//		System.out.println("Victim Position: " + monitorEmergencyCall.getLastVictimPosition().toString());
//		for(VehicleOnCall vehicle : monitorEmergencyCall.getVehicles()){
//			Vehicle vhc = workflow.getVehicle(vehicle.getVehicleId());
//			Position vehiclePosition = vehicleEmergencyCall.getLastVehiclePosition(vehicle.getVehicleId());
//			if(!vehiclePosition.isEmpty())
//				System.out.println("Vehicle id: " + vehicle.getVehicleId() + ", position: " + vehiclePosition.toString());
//			else
//				System.out.println("Vehicle id: " + vehicle.getVehicleId());
//			for(Agent agent : vhc.getAgents()){
//				System.out.println("   - Agent: " + agent.getName() +  " " + agent.getSurname());
//			}
//		}
//
//		System.out.println("Vehicle View:");
//		victimPosition = vehicleEmergencyCall.getLastVictimPosition();
//		v = workflow.getVictim(vehicleEmergencyCall.getVictimEmail());
//		m = workflow.getMonitor(vehicleEmergencyCall.getMonitor());
//		System.out.println("Victim name: " + v.getName() + " " + v.getSurname() + ", position: " + victimPosition.toString());
//		System.out.println("Monitor name: " + m.getName() + " " + m.getSurname());
//		for(VehicleOnCall vehicle : vehicleEmergencyCall.getVehicles()){
//			Vehicle vhc = workflow.getVehicle(vehicle.getVehicleId());
//			Position vehiclePosition = vehicleEmergencyCall.getLastVehiclePosition(vehicle.getVehicleId());
//			if(!vehiclePosition.isEmpty())
//				System.out.println("Vehicle id: " + vehicle.getVehicleId() + ", position: " + vehiclePosition.toString());
//			else
//				System.out.println("Vehicle id: " + vehicle.getVehicleId());
//			for(Agent agent : vhc.getAgents()){
//				System.out.println("   - Agent: " + agent.getName() +  " " + agent.getSurname());
//			}
//		}
//		
//		System.out.println("--------------------------Call Phase Completed--------------------------");
//		workflow.finishCall(detachedVictim.getEmail());
//		
//		System.out.println(workflow.getEmergencyCallLifecycle(detachedVictim.getEmail()));
//		
//		workflow.addWaitingCall(detachedVictim2.getEmail());
//		
//		List<Agent> agents = new ArrayList<Agent>();
//		agents.add(agentX);
//		workflow.addFreeVehicle(carX.getId(), agents);
//		
//		workflow.monitorFinishedCallAcknowledgment(detachedMonitor.getId());
//		System.out.println(workflow.isMonitorOnCall(detachedMonitor.getId()));
//		
//		workflow.vehicleFinishedCallAcknowledgment(detachedSupportCar.getId());
//		System.out.println(workflow.isVehicleOnCall(detachedSupportCar.getId()));
//		
//		workflow.vehicleFinishedCallAcknowledgment(detachedCar.getId());
//		System.out.println(workflow.isVehicleOnCall(detachedCar.getId()));
//		
//		System.out.println("--------------------------Finished Phase Completed--------------------------");
//		
//		System.out.println(workflow.isMonitorOnCall(detachedMonitor.getId()));
//		
//		System.out.println(workflow.isVehicleOnCall(detachedSupportCar.getId()));
//		
//		System.out.println(workflow.isVehicleOnCall(detachedCar.getId()));
//		
//		System.out.println("--------------------------Restart Phase Completed--------------------------");
//		workflow.vehicleLeaving(detachedSupportCar.getId());
//		System.out.println(workflow.getAvailableSupportVehicles());
	}
	
	@Override
	public void identifySession(String key) throws LoginException{
		if(authenticationManager.isLoggedIn(key)){
			User user = authenticationManager.getUser(key);
			setSessionAttribute(userSessionAttribute, user);
		}else
			throw new LoginException();
	}
	
	@Override
	public User getUserInfo(){
		if(isLoggedIn()){
			return (User) getSessionAttibute(userSessionAttribute);
		}
		return null;
	}
	
	protected Boolean isLoggedIn(){
		return getSessionAttibute(userSessionAttribute) != null;
	}
	
	protected void setSessionAttribute(String attribute, Object value){
		this.getThreadLocalRequest().getSession(true).setAttribute(attribute, value);
	}
	
	protected Object getSessionAttibute(String attribute){
		return this.getThreadLocalRequest().getSession(true).getAttribute(userSessionAttribute);
	}

}
