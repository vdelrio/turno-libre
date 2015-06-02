package com.turnolibre.service;

import java.util.List;

/**
 * Servicio para operaciones básicas contra la base de datos.
 *
 * @author Victor Del Rio
 */
public interface SharedService {

	/**
	 * Persist the given transient instance, first assigning a generated identifier. (Or
	 * using the current value of the identifier property if the <tt>assigned</tt>
	 * generator is used.) This operation cascades to associated instances if the
	 * association is mapped with {@code cascade="save-update"}
	 *
	 * @param entity a transient instance of a persistent class
	 * @return the generated identifier
	 * @see org.hibernate.Session#save(Object)
	 */
	<T> Long save(T entity);

	/**
	 * Update the persistent instance with the identifier of the given detached
	 * instance. If there is a persistent instance with the same identifier,
	 * an exception is thrown. This operation cascades to associated instances
	 * if the association is mapped with {@code cascade="save-update"}
	 *
	 * @param entity a detached instance containing updated state
	 */
	<T> void update(T entity);

	/**
	 * Either {@link #save(Object)} or {@link #update(Object)} the given
	 * instance, depending upon resolution of the unsaved-value checks (see the
	 * manual for discussion of unsaved-value checking).
	 * <p/>
	 * This operation cascades to associated instances if the association is mapped
	 * with {@code cascade="save-update"}
	 *
	 * @param entity a transient or detached instance containing new or updated state
	 *
	 * @see org.hibernate.Session#save(java.lang.Object)
	 * @see org.hibernate.Session#update(Object object)
	 */
	<T> void saveOrUpdate(T entity);

	/**
	 * Remove a persistent instance from the datastore. The argument may be
	 * an instance associated with the receiving <tt>Session</tt> or a transient
	 * instance with an identifier associated with existing persistent state.
	 * This operation cascades to associated instances if the association is mapped
	 * with {@code cascade="delete"}
	 *
	 * @param entity the instance to be removed
	 * @see org.hibernate.Session#delete(Object)
	 */
	<T> void delete(T entity);

	/**
	 * Return the persistent instance of the given entity class with the given identifier,
	 * or null if there is no such persistent instance. (If the instance is already associated
	 * with the session, return that instance. This method never returns an uninitialized instance.)
	 *
	 * @param entityClass a persistent class
	 * @param id an identifier
	 * @return a persistent instance or null
	 * @see org.hibernate.Session#get(Class, java.io.Serializable)
	 */
	<T> T get(Class<T> entityClass, Long id);

	/**
	 * Return the persistent instance of the given entity class with the given identifier,
	 * assuming that the instance exists. This method might return a proxied instance that
	 * is initialized on-demand, when a non-identifier method is accessed.
	 * <br><br>
	 * You should not use this method to determine if an instance exists (use <tt>get()</tt>
	 * instead). Use this only to retrieve an instance that you assume exists, where non-existence
	 * would be an actual error.
	 *
	 * @param entityClass a persistent class
	 * @param id a valid identifier of an existing persistent instance of the class
	 * @return the persistent instance or proxy
	 * @see org.hibernate.Session#load(Class, java.io.Serializable)
	 */
	<T> T load(Class<T> entityClass, Long id);

	/**
	 * Find all persistence instances of the given entity class.
	 *
	 * @return all persistence instances of the given entity class
	 */
	<T> List<T> findAll(Class<T> entityClass);

}
