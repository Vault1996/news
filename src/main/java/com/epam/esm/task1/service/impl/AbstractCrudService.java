package com.epam.esm.task1.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.epam.esm.task1.converter.Converter;
import com.epam.esm.task1.dto.AbstractDTO;
import com.epam.esm.task1.entity.AbstractEntity;
import com.epam.esm.task1.repository.CrudRepository;
import com.epam.esm.task1.repository.exception.EntityNotFoundException;
import com.epam.esm.task1.service.CrudService;

public abstract class AbstractCrudService<S extends AbstractDTO<K>, K extends Serializable, T extends AbstractEntity<K>>
		implements CrudService<S, K> {

	@Autowired
	protected CrudRepository<T, K> crudRepository;

	@Autowired
	protected Converter<T, S> converter;

	@Override
	@Transactional(readOnly = true)
	public List<S> findAll() {
		List<T> entities = crudRepository.findAll();
		if (!entities.isEmpty()) {
			return converter.convertToDTO(entities);
		} else {
			throw new EntityNotFoundException("No entities in the system");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public S findById(K id) {
		Assert.notNull(id, "Key should not be null");
		T entity = crudRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No entity found"));
		return converter.convertToDTO(entity);
	}

	@Override
	@Transactional
	public S save(S dto) {
		Assert.notNull(dto, "Entity should not be null");
		dto.setId(null);
		T entity = converter.convertToEntity(dto);
		return converter.convertToDTO(crudRepository.save(entity));
	}

	@Override
	@Transactional
	public void delete(K id) {
		Assert.notNull(id, "Key should not be null.");
		crudRepository.delete(id);
	}

	@Override
	@Transactional
	public S update(S dto) {
		Assert.notNull(dto, "Entity should not be null.");
		T entity = converter.convertToEntity(dto);
		return converter.convertToDTO(crudRepository.update(entity));
	}
}
