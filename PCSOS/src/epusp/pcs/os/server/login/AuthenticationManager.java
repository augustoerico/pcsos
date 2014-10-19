package epusp.pcs.os.server.login;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import epusp.pcs.os.shared.model.person.user.User;

public enum AuthenticationManager {
	INSTANCE;

	private final ConcurrentHashMap<String, UserLogin> authenticatedUsers =
			new ConcurrentHashMap<String, UserLogin>();
	
	TokenGenerator generator = new TokenGenerator("pcsos-2#9$d=nru!94jc,+");

	public String login(User user){
		UserLogin userLogin = new UserLogin();
		userLogin.setUser(user);

		Date loginDate = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.SECOND, 10);
		Date expireDate = calendar.getTime();

		userLogin = new UserLogin(user, loginDate, expireDate);
		
		TokenOptions options = new TokenOptions();
		options.setExpires(expireDate);
		
		String key = generator.createToken(user);
		
		System.out.println("Storing user info @ " + key);
		
		if(!authenticatedUsers.containsValue(userLogin)){
			authenticatedUsers.put(key, userLogin);
			return key;
		}else{
			for(Map.Entry<String, UserLogin> entry : authenticatedUsers.entrySet()){
				if(entry.getValue().getUser().equals(user)){
					authenticatedUsers.remove(entry.getKey());
					authenticatedUsers.put(key, userLogin);
					return key;
				}
			}
		}
		clean();
		return null;
	}

	public Boolean isLoggedIn(String key){
		if(authenticatedUsers.containsKey(key)){
			UserLogin userLogin = authenticatedUsers.get(key);
			clean();
			if(userLogin.getExpireDate().after(new Date())){
				return true;
			}else{
				authenticatedUsers.remove(key);
			}
		}
		return false;
	}

	public User getUser(String key){
		User user = authenticatedUsers.get(key).getUser();
		authenticatedUsers.remove(key);
		clean();
		return user;
	}
	
	private void clean(){
		if(!authenticatedUsers.isEmpty()){
			Date currentDate = new Date();
			for(Map.Entry<String, UserLogin> entry : authenticatedUsers.entrySet()){
				if(currentDate.after(entry.getValue().getExpireDate())){
					authenticatedUsers.remove(entry.getKey());
				}
			}
		}
	}

	public static AuthenticationManager getInstance(){
		return INSTANCE;
	}

	AuthenticationManager(){
	}
}
