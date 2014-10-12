package epusp.pcs.os.server.login;

import java.util.Calendar;
import java.util.Date;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import epusp.pcs.os.model.person.user.User;

public enum AuthenticationManager {
	INSTANCE;

	private final ConcurrentHashMap<String, UserLogin> authenticatedUsers =
			new ConcurrentHashMap<String, UserLogin>();

	private final Timer timer = new Timer();

	public String login(User user){
		UserLogin userLogin = new UserLogin();
		userLogin.setUser(user);

		String email = user.getEmail();

		Date loginDate = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.HOUR_OF_DAY, +8);
		Date expireDate = calendar.getTime();

		userLogin = new UserLogin(user, loginDate, expireDate);

		String key = String.valueOf(loginDate.toString().concat(email).hashCode());
		
		System.out.println("Storing user info @ " + key);
		
		if(!authenticatedUsers.containsValue(userLogin)){
			authenticatedUsers.put(key, userLogin);
			return key;
		}else{
			for(Entry<String, UserLogin> entry : authenticatedUsers.entrySet()){
				if(entry.getValue().getUser().equals(user)){
					authenticatedUsers.remove(entry.getKey());
					authenticatedUsers.put(key, userLogin);
					return key;
				}
			}
		}
		return null;
	}

	public Boolean isLoggedIn(String key){
		if(authenticatedUsers.containsKey(key)){
			UserLogin userLogin = authenticatedUsers.get(key);
			if(userLogin.getExpireDate().after(new Date())){
				return true;
			}else{
				authenticatedUsers.remove(key);
			}
		}
		return false;
	}

	public User getUser(String key){
		return authenticatedUsers.get(key).getUser();
	}

	public UserLogin getUserLogin(String key){
		return authenticatedUsers.get(key);
	}

	public void logout(String key){
		if(authenticatedUsers.containsKey(key))
			authenticatedUsers.remove(key);
	}

	public static AuthenticationManager getInstance(){
		return INSTANCE;
	}

	AuthenticationManager(){
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				Date currentDate = new Date();
				for(Entry<String, UserLogin> entry : authenticatedUsers.entrySet()){
					if(currentDate.after(entry.getValue().getExpireDate())){
						authenticatedUsers.remove(entry.getKey());
					}
				}
			}
		}, 60*1000, 60*1000);
	}
}
