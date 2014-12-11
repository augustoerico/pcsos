package epusp.pcs.os.server.workflow;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import javax.jdo.PersistenceManager;

import epusp.pcs.os.server.PMF;
import epusp.pcs.os.shared.model.oncall.EmergencyCall;
import epusp.pcs.os.shared.model.oncall.EmergencyCallLifecycle;
import epusp.pcs.os.shared.model.oncall.Position;
import epusp.pcs.os.shared.model.oncall.VehicleOnCall;
import epusp.pcs.os.shared.model.person.user.agent.Agent;
import epusp.pcs.os.shared.model.person.user.monitor.Monitor;
import epusp.pcs.os.shared.model.person.victim.Victim;
import epusp.pcs.os.shared.model.vehicle.Vehicle;
import epusp.pcs.os.shared.model.vehicle.car.Car;
import epusp.pcs.os.shared.model.vehicle.helicopter.Helicopter;

public enum EmergencyCallWorkflow {
	INSTANCE;

	private final ConcurrentLinkedDeque<EmergencyCall> waitingCalls =
			new ConcurrentLinkedDeque<EmergencyCall>();
	
	private final ConcurrentLinkedDeque<Monitor> freeMonitors =
			new ConcurrentLinkedDeque<Monitor>();
	
	private final ConcurrentHashMap<String, Vehicle> freePrimaryVehicles =
			new ConcurrentHashMap<String, Vehicle>();
	
	private final ConcurrentHashMap<String, Vehicle> freeSupportVehicles =
			new ConcurrentHashMap<String, Vehicle>();

	private final ConcurrentHashMap<String, Vehicle> activeVehicles =
			new ConcurrentHashMap<String, Vehicle>();
	
	private final ConcurrentHashMap<String, Monitor> activeMonitors =
			new ConcurrentHashMap<String, Monitor>();
	
	private final ConcurrentHashMap<String, EmergencyCall> activeCalls =
			new ConcurrentHashMap<String, EmergencyCall>();
	
	private final ConcurrentHashMap<String, Victim> activeVictims =
			new ConcurrentHashMap<String, Victim>();
	
	private final ConcurrentHashMap<String, EmergencyCall> monitorsOnCall =
			new ConcurrentHashMap<String, EmergencyCall>();
	
	private final ConcurrentHashMap<String, EmergencyCall> vehiclesOnCall =
			new ConcurrentHashMap<String, EmergencyCall>();
	
	private final ConcurrentHashMap<String, AcknowledgmentTracker> ackControl =
			new ConcurrentHashMap<String, AcknowledgmentTracker>();
	
	private EmergencyCallWorkflow(){}
	
	public static EmergencyCallWorkflow getInstance(){
		return INSTANCE;
	}
	
	/*
	 * Available resources update methods
	 */
	public void addWaitingCall(String victimEmail, Position position){
		if(!activeCalls.containsKey(victimEmail)){
			PersistenceManager mgr = getPersistenceManager();
			Victim victim, detached = null;
			try {
				victim = mgr.getObjectById(Victim.class, victimEmail);
				detached = mgr.detachCopy(victim);
			} finally {
				mgr.close();
			}
			
			if(detached != null){
				EmergencyCall emergencyCall = new EmergencyCall(new Date(), victimEmail);
				
				System.out.println("Emergency call for " + emergencyCall.getVictimEmail() + ", begin " + emergencyCall.getBegin() + " at " + position.toString());
				waitingCalls.addLast(emergencyCall);
				activeCalls.putIfAbsent(detached.getEmail(), emergencyCall);
				activeVictims.putIfAbsent(detached.getEmail(), victim);
				addVictimPosition(victimEmail, position);
				if(!freeMonitors.isEmpty() && !freePrimaryVehicles.isEmpty())
					associate();
			}
		}
	}
	
