package com.epam.esm.task1.repository.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import org.springframework.core.GenericTypeResolver;
import org.springframework.util.Assert;

import com.epam.esm.task1.entity.AbstractEntity;
import com.epam.esm.task1.repository.CrudRepository;

public abstract class AbstractCrudRepository<T extends AbstractEntity<K>, K extends Serializable>
		implements CrudRepository<T, K> {

	private static final String ID = "id";

	@PersistenceContext
	protected EntityManager entityManager;

	private Class<T> entityClass;

	@SuppressWarnings("unchecked")
	public AbstractCrudRepository() {
		this.entityClass = (Class<T>) GenericTypeResolver.resolveTypeArguments(getClass(), CrudRepository.class)[0];
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAll() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> criteria = criteriaBuilder.createQuery(entityClass);
		Root<T> root = criteria.from(entityClass);
		criteria.select(root);
		if (getDefaultOrder(criteriaBuilder, root) != null) {
			criteria.orderBy(getDefaultOrder(criteriaBuilder, root), criteriaBuilder.desc(root.get(ID)));
		}
		Query query = entityManager.createQuery(criteria);
		return query.getResultList();
	}

	@Override
	public Optional<T> findById(K id) {
		Assert.notNull(id, "Id should not be null.");
		return Optional.ofNullable(entityManager.find(entityClass, id));
	}

	@Override
	public T save(T entity) {
		Assert.notNull(entity, "Entity should not be null.");
		Assert.isNull(entity.getId(), "Id should be null");
		entityManager.persist(entity);
		return entity;
	}

	@Override
	public void delete(K id) {
		Assert.notNull(id, "Id should not be null.");
		T entity = entityManager.find(entityClass, id);
		if (entity != null) {
			entityManager.remove(entity);
		}
	}

	@Override
	public T update(T entity) {
		Assert.notNull(entity, "Entity should not be null.");
		T checkedEntity = entityManager.find(entityClass, entity.getId());
		if (checkedEntity == null) {
			throw new EntityNotFoundException("No entity found with such id to update");
		}
		return entityManager.merge(entity);
	}

	protected abstract Order getDefaultOrder(CriteriaBuilder criteriaBuilder, Root<T> root);

}
