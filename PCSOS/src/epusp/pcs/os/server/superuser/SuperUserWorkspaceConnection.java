package epusp.pcs.os.server.superuser;

import java.util.Collection;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;

import epusp.pcs.os.server.Connection;
import epusp.pcs.os.server.PMF;
import epusp.pcs.os.shared.general.MoveCursor;
import epusp.pcs.os.shared.model.person.user.AccountTypes;
import epusp.pcs.os.shared.model.person.user.Admin;
import epusp.pcs.os.shared.model.person.user.Monitor;
import epusp.pcs.os.shared.model.person.user.SuperUser;
import epusp.pcs.os.shared.model.person.user.User;
import epusp.pcs.os.superuser.client.rpc.ISuperUserWorkspaceService;

public class SuperUserWorkspaceConnection extends Connection implements ISuperUserWorkspaceService{

	private static final long serialVersionUID = 1L;
	
	@Override
	public void init() throws ServletException{}

	@Override
	protected Boolean isLoggedIn() {
		if(super.isLoggedIn()){
			User user = (User) getSessionAttibute(userSessionAttribute);
			if(user.getType().equals(AccountTypes.SuperUser)){
				return true;
			}
		}
		return false;
	}
	

	private static final String adminCursorPositionSessionAttribute = "adminCursorPositionSessionAttribute";
	private static final String adminCursorsListSessionAttribute = "adminCursorsListSessionAttribute";

	@Override
	public Collection<Admin> getAdmins(MoveCursor move, int range){
		if(isLoggedIn()){
			return getData(Admin.class, move, range, adminCursorPositionSessionAttribute, adminCursorsListSessionAttribute);
		}
		return null;
	}
	
	private static final String monitorCursorPositionSessionAttribute = "monitorCursorPositionSessionAttribute";
	private static final String monitorCursorsListSessionAttribute = "monitorCursorsListSessionAttribute";

	@Override
	public Collection<Monitor> getMonitors(MoveCursor move, int range){
		if(isLoggedIn()){
			return getData(Monitor.class, move, range, monitorCursorPositionSessionAttribute, monitorCursorsListSessionAttribute);
		}
		return null;
	}
	
	private static final String superUserCursorPositionSessionAttribute = "superUserCursorPositionSessionAttribute";
	private static final String superUserCursorsListSessionAttribute = "superUserCursorsListSessionAttribute";

	@Override
	public Collection<SuperUser> getSuperUsers(MoveCursor move, int range){
		if(isLoggedIn()){
			return getData(SuperUser.class, move, range, superUserCursorPositionSessionAttribute, superUserCursorsListSessionAttribute);
		}
		return null;
	}
	
	@Override
	public void createMonitor(Monitor monitor){
		if(isLoggedIn()){
			PersistenceManager pm = PMF.get().getPersistenceManager();

			System.out.println("Creating/updating Monitor " + monitor.getEmail());

			try{
				pm.currentTransaction().begin();
				pm.makePersistent(monitor);
				pm.currentTransaction().commit();
			}catch (Exception e){
				e.printStackTrace();
				if(pm.currentTransaction().isActive())
					pm.currentTransaction().rollback();
			}finally{
				pm.close();
			}
		}
	}
	
	@Override
	public void createAdmin(Admin admin){
		if(isLoggedIn()){
			PersistenceManager pm = PMF.get().getPersistenceManager();

			System.out.println("Creating/updating Admin " + admin.getEmail());

			try{
				pm.currentTransaction().begin();
				pm.makePersistent(admin);
				pm.currentTransaction().commit();
			}catch (Exception e){
				e.printStackTrace();
				if(pm.currentTransaction().isActive())
					pm.currentTransaction().rollback();
			}finally{
				pm.close();
			}
		}
	}
	
	@Override
	public void createSuperUser(SuperUser superUser){
		if(isLoggedIn()){
			PersistenceManager pm = PMF.get().getPersistenceManager();

			System.out.println("Creating/updating Admin " + superUser.getEmail());

			try{
				pm.currentTransaction().begin();
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
	}
}
