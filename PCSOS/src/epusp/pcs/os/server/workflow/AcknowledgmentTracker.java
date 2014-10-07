package epusp.pcs.os.server.workflow;

import java.util.concurrent.CopyOnWriteArrayList;

public class AcknowledgmentTracker {
	
	private final CopyOnWriteArrayList<String> waitingAcknowledgment =
			new CopyOnWriteArrayList<String>();
	
	
	public void add(String id){
		waitingAcknowledgment.add(id);
	}
	
	public void remove(String id){
		waitingAcknowledgment.remove(id);
	}
	
	public Boolean isEmpty(){
		return waitingAcknowledgment.isEmpty();
	}
}
