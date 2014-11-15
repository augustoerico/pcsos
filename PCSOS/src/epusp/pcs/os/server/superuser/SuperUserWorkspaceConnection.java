package epusp.pcs.os.server.superuser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

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

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Admin> getAdmins(MoveCursor move, int range){
		if(isLoggedIn()){
			Integer cursorPosition = (Integer) getSessionAttibute(adminCursorPositionSessionAttribute);
			List<Cursor> cursors = (List<Cursor>) getSessionAttibute(adminCursorsListSessionAttribute);

			PersistenceManager pm = PMF.get().getPersistenceManager();

			Query q = pm.newQuery(Admin.class);
			q.setRange(0, range);

			//Initializing
			if(cursorPosition == null && cursors == null){
				cursorPosition = -1;
				cursors = new ArrayList<Cursor>();
			}

			switch(move){
			case BACKWARD:
				if(cursorPosition > 0){
					cursorPosition--;
				}
				break;
			case FORWARD:
				cursorPosition++;
				break;
			case FIRST:
				cursorPosition = -1;
				break;
			default:
				break;
			}

			if(!move.equals(MoveCursor.FIRST)){
				Cursor cursor = cursors.get(cursorPosition);
				Map<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				q.setExtensions(extensionMap);
			}

			List<Admin> admins = null;
			Collection<Admin> detachedAdmins = null;

			try {
				admins = (List<Admin>) q.execute();
				if(admins != null)
					detachedAdmins = pm.detachCopyAll(admins);
			} finally {
				q.closeAll();
			}

			if((detachedAdmins != null && !detachedAdmins.isEmpty() && move.equals(MoveCursor.FORWARD) && cursorPosition == cursors.size()-1) 
					|| (cursors.isEmpty() && cursorPosition == -1)){
				Cursor cursor = JDOCursorHelper.getCursor(admins);
				cursors.add(cursor);
			}

			setSessionAttribute(adminCursorPositionSessionAttribute, cursorPosition);
			setSessionAttribute(adminCursorsListSessionAttribute, cursors);

			if(detachedAdmins == null)
				return new ArrayList<Admin>();

			return detachedAdmins;
		}
		return null;
	}
	
	private static final String monitorCursorPositionSessionAttribute = "monitorCursorPositionSessionAttribute";
	private static final String monitorCursorsListSessionAttribute = "monitorCursorsListSessionAttribute";

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Monitor> getMonitors(MoveCursor move, int range){
		if(isLoggedIn()){
			Integer cursorPosition = (Integer) getSessionAttibute(monitorCursorPositionSessionAttribute);
			List<Cursor> cursors = (List<Cursor>) getSessionAttibute(monitorCursorsListSessionAttribute);

			PersistenceManager pm = PMF.get().getPersistenceManager();

			Query q = pm.newQuery(Monitor.class);
			q.setRange(0, range);

			//Initializing
			if(cursorPosition == null && cursors == null){
				cursorPosition = -1;
				cursors = new ArrayList<Cursor>();
			}

			switch(move){
			case BACKWARD:
				if(cursorPosition > 0){
					cursorPosition--;
				}
				break;
			case FORWARD:
				cursorPosition++;
				break;
			case FIRST:
				cursorPosition = -1;
				break;
			default:
				break;
			}

			if(!move.equals(MoveCursor.FIRST)){
				Cursor cursor = cursors.get(cursorPosition);
				Map<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				q.setExtensions(extensionMap);
			}

			List<Monitor> monitors = null;
			Collection<Monitor> detachedMonitors = null;

			try {
				monitors = (List<Monitor>) q.execute();
				if(monitors != null)
					detachedMonitors = pm.detachCopyAll(monitors);
			} finally {
				q.closeAll();
			}

			if((detachedMonitors != null && !detachedMonitors.isEmpty() && move.equals(MoveCursor.FORWARD) && cursorPosition == cursors.size()-1) 
					|| (cursors.isEmpty() && cursorPosition == -1)){
				Cursor cursor = JDOCursorHelper.getCursor(monitors);
				cursors.add(cursor);
			}

			setSessionAttribute(monitorCursorPositionSessionAttribute, cursorPosition);
			setSessionAttribute(monitorCursorsListSessionAttribute, cursors);

			if(detachedMonitors == null)
				return new ArrayList<Monitor>();

			return detachedMonitors;
		}
		return null;
	}
	
	private static final String superUserCursorPositionSessionAttribute = "superUserCursorPositionSessionAttribute";
	private static final String superUserCursorsListSessionAttribute = "superUserCursorsListSessionAttribute";

	@SuppressWarnings("unchecked")
	@Override
	public Collection<SuperUser> getSuperUsers(MoveCursor move, int range){
		if(isLoggedIn()){
			Integer cursorPosition = (Integer) getSessionAttibute(superUserCursorPositionSessionAttribute);
			List<Cursor> cursors = (List<Cursor>) getSessionAttibute(superUserCursorsListSessionAttribute);

			PersistenceManager pm = PMF.get().getPersistenceManager();

			Query q = pm.newQuery(SuperUser.class);
			q.setRange(0, range);

			//Initializing
			if(cursorPosition == null && cursors == null){
				cursorPosition = -1;
				cursors = new ArrayList<Cursor>();
			}

			switch(move){
			case BACKWARD:
				if(cursorPosition > 0){
					cursorPosition--;
				}
				break;
			case FORWARD:
				cursorPosition++;
				break;
			case FIRST:
				cursorPosition = -1;
				break;
			default:
				break;
			}

			if(!move.equals(MoveCursor.FIRST)){
				Cursor cursor = cursors.get(cursorPosition);
				Map<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				q.setExtensions(extensionMap);
			}

			List<SuperUser> superUsers = null;
			Collection<SuperUser> detachedSuperUsers = null;

			try {
				superUsers = (List<SuperUser>) q.execute();
				if(superUsers != null)
					detachedSuperUsers = pm.detachCopyAll(superUsers);
			} finally {
				q.closeAll();
			}

			if((detachedSuperUsers != null && !detachedSuperUsers.isEmpty() && move.equals(MoveCursor.FORWARD) && cursorPosition == cursors.size()-1) 
					|| (cursors.isEmpty() && cursorPosition == -1)){
				Cursor cursor = JDOCursorHelper.getCursor(superUsers);
				cursors.add(cursor);
			}

			setSessionAttribute(superUserCursorPositionSessionAttribute, cursorPosition);
			setSessionAttribute(superUserCursorsListSessionAttribute, cursors);

			if(detachedSuperUsers == null)
				return new ArrayList<SuperUser>();

			return detachedSuperUsers;
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
