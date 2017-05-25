package com.epam.esm.task1.repository.impl;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.core.GenericTypeResolver;

import com.epam.esm.task1.dto.pagination.Page;
import com.epam.esm.task1.dto.pagination.PageInfo;
import com.epam.esm.task1.entity.AbstractEntity;
import com.epam.esm.task1.repository.CrudPaginatableRepository;
import com.epam.esm.task1.repository.CrudRepository;

public abstract class AbstractPaginatableCrudRepository<T extends AbstractEntity<K>, K extends Serializable>
		extends AbstractCrudRepository<T, K> implements CrudPaginatableRepository<T, K> {

	private static final String ID = "id";

	@PersistenceContext
	protected EntityManager entityManager;

	private Class<T> entityClass;

	@SuppressWarnings("unchecked")
	public AbstractPaginatableCrudRepository() {
		this.entityClass = (Class<T>) GenericTypeResolver.resolveTypeArguments(getClass(), CrudRepository.class)[0];
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<T> findAll(PageInfo pageInfo) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
		cq.select(criteriaBuilder.count(cq.from(entityClass)));
		Long entityCount = entityManager.createQuery(cq).getSingleResult();

		CriteriaQuery<T> criteria = criteriaBuilder.createQuery(entityClass);
		Root<T> root = criteria.from(entityClass);
		criteria.select(root);
		if (getDefaultOrder(criteriaBuilder, root) != null) {
			criteria.orderBy(getDefaultOrder(criteriaBuilder, root), criteriaBuilder.desc(root.get(ID)));
		}
		Query query = entityManager.createQuery(criteria).setFirstResult(pageInfo.getOffset())
				.setMaxResults(pageInfo.getLimit());
		List<T> entities = query.getResultList();

		Page<T> pageDTO = new Page<>(entityCount.intValue(), pageInfo.getOffset(), pageInfo.getLimit(), entities);
		return pageDTO;
	}
}
