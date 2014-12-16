package epusp.pcs.os.shared.model;

import java.util.HashMap;

public class VictimAttributesMap {
	HashMap<String, String> primaryMap;
	HashMap<String, String> secondaryMap;
	
	public VictimAttributesMap() {
		primaryMap = new HashMap<String, String>();
		secondaryMap = new HashMap<String, String>();
	}

	public HashMap<String, String> getPrimaryMap() {
		return primaryMap;
	}

	public void setPrimaryMap(HashMap<String, String> primaryMap) {
		this.primaryMap = primaryMap;
	}

	public HashMap<String, String> getSecondaryMap() {
		return secondaryMap;
	}

	public void setSecondaryMap(HashMap<String, String> secondaryMap) {
		this.secondaryMap = secondaryMap;
	}

}
