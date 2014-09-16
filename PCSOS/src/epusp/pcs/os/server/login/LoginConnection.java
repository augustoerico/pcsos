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
import epusp.pcs.os.model.person.user.Admin;
import epusp.pcs.os.model.person.user.User;
import epusp.pcs.os.server.Connection;
import epusp.pcs.os.server.PMF;

public class LoginConnection extends Connection implements ILoginService{

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(LoginConnection.class.getCanonicalName());

	public void init() throws ServletException
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();

		System.out.println("Initializing with test data");
		Admin admin = new Admin("Giovanni", "Gatti Pinheiro", "giovanni.gatti.pinheiro@gmail.com");
		admin.setIsActive(true);
		admin.setGoogleUserId("115057125698280242918");
		admin.setPictureURL("https://lh5.googleusercontent.com/--PBV1HBWVsc/AAAAAAAAAAI/AAAAAAAAAFQ/O57isBLRtRA/photo.jpg");

		try{
			pm.makePersistent(admin);
		}finally{
			pm.close();
		}
	}

	@Override
	public String login(String token){
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

			String userId = null;

			try {
				final JSONObject obj = new JSONObject(r.toString());
				userId = obj.getString("id");
			} catch (Exception e) {
				log.log(Level.SEVERE, e.getMessage());
			}

			PersistenceManager pm = PMF.get().getPersistenceManager();
			User user = null;
			try{
				user = pm.getObjectById(User.class, userId);
			}finally{
				pm.close();
			}

			getThreadLocalRequest().getSession().setAttribute(userInfo, user);

			switch(user.getType()){
			case Admin:
				return "";
			case Agent:
				return "";
			case Auditor:
				return "";
			case Monitor:
				return "";
			case SuperUser:
				return "";
			default:
				return null;
			}



		}else
			return null;
	}
}
