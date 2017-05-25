package com.epam.esm.task1.repository;

import java.io.Serializable;

import com.epam.esm.task1.dto.pagination.Page;
import com.epam.esm.task1.dto.pagination.PageInfo;
import com.epam.esm.task1.entity.AbstractEntity;

/**
 * Interface to perform findAll operation that supports pagination
 * 
 * @author Algis_Ivashkiavichus
 *
 * @param <T>
 *            entity type on which perform operations
 * @param <K>
 *            id type of the entity
 */
public interface CrudPaginatableRepository<T extends AbstractEntity<K>, K extends Serializable>
		extends CrudRepository<T, K> {

	/**
	 * Method to find slice of entities according to PageInfo parameter
	 * 
	 * @param pageInfo
	 *            info to perform pagination
	 * @return Page object, containing found objects and page info.
	 */
	Page<T> findAll(PageInfo pageInfo);

}