package com.epam.esm.task1.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.task1.dto.AbstractDTO;
import com.epam.esm.task1.dto.pagination.Page;
import com.epam.esm.task1.dto.pagination.PageInfo;
import com.epam.esm.task1.entity.AbstractEntity;
import com.epam.esm.task1.exception.ValidationException;
import com.epam.esm.task1.repository.CrudPaginatableRepository;
import com.epam.esm.task1.service.CrudPaginatableService;

public abstract class AbstractPaginatableCrudService<S extends AbstractDTO<K>, K extends Serializable, T extends AbstractEntity<K>>
		extends AbstractCrudService<S, K, T> implements CrudPaginatableService<S, K> {

	protected static final int LIMIT_TO_PAGINATION = 1000;

	@Autowired
	protected CrudPaginatableRepository<T, K> paginatableRepository;

	@Override
	@Transactional(readOnly = true)
	public Page<S> findAll(PageInfo pageInfo) {
		if (pageInfo.getLimit() > LIMIT_TO_PAGINATION) {
			throw new ValidationException("Greatest number of entities to show is " + LIMIT_TO_PAGINATION);
		}
		Page<T> pages = paginatableRepository.findAll(pageInfo);
		Page<S> pageDTO = converter.convertToDTO(pages);

		return pageDTO;
	}

}
