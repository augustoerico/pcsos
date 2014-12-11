package epusp.pcs.os.server.workflow;

import epusp.pcs.os.shared.model.oncall.Position;

public class IndexPositionMapper implements Comparable<IndexPositionMapper>{

	private Position position;
	private int index;
	private double distance;
	
	public IndexPositionMapper(Position position, int index){
		this.position = position;
		distance = Math.sqrt(Math.pow(position.getLatitude(), 2) + Math.pow(position.getLongitude(), 2));
		this.index = index;
	}

	public int getIndex() {
		return index;
	}
	
	public double getDistance() {
		return distance;
	}
	
	public Position getPosition(){
		return position;
	}

	@Override
	public int compareTo(IndexPositionMapper o) {
		return Double.compare(distance, o.getDistance());
	}

}
