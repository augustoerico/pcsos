package epusp.pcs.os.server.admin;

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

import epusp.pcs.os.admin.client.rpc.IAdminWorkspaceService;
import epusp.pcs.os.server.Connection;
import epusp.pcs.os.server.PMF;
import epusp.pcs.os.shared.general.MoveCursor;
import epusp.pcs.os.shared.model.person.Victim;
import epusp.pcs.os.shared.model.person.user.AccountTypes;
import epusp.pcs.os.shared.model.person.user.Agent;
import epusp.pcs.os.shared.model.person.user.User;

public class AdminWorkspaceConnection extends Connection implements IAdminWorkspaceService{

	private static final long serialVersionUID = 1L;

	private static final String victimCursorSessionAttribute = "victimCursorSessionAttribute";

	private static final String vehicleCursorPositionSessionAttribute = "vehicleCursorPositionSessionAttribute";
	private static final String vehicleCursorsListSessionAttribute = "vehicleCursorsListSessionAttribute";


	private static final String agentCursorPositionSessionAttribute = "agentCursorPositionSessionAttribute";
	private static final String agentCursorsListSessionAttribute = "agentCursorsListSessionAttribute";

	@Override
	public void init() throws ServletException{}

	@Override
	protected Boolean isLoggedIn() {
		if(super.isLoggedIn()){
			User user = (User) getSessionAttibute(userSessionAttribute);
			if(user.getType().equals(AccountTypes.Admin)){
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Victim> getVictims(){
		if(isLoggedIn()){

			String cursorString = (String) getSessionAttibute(victimCursorSessionAttribute);

			PersistenceManager pm = PMF.get().getPersistenceManager();

			pm.getFetchPlan().addGroup("all_system_object_attributes");
			pm.getFetchPlan().setMaxFetchDepth(-1);

			Query q = pm.newQuery(Victim.class);
			q.setRange(0, 10);

			if(cursorString != null){
				Cursor cursor = Cursor.fromWebSafeString(cursorString);

				Map<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				q.setExtensions(extensionMap);
			}

			List<Victim> victims = null;
			Collection<Victim> detachedVictims = null;

			try {
				victims = (List<Victim>) q.execute();
				detachedVictims = pm.detachCopyAll(victims);
			} finally {
				q.closeAll();
			}

			Cursor cursor = JDOCursorHelper.getCursor(victims);

			setSessionAttribute(victimCursorSessionAttribute, cursor.toWebSafeString());

			if(detachedVictims == null)
				return new ArrayList<Victim>();

			return detachedVictims;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Agent> getAgents(MoveCursor move){
		if(isLoggedIn()){
			System.out.println("---------------");
			Integer cursorPosition = (Integer) getSessionAttibute(agentCursorPositionSessionAttribute);
			List<Cursor> cursors = (List<Cursor>) getSessionAttibute(agentCursorsListSessionAttribute);

			PersistenceManager pm = PMF.get().getPersistenceManager();

			pm.getFetchPlan().addGroup("all_system_object_attributes");
			pm.getFetchPlan().setMaxFetchDepth(-1);

			Query q = pm.newQuery(Agent.class);
			q.setRange(0, 2);

			//Initializing
			if(cursorPosition == null && cursors == null){
				cursorPosition = -1;
				cursors = new ArrayList<Cursor>();
			}
			
			System.out.println("start cursor position: " + cursorPosition);

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

			System.out.println(move);
			

			if(!move.equals(MoveCursor.FIRST)){
				Cursor cursor = cursors.get(cursorPosition);
				Map<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				q.setExtensions(extensionMap);
			}

			List<Agent> agents = null;
			Collection<Agent> detachedAgents = null;

			try {
				agents = (List<Agent>) q.execute();
				detachedAgents = pm.detachCopyAll(agents);
			} finally {
				q.closeAll();
			}

			System.out.println("if: " + ((detachedAgents != null && !detachedAgents.isEmpty() && move.equals(MoveCursor.FORWARD) && cursorPosition == cursors.size()-1) || (cursors.isEmpty() && cursorPosition == -1)));

			if((detachedAgents != null && !detachedAgents.isEmpty() && move.equals(MoveCursor.FORWARD) && cursorPosition == cursors.size()-1) 
					|| (cursors.isEmpty() && cursorPosition == -1)){
				Cursor cursor = JDOCursorHelper.getCursor(agents);
				cursors.add(cursor);
			}

			setSessionAttribute(agentCursorPositionSessionAttribute, cursorPosition);
			setSessionAttribute(agentCursorsListSessionAttribute, cursors);

			if(detachedAgents == null)
				return new ArrayList<Agent>();

			System.out.println("has agents: " + !detachedAgents.isEmpty());
			System.out.println("end cursor position: " + cursorPosition);
			System.out.println("---------------");
			return detachedAgents;
		}
		return null;
	}
}
