package epusp.pcs.os.server.workflow;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.jdo.PersistenceManager;

import epusp.pcs.os.model.oncall.EmergencyCall;
import epusp.pcs.os.model.oncall.EmergencyCallLifecycle;
import epusp.pcs.os.model.oncall.Position;
import epusp.pcs.os.model.person.Victim;
import epusp.pcs.os.model.person.user.Monitor;
import epusp.pcs.os.model.vehicle.Car;
import epusp.pcs.os.model.vehicle.Vehicle;
import epusp.pcs.os.server.PMF;

public enum EmergencyCallWorkflow {
	INSTANCE;

	private final ConcurrentLinkedDeque<EmergencyCall> waitingCalls =
			new ConcurrentLinkedDeque<EmergencyCall>();
	
	private final ConcurrentLinkedDeque<Monitor> freeMonitors =
			new ConcurrentLinkedDeque<Monitor>();
	
	private final CopyOnWriteArrayList<Car> freeCars =
			new CopyOnWriteArrayList<Car>();
	
//	private final CopyOnWriteArrayList<Vehicle> freeSupportVehicles =
//			new CopyOnWriteArrayList<Vehicle>();
	
	private final ConcurrentHashMap<Long, Vehicle> activeVehicles =
			new ConcurrentHashMap<Long, Vehicle>();
	
	private final ConcurrentHashMap<String, EmergencyCall> activeCalls =
			new ConcurrentHashMap<String, EmergencyCall>();
	
	private final ConcurrentHashMap<String, EmergencyCall> monitorsOnCall =
			new ConcurrentHashMap<String, EmergencyCall>();
	
	private final ConcurrentHashMap<Long, EmergencyCall> vehiclesOnCall =
			new ConcurrentHashMap<Long, EmergencyCall>();
	
	private EmergencyCallWorkflow(){}
	
	public static EmergencyCallWorkflow getInstance(){
		return INSTANCE;
	}
	
	public void addWaitingCall(String victimId){
		if(!activeCalls.containsKey(victimId)){
			PersistenceManager mgr = getPersistenceManager();
			Victim victim, detached = null;
			try {
				victim = mgr.getObjectById(Victim.class, victimId);
				detached = mgr.detachCopy(victim);
			} finally {
				mgr.close();
			}
			
			if(detached != null){
				EmergencyCall emergencyCall = new EmergencyCall(new Date(), detached);
				waitingCalls.addLast(emergencyCall);
				activeCalls.put(victimId, emergencyCall);
				if(!freeMonitors.isEmpty() && !freeCars.isEmpty())
					associate();
			}
		}
	}
	
	public void addFreeMonitor(String monitorId){
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
				if(!waitingCalls.isEmpty() && !freeCars.isEmpty())
					associate();
			}
		}
	}
	
	public void addFreeCar(Long carId){
		if(!activeVehicles.contains(carId)){
			PersistenceManager mgr = getPersistenceManager();
			Car car, detached = null;
			try {
				car = mgr.getObjectById(Car.class, carId);
				detached = mgr.detachCopy(car);
			} finally {
				mgr.close();
			}

			if(detached != null){
				freeCars.add(detached);
				activeVehicles.put(carId, detached);
				if(!waitingCalls.isEmpty() && !freeMonitors.isEmpty())
					associate();
			}
		}
	}
	
	private void associate(){
		EmergencyCall emergencyCall = waitingCalls.pollFirst();
		Monitor monitor = freeMonitors.pollFirst();
		
		emergencyCall.setMonitor(monitor);
		
		Position victimPosition = emergencyCall.getVictimPosition(emergencyCall.getVictimPositionSize()-1);
		//Choose best car...
		Car car = freeCars.remove(0);
		emergencyCall.addVehicle(car.getId(), car.getAgents());
		vehiclesOnCall.put(car.getId(), emergencyCall);
		monitorsOnCall.put(monitor.getId(), emergencyCall);
		emergencyCall.setEmergencyCallLifecycle(EmergencyCallLifecycle.WaitingAcknowledgment);
	}
	
	public Boolean hasBeenAssociated(String monitorId){
		return monitorsOnCall.contains(monitorId);
	}
	
	public Boolean hasBeenAssociated(Long vehicleId){
		return vehiclesOnCall.contains(vehicleId);
	}
	
	public void acknowledgment(String monitorId){
		EmergencyCall emergencyCall = monitorsOnCall.get(monitorId);
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

	public void acknowledgment(Long carId){
		EmergencyCall emergencyCall = vehiclesOnCall.get(carId);
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
	
	public EmergencyCallLifecycle getEmergencyCallLifecycle(String victimId){
		return activeCalls.get(victimId).getEmergencyCallLifecycle();
	}	
	
	/* General Update Methods */
	public void addVictimPosition(String victimId, Position position){
		activeCalls.get(victimId).addVictimPosition(position);
	}
	
	public void addVehiclePosition(Long vehicleId, Position position){
		activeVehicles.get(vehicleId).setPosition(position);
		if(vehiclesOnCall.contains(vehicleId)){
			vehiclesOnCall.get(vehicleId).addVehiclePosition(vehicleId, position);
		}
	}
	
//	public void addVehicle(Long vehicleId, String victimId){
//		De onde virá o outro veículo?
//		Como será o processo em que esse veículo é informado que está numa chamada?
//		como assosciar essa chamada a esse veículo?
//	}
	
	private static PersistenceManager getPersistenceManager() {
		return PMF.get().getPersistenceManager();
	}
}