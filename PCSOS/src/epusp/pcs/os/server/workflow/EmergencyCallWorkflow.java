package epusp.pcs.os.server.workflow;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.jdo.PersistenceManager;

import epusp.pcs.os.server.PMF;
import epusp.pcs.os.shared.model.oncall.EmergencyCall;
import epusp.pcs.os.shared.model.oncall.EmergencyCallLifecycle;
import epusp.pcs.os.shared.model.oncall.Position;
import epusp.pcs.os.shared.model.oncall.VehicleOnCall;
import epusp.pcs.os.shared.model.person.Victim;
import epusp.pcs.os.shared.model.person.user.Agent;
import epusp.pcs.os.shared.model.person.user.Monitor;
import epusp.pcs.os.shared.model.vehicle.Car;
import epusp.pcs.os.shared.model.vehicle.Vehicle;

public enum EmergencyCallWorkflow {
	INSTANCE;

	private final ConcurrentLinkedDeque<EmergencyCall> waitingCalls =
			new ConcurrentLinkedDeque<EmergencyCall>();
	
	private final ConcurrentLinkedDeque<Monitor> freeMonitors =
			new ConcurrentLinkedDeque<Monitor>();
	
	private final CopyOnWriteArrayList<Vehicle> freePrimaryVehicles =
			new CopyOnWriteArrayList<Vehicle>();
	
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
	public void addWaitingCall(String victimEmail){
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
				System.out.println("Emergency call: " + emergencyCall.getVictimEmail() + " begin: " + emergencyCall.getBegin());
				waitingCalls.addLast(emergencyCall);
				activeCalls.putIfAbsent(detached.getEmail(), emergencyCall);
				activeVictims.putIfAbsent(detached.getEmail(), victim);
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
	
	public void addFreeVehicle(String vehicleIdTag, List<Agent> agents){
		if(!activeVehicles.containsKey(vehicleIdTag)){
			PersistenceManager mgr = getPersistenceManager();
			Vehicle vehicle, detached = null;
			try {
				vehicle = mgr.getObjectById(Car.class, vehicleIdTag);
				if(vehicle != null)
					detached = mgr.detachCopy(vehicle);
			} finally {
				mgr.close();
			}

			if(detached != null){
				switch(detached.getPriority()){
				case PRIMARY:
					freePrimaryVehicles.add(detached);
					break;
				case SUPPORT:
					freeSupportVehicles.put(detached.getId(), detached);
					break;
				default:
					break;
				}
				detached.addAgents(agents);
				activeVehicles.put(vehicleIdTag, detached);
				if(!waitingCalls.isEmpty() && !freeMonitors.isEmpty())
					associate();
			}
		}
	}
	
	private void associate(){
		EmergencyCall emergencyCall = waitingCalls.pollFirst();
		Monitor monitor = freeMonitors.pollFirst();
		
		emergencyCall.setMonitor(monitor.getId());
		Position victimPosition = null;
		if(emergencyCall.getVictimPositionSize() > 0)
			victimPosition = emergencyCall.getLastVictimPosition();
		//Choose best car...
		Vehicle vehicle = freePrimaryVehicles.remove(0);
		List<String> agents = new ArrayList<String>();
		for(Agent agent : vehicle.getAgents())
			agents.add(agent.getId());
		
		emergencyCall.addVehicle(vehicle.getIdTag(), agents);
		emergencyCall.addVehiclePosition(vehicle.getIdTag(), vehicle.getPosition());
		emergencyCall.setEmergencyCallLifecycle(EmergencyCallLifecycle.WaitingAcknowledgment);
		vehiclesOnCall.put(vehicle.getIdTag(), emergencyCall);
		monitorsOnCall.put(monitor.getId(), emergencyCall);
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
				freePrimaryVehicles.add(vehicle);
				if(!waitingCalls.isEmpty() && !freeMonitors.isEmpty())
					associate();
				break;
			case SUPPORT:
				freeSupportVehicles.put(vehicle.getId(), vehicle);
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
			finish(emergencyCall);
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
				freeSupportVehicles.remove(vehicleIdTag);
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
			 for(Vehicle vehicle : freePrimaryVehicles){
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
	
	public void monitorLeaving(String monitorId){
		if(freeMonitors.contains(monitorId)){
			Monitor monitor = activeMonitors.get(monitorId);
			activeMonitors.remove(monitorId);
			freeMonitors.remove(monitor);
		}
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