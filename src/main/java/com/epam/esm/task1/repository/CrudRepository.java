package com.epam.esm.task1.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import com.epam.esm.task1.entity.AbstractEntity;

/**
 * Interface to perform CRUD operations to entities
 * 
 * @author Algis_Ivashkiavichus
 *
 * @param <T>
 *            entity type on which perform operations
 * @param <K>
 *            id type of the entity
 */
public interface CrudRepository<T extends AbstractEntity<K>, K extends Serializable> {

	/**
	 * Method to find all entities in the system
	 * 
	 * @return list of found entities
	 */
	List<T> findAll();

	/**
	 * Method to find entity by its id
	 * 
	 * @param id
	 *            id by which to find
	 * @return Optional with entity if entity exists and empty Optional
	 *         otherwise
	 */
	Optional<T> findById(K id);

	/**
	 * Method to save entity in the system by its id
	 * 
	 * @param entity
	 *            entity to save
	 * @return saved entity
	 */
	T save(T entity);

	/**
	 * Method to delete entity from system by its id
	 * 
	 * @param id
	 *            key to perform delete operation
	 * 
	 */
	void delete(K id);

	/**
	 * Method to update entity in the system
	 * 
	 * @param entity
	 *            entity to update
	 * @return updated entity
	 */
	T update(T entity);

}
