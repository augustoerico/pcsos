package epusp.pcs.os.server.login;

import java.io.Serializable;
import java.util.Date;

import epusp.pcs.os.shared.model.person.user.User;

public class UserLogin implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private User user;
	private Date loginDate;
	private Date expireDate;
	
	public UserLogin(User user, Date loginDate, Date expireDate){
		this.user = user;
		this.loginDate = loginDate;
		this.expireDate = expireDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	
	public UserLogin(){
		super();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof UserLogin){
			UserLogin userLogin = (UserLogin) obj;
			return userLogin.getUser().equals(user);
		}
		return false;
	}

}
