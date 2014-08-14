package epusp.pcs.os.server.login;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.json.JSONObject;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

import epusp.pcs.os.login.client.rpc.ILoginService;
import epusp.pcs.os.model.person.user.Admin;
import epusp.pcs.os.model.person.user.User;
import epusp.pcs.os.server.Connection;

public class LoginConnection extends Connection implements ILoginService{

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(LoginConnection.class.getCanonicalName());
	
    public void init() throws ServletException
    {
//         System.out.println("Creating temporary data for testing");
//         DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
//         Entity admin = new Entity("Admin");
//         admin.setProperty("name","Giovanni");
//         admin.setProperty("surname", "Gatti Pinheiro");
//         admin.setProperty("email", "giovanni.gatti.pinheiro@gmail.com");
//         admin.setProperty("isActive", true);
//         datastore.put(admin);
    }

	@Override
	public User loginDetails(String token){
		if(token != null){
			String url = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + token;

			final StringBuffer r = new StringBuffer();
			try {
				final URL u = new URL(url);
				final URLConnection uc = u.openConnection();
				final int end = 1000;
				InputStreamReader isr = null;
				BufferedReader br = null;
				try {
					isr = new InputStreamReader(uc.getInputStream());
					br = new BufferedReader(isr);
					final int chk = 0;
					while ((url = br.readLine()) != null) {
						if ((chk >= 0) && ((chk < end))) {
							r.append(url).append('\n');
						}
					}
				} catch (final java.net.ConnectException cex) {
					r.append(cex.getMessage());
				} catch (final Exception ex) {
					log.log(Level.SEVERE, ex.getMessage());
				} finally {
					try {
						br.close();
					} catch (final Exception ex) {
						log.log(Level.SEVERE, ex.getMessage());
					}
				}
			} catch (final Exception e) {
				log.log(Level.SEVERE, e.getMessage());
			}
			System.out.println(r.toString());
			
			try {
				final JSONObject obj = new JSONObject(r.toString());
			} catch (Exception e) {
				log.log(Level.SEVERE, e.getMessage());
			}
			
			User user = new Admin("", "");
			
			return user;
		}else
			return null;
	}
}
