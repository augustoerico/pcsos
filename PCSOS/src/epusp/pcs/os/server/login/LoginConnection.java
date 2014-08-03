package epusp.pcs.os.server.login;

import epusp.pcs.os.login.client.rpc.ILoginService;
import epusp.pcs.os.login.exceptions.LoginException;
import epusp.pcs.os.model.person.user.SuperUser;
import epusp.pcs.os.model.person.user.User;
import epusp.pcs.os.server.Connection;

public class LoginConnection extends Connection implements ILoginService{

	private static final long serialVersionUID = 1L;

	@Override
	public User login(String username, String password) throws LoginException{
		//To do: create a true login (including password validation)
		SuperUser superUser = new SuperUser("Giovanni", "Gatti Pinheiro");
		superUser.setIsActive(true);
		superUser.setLogin("giovanni.gatti@usp.br");
		superUser.setSenha("pcsos");
		return superUser;
	}
}
