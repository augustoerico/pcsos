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

@Api(name = "personendpoint", namespace = @ApiNamespace(ownerDomain = "pcs.epusp", ownerName = "pcs.epusp", packagePath = "os.model.person"))
public class PersonEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listPerson")
	public CollectionResponse<Person> listPerson(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<Person> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(Person.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<Person>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (Person obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Person> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getPerson")
	public Person getPerson(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		Person person = null;
		try {
			person = mgr.getObjectById(Person.class, id);
		} finally {
			mgr.close();
		}
		return person;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param person the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertPerson")
	public Person insertPerson(Person person) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (containsPerson(person)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.makePersistent(person);
		} finally {
			mgr.close();
		}
		return person;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param person the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updatePerson")
	public Person updatePerson(Person person) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsPerson(person)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(person);
		} finally {
			mgr.close();
		}
		return person;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removePerson")
	public void removePerson(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			Person person = mgr.getObjectById(Person.class, id);
			mgr.deletePersistent(person);
		} finally {
			mgr.close();
		}
	}

	private boolean containsPerson(Person person) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(Person.class, person.getId());
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
