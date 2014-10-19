package epusp.pcs.os.server.endpoint;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

import epusp.pcs.os.server.PMF;
import epusp.pcs.os.shared.model.person.user.Agent;

@Api(name = "agentendpoint", namespace = @ApiNamespace(ownerDomain = "pcs.epusp", ownerName = "pcs.epusp", packagePath = "os.model.person.user"))
public class AgentEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listAgent")
	public CollectionResponse<Agent> listAgent(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<Agent> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(Agent.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<Agent>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (Agent obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Agent> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getAgent")
	public Agent getAgent(@Named("email") String email) {
		PersistenceManager mgr = getPersistenceManager();
		Agent agent = null, detached = null;
		try {
			Key k = KeyFactory.createKey(Agent.class.getSimpleName(), email);
			agent = mgr.getObjectById(Agent.class, k);
			detached = mgr.detachCopy(agent);
		} finally {
			mgr.close();
		}
		return detached;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param agent the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertAgent")
	public Agent insertAgent(Agent agent) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (containsAgent(agent)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.makePersistent(agent);
		} finally {
			mgr.close();
		}
		return agent;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param agent the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateAgent")
	public Agent updateAgent(Agent agent) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsAgent(agent)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(agent);
		} finally {
			mgr.close();
		}
		return agent;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeAgent")
	public void removeAgent(@Named("email") String email) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			Key k = KeyFactory.createKey(Agent.class.getSimpleName(), email);
			Agent agent = mgr.getObjectById(Agent.class, k);
			mgr.deletePersistent(agent);
		} finally {
			mgr.close();
		}
	}

	private boolean containsAgent(Agent agent) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			Key k = KeyFactory.createKey(Agent.class.getSimpleName(), agent.getEmail());
			mgr.getObjectById(Agent.class, k);
		} catch (javax.jdo.JDOObjectNotFoundException ex) {
			contains = false;
		} finally {
			mgr.close();
		}
		return contains;
	}

	private static PersistenceManager getPersistenceManager() {
		return PMF.get().getPersistenceManager();
	}

}