	public void addFreeMonitor(String monitorId){
		if(!activeMonitors.containsKey(monitorId)){
			PersistenceManager mgr = getPersistenceManager();
			Monitor monitor, detached = null;
			try {
				monitor = mgr.getObjectById(Monitor.class, monitorId);
				detached = mgr.detachCopy(monitor);
			} finally {
				mgr.close();
			}

			if(detached != null){
				if(!freeMonitors.contains(detached)){
					freeMonitors.addLast(detached);
					activeMonitors.put(detached.getId(), detached);
					if(!waitingCalls.isEmpty() && !freePrimaryVehicles.isEmpty())
						associate();
				}
			}
		}
	}
	
	public void addFreeVehicle(String vehicleIdTag, List<Agent> agents, Position position){
		if(!activeVehicles.containsKey(vehicleIdTag)){
			PersistenceManager mgr = getPersistenceManager();
			mgr.getFetchPlan().addGroup("all_system_object_attributes");
			Vehicle vehicle = null, detached = null;
			try {
				vehicle = mgr.getObjectById(Car.class, vehicleIdTag);
				if(vehicle != null)
					detached = mgr.detachCopy(vehicle);
			}catch (Exception e){
			} finally {
				mgr.close();
			}
			
			if(vehicle == null){
				mgr = getPersistenceManager();
				mgr.getFetchPlan().addGroup("all_system_object_attributes");
				try {
					vehicle = mgr.getObjectById(Helicopter.class, vehicleIdTag);
					if(vehicle != null)
						detached = mgr.detachCopy(vehicle);
				}catch (Exception e){
				} finally {
					mgr.close();
				}
			}

			if(detached != null){
				switch(detached.getPriority()){
				case PRIMARY:
					freePrimaryVehicles.put(detached.getIdTag(), detached);
					break;
				case SUPPORT:
					freeSupportVehicles.put(detached.getIdTag(), detached);
					break;
				default:
					break;
				}
				detached.addAgents(agents);
				activeVehicles.put(vehicleIdTag, detached);
				addVehiclePosition(vehicleIdTag, position);
				if(!waitingCalls.isEmpty() && !freeMonitors.isEmpty())
					associate();
			}
		}
	}
	
	private void associate(){
		final EmergencyCall emergencyCall = waitingCalls.pollFirst();
		final Monitor monitor = freeMonitors.pollFirst();
		
		emergencyCall.setMonitor(monitor.getId());
		
		final Position victimPosition;
		
		if(emergencyCall.getVictimPositionSize() > 0)
			victimPosition = emergencyCall.getLastVictimPosition();
		else
			victimPosition = null;
		
		final List<Position> positions = new ArrayList<Position>();
		final List<Vehicle> vehicles = new ArrayList<Vehicle>();
		
		for(Vehicle vehicle : freePrimaryVehicles.values()){
			if(!vehicle.getPosition().isEmpty()){
				positions.add(vehicle.getPosition());
				vehicles.add(vehicle);
			}
		}
		
		DistanceCalculator calculator = new DistanceCalculator();
		
		int min = calculator.minTimeDistance(victimPosition, positions);
		
		String closest = null;
		if(min >= 0){
			closest = vehicles.get(min).getIdTag();
			System.out.println("Closest vehicle from call: " + closest);
		}else{
			//Something went wrong: victim or any free primary vehicle have a position... So we can't decide which one is the closest. On this case, just pick one.
			closest =freePrimaryVehicles.keySet().iterator().next();
			System.out.println("Missing positions, selected: " + closest);
		}
		
		Vehicle vehicle = freePrimaryVehicles.remove(closest);
		List<String> agents = new ArrayList<String>();

		for(Agent agent : vehicle.getAgents())
			agents.add(agent.getId());
		
		emergencyCall.addVehicle(vehicle.getIdTag(), agents);
		emergencyCall.addVehiclePosition(vehicle.getIdTag(), vehicle.getPosition());
		emergencyCall.setEmergencyCallLifecycle(EmergencyCallLifecycle.WaitingAcknowledgment);
		vehiclesOnCall.put(vehicle.getIdTag(), emergencyCall);
		monitorsOnCall.put(monitor.getId(), emergencyCall);
		
		System.out.println(vehicle.getPosition().toString());
	}
	
