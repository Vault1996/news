package com.epam.esm.task1.service;

import java.io.Serializable;

import com.epam.esm.task1.dto.AbstractDTO;
import com.epam.esm.task1.dto.pagination.Page;
import com.epam.esm.task1.dto.pagination.PageInfo;

/**
 * Interface to perform findAll operation that supports Pagination
 * 
 * @author Algis_Ivashkiavichus
 *
 * @param <S>
 *            DTO type on which perform operations
 * @param <K>
 *            id type of the DTO
 */
public interface CrudPaginatableService<T extends AbstractDTO<K>, K extends Serializable> extends CrudService<T, K> {

	/**
	 * Method to find slice of entities according to PageInfo parameter
	 * 
	 * @param pageInfo
	 *            info to perform pagination
	 * @return Page object, containing found objects and page info.
	 */
	Page<T> findAll(PageInfo pageInfo);

}