package epusp.pcs.os.server.login;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;

import org.json.JSONObject;

import epusp.pcs.os.login.client.rpc.ILoginService;
import epusp.pcs.os.login.shared.LoginConfig;
import epusp.pcs.os.server.Connection;
import epusp.pcs.os.server.PMF;
import epusp.pcs.os.shared.exception.DeniedAccess;
import epusp.pcs.os.shared.model.person.user.AccountTypes;
import epusp.pcs.os.shared.model.person.user.Auditor;
import epusp.pcs.os.shared.model.person.user.User;
import epusp.pcs.os.shared.model.person.user.admin.Admin;
import epusp.pcs.os.shared.model.person.user.monitor.Monitor;
import epusp.pcs.os.shared.model.person.user.superuser.SuperUser;

public class LoginConnection extends Connection implements ILoginService{

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(LoginConnection.class.getCanonicalName());
	
	@Override
	public void init() throws ServletException{}

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
				User user = null, detached = null;
				for(AccountTypes accountType : AccountTypes.values()){
					Class<?> targetClass = accountType.getTargetClass();
					if(targetClass != null){
						PersistenceManager mgr = PMF.get().getPersistenceManager();
						try {
							user = (User) mgr.getObjectById(targetClass, userEmail);
							if(user != null){
								detached = mgr.detachCopy(user);
								break;
							}
						}catch (Exception e){
						} finally {
							mgr.close();
						}
					}
				}

				if(detached != null && detached.isActive()){
					
					if(!userPicture.equals(user.getPictureURL())){
						detached.setPictureURL(userPicture);
						
						Admin admin = null;
						Monitor monitor = null;
						Auditor auditor = null;
						SuperUser superUser = null;
						switch (detached.getType()) {
						case Admin:
							admin = (Admin) detached;
							break;
						case Auditor:
							auditor = (Auditor) detached;
							break;
						case Monitor:
							monitor = (Monitor) detached;
							break;
						case SuperUser:
							superUser = (SuperUser) detached;
							break;
						default:
							System.out.println("Denied access to " + detached.getEmail());
							return null;
						}
						
						PersistenceManager pm = PMF.get().getPersistenceManager();
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

					switch (detached.getPreferedLanguage()) {
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
					
					String key = super.authenticationManager.login(detached);
					config.setKey(key);

					switch(detached.getType()){
					case Admin:
						System.out.println("Admin " + detached.getEmail() + " has logged in.");
						config.setUrlPath("AdminWorkspace.html");
						return config;
					case Auditor:
						System.out.println("Aditor " + detached.getEmail() + " has logged in.");
						config.setUrlPath("todo");
						return config;
					case Monitor:
						System.out.println("Monitor " + detached.getEmail() + " has logged in.");
						config.setUrlPath("MonitorWorkspace.html");
						return config;
					case SuperUser:
						System.out.println("Super user " + user.getEmail() + " has logged in.");
						config.setUrlPath("SuperUserWorkspace.html");
						return config;
					default:
						System.out.println("Denied access to " + detached.getEmail());
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
