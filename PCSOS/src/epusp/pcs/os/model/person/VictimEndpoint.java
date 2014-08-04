package epusp.pcs.os.model.person;

import epusp.pcs.os.model.person.PMF;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

@Api(name = "victimendpoint", namespace = @ApiNamespace(ownerDomain = "pcs.epusp", ownerName = "pcs.epusp", packagePath = "os.model.person"))
public class VictimEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listVictim")
	public CollectionResponse<Victim> listVictim(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<Victim> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(Victim.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<Victim>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (Victim obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Victim> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getVictim")
	public Victim getVictim(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		Victim victim = null;
		try {
			victim = mgr.getObjectById(Victim.class, id);
		} finally {
			mgr.close();
		}
		return victim;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param victim the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertVictim")
	public Victim insertVictim(Victim victim) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (containsVictim(victim)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.makePersistent(victim);
		} finally {
			mgr.close();
		}
		return victim;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param victim the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateVictim")
	public Victim updateVictim(Victim victim) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsVictim(victim)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(victim);
		} finally {
			mgr.close();
		}
		return victim;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeVictim")
	public void removeVictim(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			Victim victim = mgr.getObjectById(Victim.class, id);
			mgr.deletePersistent(victim);
		} finally {
			mgr.close();
		}
	}

	private boolean containsVictim(Victim victim) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(Victim.class, victim.getId());
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