	/*
	 * Check Emergency Call state
	 */
	public Boolean isMonitorOnCall(String monitorId){
		return monitorsOnCall.containsKey(monitorId);
	}
	
	public Boolean isVehicleOnCall(String vehicleIdTag){
		return vehiclesOnCall.containsKey(vehicleIdTag);
	}
	
	public void monitorOnCallAcknowledgment(String monitorId){
		EmergencyCall emergencyCall = monitorsOnCall.get(monitorId);
		if(emergencyCall != null)
			switch (emergencyCall.getEmergencyCallLifecycle()) {
			case WaitingAcknowledgment:
				emergencyCall.setEmergencyCallLifecycle(EmergencyCallLifecycle.WaitingCarAcknowledgment);
				break;
			case WaitingMonitorAcknowledgment:
				emergencyCall.setEmergencyCallLifecycle(EmergencyCallLifecycle.OnCall);
				break;
			default:
				break;
			}
	}

	public void vehicleOnCallAcknowledgment(String vehicleIdTag){
		EmergencyCall emergencyCall = vehiclesOnCall.get(vehicleIdTag);
		if(emergencyCall != null)
			switch (emergencyCall.getEmergencyCallLifecycle()) {
			case WaitingAcknowledgment:
				emergencyCall.setEmergencyCallLifecycle(EmergencyCallLifecycle.WaitingMonitorAcknowledgment);
				break;
			case WaitingCarAcknowledgment:
				emergencyCall.setEmergencyCallLifecycle(EmergencyCallLifecycle.OnCall);
				break;
			default:
				break;
			}
	}

	public EmergencyCallLifecycle getEmergencyCallLifecycle(String victimEmail){
		if(activeCalls.containsKey(victimEmail))
			return activeCalls.get(victimEmail).getEmergencyCallLifecycle();
		else
			return null;
	}	
	
	public void finishCall(String victimEmail){
		if(activeCalls.containsKey(victimEmail)){
			EmergencyCall emergencyCall = activeCalls.get(victimEmail);
			emergencyCall.setEmergencyCallLifecycle(EmergencyCallLifecycle.Finished);
			List<VehicleOnCall> vehicles = emergencyCall.getVehicles();
			String monitorId = emergencyCall.getMonitor();
			AcknowledgmentTracker tracker = new AcknowledgmentTracker();
			
			for(VehicleOnCall vehicleOnCall : vehicles)
				tracker.add(vehicleOnCall.getVehicleIdTag());
			
			tracker.add(monitorId);
			
			ackControl.put(victimEmail, tracker);
		}
	}
	
	
	public void vehicleFinishedCallAcknowledgment(String vehicleIdTag){
		EmergencyCall emergencyCall = vehiclesOnCall.get(vehicleIdTag);
		if(emergencyCall != null && emergencyCall.getEmergencyCallLifecycle().equals(EmergencyCallLifecycle.Finished)){
			vehiclesOnCall.remove(vehicleIdTag);
			Vehicle vehicle = activeVehicles.get(vehicleIdTag);
			AcknowledgmentTracker tracker = ackControl.get(emergencyCall.getVictimEmail());
			tracker.remove(vehicleIdTag);
			if(tracker.isEmpty())
				finish(emergencyCall);
			switch(vehicle.getPriority()){
			case PRIMARY:
				freePrimaryVehicles.put(vehicle.getIdTag(), vehicle);
				if(!waitingCalls.isEmpty() && !freeMonitors.isEmpty())
					associate();
				break;
			case SUPPORT:
				freeSupportVehicles.put(vehicle.getIdTag(), vehicle);
				break;
			default:
				break;
			}
		}
	}
	
