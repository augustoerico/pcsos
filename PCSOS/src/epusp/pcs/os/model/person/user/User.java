package epusp.pcs.os.model.person.user;

import com.google.gwt.user.client.rpc.IsSerializable;

import epusp.pcs.os.model.person.Person;

public abstract class User extends Person implements IsSerializable{
	
	private String login;
	private String senha;
	
	public User(String name, String surname){
		super(name, surname);
	}
	
	public User(String name, String secondName, String surname){
		super(name, secondName, surname);
	}
	
	public String getLogin(){
		return login;
	}
	
	public void setLogin(String login){
		this.login = login;
	}
	
	public String getSenha() {
		return senha;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public abstract AccountTypes getType();
	
	/*
	 * Seen by isSerializable
	 */
	public User(){
		super();
	}



}
