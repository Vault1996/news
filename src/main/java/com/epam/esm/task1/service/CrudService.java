package com.epam.esm.task1.service;

import java.io.Serializable;
import java.util.List;

import com.epam.esm.task1.dto.AbstractDTO;

/**
 * Interface to perform CRUD operations to DTOs.
 * 
 * @author Algis_Ivashkiavichus
 *
 * @param <S>
 *            DTO type on which perform operations
 * @param <K>
 *            id type of the DTO
 */
public interface CrudService<S extends AbstractDTO<K>, K extends Serializable> {

	/**
	 * Method to find all DTOs in the system
	 * 
	 * @return List of all DTOs
	 */
	List<S> findAll();

	/**
	 * Method to find DTO in the system by its id
	 * 
	 * @param id
	 *            id by which we find DTO
	 * @return found DTO
	 */
	S findById(K id);

	/**
	 * Method to save DTO in the system
	 * 
	 * @param dto
	 *            DTO to save
	 * @return saved DTO
	 */
	S save(S dto);

	/**
	 * Method to delete DTO from the system by its id
	 * 
	 * @param id
	 *            key to perform delete operation
	 */
	void delete(K id);

	/**
	 * Method to update DTO in the system
	 * 
	 * @param dto
	 *            DTO to edit
	 * @return edited DTO
	 */
	S update(S dto);

}