	public void monitorFinishedCallAcknowledgment(String monitorId){
		EmergencyCall emergencyCall = monitorsOnCall.get(monitorId);
		if(emergencyCall != null && emergencyCall.getEmergencyCallLifecycle().equals(EmergencyCallLifecycle.Finished)){
			monitorsOnCall.remove(monitorId);
			Monitor monitor = activeMonitors.get(monitorId);

			AcknowledgmentTracker tracker = ackControl.get(emergencyCall.getVictimEmail());
			tracker.remove(monitorId);
			if(tracker.isEmpty())
				finish(emergencyCall);
			
			freeMonitors.add(monitor);
			if(!waitingCalls.isEmpty() && !freePrimaryVehicles.isEmpty())
				associate();
		}
	}
	
	private void finish(EmergencyCall emergencyCall){		
		emergencyCall.setEnd(new Date());
		PersistenceManager pm = getPersistenceManager();
		try{
			pm.currentTransaction().begin();
			pm.makePersistent(emergencyCall);
			pm.currentTransaction().commit();
		}catch (Exception e){
			e.printStackTrace();
			if(pm.currentTransaction().isActive())
				pm.currentTransaction().rollback();
		}finally{
			pm.close();
		}
	}
	/*
	 * State update methods
	 */
	public void addVictimPosition(String victimEmail, Position position){
		EmergencyCall emergencyCall = activeCalls.get(victimEmail);
		if(activeCalls.containsKey(victimEmail) && !emergencyCall.getEmergencyCallLifecycle().equals(EmergencyCallLifecycle.Finished)){
			emergencyCall.addVictimPosition(position);
		}
	}
	
	public void addVehiclePosition(String vehicleIdTag, Position position){
		if(!position.isEmpty() && activeVehicles.containsKey(vehicleIdTag)){
			if(vehiclesOnCall.containsKey(vehicleIdTag)){
				EmergencyCall emergencyCall = vehiclesOnCall.get(vehicleIdTag);
				if(emergencyCall != null && !emergencyCall.getEmergencyCallLifecycle().equals(EmergencyCallLifecycle.Finished)){
					vehiclesOnCall.get(vehicleIdTag).addVehiclePosition(vehicleIdTag, position);
				}
			}
			activeVehicles.get(vehicleIdTag).setPosition(position);
		}
	}
	
	public void addVehicleToCall(String victimEmail, String vehicleIdTag){
		Vehicle vehicle = activeVehicles.get(vehicleIdTag);
		if(activeCalls.containsKey(victimEmail)){
			EmergencyCall call = activeCalls.get(victimEmail);
			if(call != null && !call.getEmergencyCallLifecycle().equals(EmergencyCallLifecycle.Finished)){
				List<String> agents = new ArrayList<String>();
				for(Agent agent : vehicle.getAgents())
					agents.add(agent.getId());
				call.addVehicle(vehicleIdTag, agents);
				Position p = vehicle.getPosition();
				if(!p.isEmpty()){
					call.addVehiclePosition(vehicleIdTag, p);
				}
				switch (vehicle.getPriority()) {
				case PRIMARY:
					freePrimaryVehicles.remove(vehicleIdTag);
					break;
				case SUPPORT:
					freeSupportVehicles.remove(vehicleIdTag);
					break;
				default:
					break;				
				}
				vehiclesOnCall.put(vehicle.getIdTag(), call);
			}
		}
	}
	
	/*
	 * Check available resources
	 */
	public List<Vehicle> getAvailableSupportVehicles(){
		 List<Vehicle> available = new ArrayList<Vehicle>();
		
		 Enumeration<Vehicle> vehicles = freeSupportVehicles.elements();
		 
		 while(vehicles.hasMoreElements()){
			 Vehicle vehicle = vehicles.nextElement();
			 available.add(vehicle);
		 }
		 
		 if(available.isEmpty()){
			 vehicles = freePrimaryVehicles.elements();
			 while(vehicles.hasMoreElements()){
				 Vehicle vehicle = vehicles.nextElement();
				 available.add(vehicle);
			 }
		 }
		 
		 return available;
	}
	
	
	/*
	 * Other methods
	 */
	public EmergencyCall getMonitorEmergencyCall(String monitorId){
		return monitorsOnCall.get(monitorId);
	}
	
