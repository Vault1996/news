package com.epam.esm.task1.converter.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.epam.esm.task1.converter.Converter;
import com.epam.esm.task1.dto.AbstractDTO;
import com.epam.esm.task1.dto.pagination.Page;
import com.epam.esm.task1.entity.AbstractEntity;

public abstract class AbstractConverter<T extends AbstractEntity<? extends Serializable>, S extends AbstractDTO<? extends Serializable>>
		implements Converter<T, S> {

	@Override
	public List<T> convertToEntity(List<S> dtos) {
		return collect(dtos, this::convertToEntity, Collectors.toList());
	}

	@Override
	public Set<T> convertToEntity(Set<S> dtos) {
		return collect(dtos, this::convertToEntity, Collectors.toSet());
	}

	@Override
	public List<S> convertToDTO(List<T> entities) {
		return collect(entities, this::convertToDTO, Collectors.toList());
	}

	@Override
	public Set<S> convertToDTO(Set<T> entities) {
		return collect(entities, this::convertToDTO, Collectors.toSet());
	}

	@Override
	public Page<S> convertToDTO(Page<T> entityPage) {
		Page<S> dtoPage = new Page<>();
		dtoPage.setNumberOfPages(entityPage.getNumberOfPages());
		dtoPage.setPageNumber(entityPage.getPageNumber());
		dtoPage.setTotalCount(entityPage.getTotalCount());
		dtoPage.setEntities(convertToDTO(entityPage.getEntities()));
		return dtoPage;
	}

	@Override
	public Page<T> convertToEntity(Page<S> dtoPage) {
		Page<T> entityPage = new Page<>();
		entityPage.setNumberOfPages(dtoPage.getNumberOfPages());
		entityPage.setPageNumber(dtoPage.getPageNumber());
		entityPage.setTotalCount(dtoPage.getTotalCount());
		entityPage.setEntities(convertToEntity(dtoPage.getEntities()));
		return entityPage;
	}

	private <D, E, C extends Collection<E>, R extends Collection<D>> R collect(C objects, Function<E, D> function,
			Collector<D, ?, R> collector) {
		return objects != null ? objects.stream().map(function).collect(collector) : null;
	}
}
