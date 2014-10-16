package epusp.pcs.os.monitor.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;

import epusp.pcs.os.shared.client.SharedResources;

public interface MonitorResources extends SharedResources {
	
	public static final MonitorResources INSTANCE = GWT.create(MonitorResources.class);
	
	@Source("images/info-ico.png")
	ImageResource info();
	
	@Source("images/map-ico.png")
	ImageResource map();
	
	@Source("images/reinforcements-ico.png")
	ImageResource reinforcements();
	
	@Source("images/victim-marker.png")
	ImageResource victimMarker();
	
}