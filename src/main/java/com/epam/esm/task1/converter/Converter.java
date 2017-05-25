package com.epam.esm.task1.converter;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.epam.esm.task1.dto.AbstractDTO;
import com.epam.esm.task1.dto.pagination.Page;
import com.epam.esm.task1.entity.AbstractEntity;

/**
 * Class to convert one type of object to another and backwards.
 * 
 * @author Algis_Ivashkiavichus
 *
 * @param <T>
 *            First object/entity to convert
 * @param <S>
 *            Second object/entity to convert
 */
public interface Converter<T extends AbstractEntity<? extends Serializable>, S extends AbstractDTO<? extends Serializable>> {

	/**
	 * Converts DTO to entity
	 * 
	 * @param dto
	 *            DTO to convert
	 * @return converted DTO
	 */
	T convertToEntity(S dto);

	/**
	 * Converts List of DTO to List of entities
	 * 
	 * @param dtos
	 *            List of DTO to convert
	 * @return converted List
	 */
	List<T> convertToEntity(List<S> dtos);

	/**
	 * Converts Set of DTO to Set of entities
	 * 
	 * @param dtos
	 *            Set of DTO to convert
	 * @return converted Set
	 */
	Set<T> convertToEntity(Set<S> dtos);

	/**
	 * Converts entity to DTO
	 * 
	 * @param entity
	 *            entity to convert
	 * @return converted entity
	 */
	S convertToDTO(T entity);

	/**
	 * Converts List of entities to List of DTO
	 * 
	 * @param entities
	 *            List of entities to convert
	 * @return converted List
	 */
	List<S> convertToDTO(List<T> entities);

	/**
	 * Converts Set of entities to Set of DTO
	 * 
	 * @param entities
	 *            Set of entities to convert
	 * @return converted Set
	 */
	Set<S> convertToDTO(Set<T> entities);

	Page<S> convertToDTO(Page<T> entityPage);

	Page<T> convertToEntity(Page<S> dtoPage);

}