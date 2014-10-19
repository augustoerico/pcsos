package epusp.pcs.os.server.login;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import org.json.JSONObject;

import epusp.pcs.os.login.client.rpc.ILoginService;
import epusp.pcs.os.login.shared.LoginConfig;
import epusp.pcs.os.server.Connection;
import epusp.pcs.os.server.PMF;
import epusp.pcs.os.shared.exception.DeniedAccess;
import epusp.pcs.os.shared.model.person.user.Admin;
import epusp.pcs.os.shared.model.person.user.Auditor;
import epusp.pcs.os.shared.model.person.user.Monitor;
import epusp.pcs.os.shared.model.person.user.SuperUser;
import epusp.pcs.os.shared.model.person.user.User;

public class LoginConnection extends Connection implements ILoginService{

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(LoginConnection.class.getCanonicalName());

	@Override
	public LoginConfig login(String token) throws DeniedAccess{
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

			String userEmail = null;
			String userPicture = null;
			try {
				final JSONObject obj = new JSONObject(r.toString());
				userEmail = obj.getString("email");
				userPicture = obj.getString("picture");
			} catch (Exception e) {
				log.log(Level.SEVERE, e.getMessage());
			}

			if(userEmail != null){
				PersistenceManager pm = PMF.get().getPersistenceManager();

				pm = PMF.get().getPersistenceManager();
				User user = null;
				try{
					user = pm.getObjectById(Monitor.class, userEmail);
				}catch(Exception e){
				}finally{				
					pm.close();
				}

				pm = PMF.get().getPersistenceManager();
				if(user == null){
					try{
						user = pm.getObjectById(Admin.class, userEmail);
					}catch(Exception e){
					}finally{
						pm.close();
					}
				}

				pm = PMF.get().getPersistenceManager();
				if(user == null){
					try{
						user = pm.getObjectById(Auditor.class, userEmail);
					}catch(Exception e){
					}finally{
						pm.close();
					}
				}

				pm = PMF.get().getPersistenceManager();
				if(user == null){
					try{
						user = pm.getObjectById(SuperUser.class, userEmail);
					}catch(Exception e){
					}finally{
						pm.close();
					}
				}

				if(user != null && user.isActive()){
					
					if(!userPicture.equals(user.getPictureURL())){
						user.setPictureURL(userPicture);
						
						Admin admin = null;
						Monitor monitor = null;
						Auditor auditor = null;
						SuperUser superUser = null;
						switch (user.getType()) {
						case Admin:
							admin = (Admin) user;
							break;
						case Auditor:
							auditor = (Auditor) user;
							break;
						case Monitor:
							monitor = (Monitor) user;
							break;
						case SuperUser:
							superUser = (SuperUser) user;
							break;
						default:
							System.out.println("Denied access to " + user.getEmail());
							return null;
						}
						
						pm = PMF.get().getPersistenceManager();
						try{
							pm.currentTransaction().begin();
							if(admin != null)
								pm.makePersistent(admin);
							else if(monitor != null)
								pm.makePersistent(monitor);
							else if(auditor != null)
								pm.makePersistent(auditor);
							else if(superUser != null)
								pm.makePersistent(superUser);
							pm.currentTransaction().commit();
						}catch (Exception e){
							e.printStackTrace();
							if(pm.currentTransaction().isActive())
								pm.currentTransaction().rollback();
						}finally{
							pm.close();
						}
						
					}

					LoginConfig config = new LoginConfig();

					switch (user.getPreferedLanguage()) {
					case ENGLISH:
						config.setLocale("en");
						break;
					case PORTUGUES:
						config.setLocale("pt");
						break;
					default:
						config.setLocale("en");
						break;
					}
					
					String key = super.authenticationManager.login(user);
					config.setKey(key);

					switch(user.getType()){
					case Admin:
						System.out.println("Admin " + user.getEmail() + " has logged in.");
						config.setUrlPath("todo");
						return config;
					case Auditor:
						System.out.println("Aditor " + user.getEmail() + " has logged in.");
						config.setUrlPath("todo");
						return config;
					case Monitor:
						System.out.println("Monitor " + user.getEmail() + " has logged in.");
						config.setUrlPath("MonitorWorkspace.html");
						return config;
					case SuperUser:
						System.out.println("Super user " + user.getEmail() + " has logged in.");
						config.setUrlPath("todo");
						return config;
					default:
						System.out.println("Denied access to " + user.getEmail());
						throw new DeniedAccess();
					}
				}else
					System.out.println("User not found " + userEmail);
			}else
				System.out.println("Google feedback doesn't contain user email");
		}else
			System.out.println("Missing access token from client");
		throw new DeniedAccess();
	}
}
