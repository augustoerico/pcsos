package epusp.pcs.os.login.shared;

import java.io.Serializable;

public class URLConfig implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String urlPath;
	private String locale;
	
	public URLConfig(){
	}
	
	public void setUrlPath(String urlPath){
		this.urlPath = urlPath;		
	}

	public String getUrlPath(){
		return urlPath;
	}

	public String getLocale(){
		return locale;
	}

	public void setLocale(String locale){
		this.locale = locale;
	}

}