	public EmergencyCall getVehicleEmergencyCall(String vehicleIdTag){
		return vehiclesOnCall.get(vehicleIdTag);
	}
	
	public EmergencyCall getMonitorEmergencyCall(String monitorId, HashMap<String, Integer> vehicleLastPositions, int victimLastPosition){
		EmergencyCall call = monitorsOnCall.get(monitorId);
		EmergencyCall emergencyCall = new EmergencyCall(call.getBegin(), call.getVictimEmail());
		
		emergencyCall.setEmergencyCallLifecycle(call.getEmergencyCallLifecycle());
		emergencyCall.setMonitor(monitorId);

		for(VehicleOnCall vehicle : call.getVehicles()){
			int i = 0;
			if(vehicleLastPositions.containsKey(vehicle.getVehicleIdTag())){
				i = vehicleLastPositions.get(vehicle.getVehicleIdTag());
			}
			emergencyCall.addVehicle(vehicle.getVehicleIdTag(), vehicle.getAgents());
			emergencyCall.addVehiclePositions(vehicle.getVehicleIdTag(),  vehicle.getPositions(i));
		}
		List<Position> positions = call.getVictimPositions(victimLastPosition);
		emergencyCall.addVictimPositions(positions);
		
		return emergencyCall;
	}
	
	public void addAgentsToVehicle(String vehicleIdTag, List<Agent> agents){
		if(activeVehicles.containsKey(vehicleIdTag)){
			Vehicle vehicle = activeVehicles.get(vehicleIdTag);
			vehicle.addAgents(agents);
		}
	}

	public void addAgentToVehicle(String vehicleIdTag, Agent agent){
		if(activeVehicles.containsKey(vehicleIdTag)){
			Vehicle vehicle = activeVehicles.get(vehicleIdTag);
			vehicle.addAgent(agent);
		}
	}

	public void removeAgentFromVehicle(String vehicleIdTag, Agent agent){
		if(activeVehicles.containsKey(vehicleIdTag)){
			Vehicle vehicle = activeVehicles.get(vehicleIdTag);
			vehicle.removeAgent(agent);
		}
	}

	public void removeAllAgentsFromVehicle(String vehicleIdTag){
		if(activeVehicles.containsKey(vehicleIdTag)){
			Vehicle vehicle = activeVehicles.get(vehicleIdTag);
			vehicle.removeAgents();
		}
	}

	public Victim getVictim(String victimEmail){
		if(activeCalls.containsKey(victimEmail)){
			return activeVictims.get(victimEmail);
		}
		return null;
	}
	
	public Monitor getMonitor(String monitorId){
		if(activeMonitors.containsKey(monitorId)){
			return activeMonitors.get(monitorId);
		}
		return null;
	}
	
	public Vehicle getVehicle(String vehicleIdTag){
		if(activeVehicles.containsKey(vehicleIdTag)){
			return activeVehicles.get(vehicleIdTag);
		}
		return null;
	}

	public Boolean monitorLeaving(String monitorId){
		if(freeMonitors.contains(monitorId)){
			Monitor monitor = activeMonitors.get(monitorId);
			if(!monitorsOnCall.containsKey(monitorId)){
				activeMonitors.remove(monitorId);
				freeMonitors.remove(monitor);
				return true;
			}else
				return false;
		}
		return true;
	}
	
	public void vehicleLeaving(String vehicleIdTag){
		if(freeSupportVehicles.containsKey(vehicleIdTag) || freePrimaryVehicles.contains(vehicleIdTag)){
			Vehicle vehicle = activeVehicles.get(vehicleIdTag);
			activeVehicles.remove(vehicleIdTag);
			
			switch(vehicle.getPriority()){
			case PRIMARY:
				freePrimaryVehicles.remove(vehicle);
				break;
			case SUPPORT:
				freeSupportVehicles.remove(vehicleIdTag);
				break;
			default:
				break;
			}			
		}
	}
	
	/*
	 * private methods
	 */
	private static PersistenceManager getPersistenceManager() {
		return PMF.get().getPersistenceManager();
	}
}