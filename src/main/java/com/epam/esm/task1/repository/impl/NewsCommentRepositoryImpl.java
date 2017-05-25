package com.epam.esm.task1.repository.impl;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.epam.esm.task1.dto.pagination.Page;
import com.epam.esm.task1.dto.pagination.PageInfo;
import com.epam.esm.task1.entity.NewsComment;
import com.epam.esm.task1.repository.NewsCommentRepository;
import com.epam.esm.task1.repository.exception.EntityNotFoundException;

@Repository
public class NewsCommentRepositoryImpl extends AbstractPaginatableCrudRepository<NewsComment, Long>
		implements NewsCommentRepository {

	private static final String CREATION_TIME = "creationTime";
	private static final String ID = "id";
	private static final String NEWS = "news";

	@Override
	protected Order getDefaultOrder(CriteriaBuilder criteriaBuilder, Root<NewsComment> root) {
		return criteriaBuilder.desc(root.get(CREATION_TIME));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<NewsComment> findByNewsId(PageInfo pageInfo, Long newsId) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
		Root<NewsComment> commentsRoot = countQuery.from(NewsComment.class);
		countQuery.select(criteriaBuilder.count(commentsRoot));
		countQuery.where(criteriaBuilder.equal(commentsRoot.get(NEWS).get(ID), newsId));
		Long entityCount = entityManager.createQuery(countQuery).getSingleResult();

		CriteriaQuery<NewsComment> criteria = criteriaBuilder.createQuery(NewsComment.class);
		Root<NewsComment> root = criteria.from(NewsComment.class);
		criteria.select(root);
		criteria.where(criteriaBuilder.equal(root.get(NEWS).get(ID), newsId));
		criteria.orderBy(criteriaBuilder.desc(root.get(CREATION_TIME)), criteriaBuilder.desc(root.get(ID)));
		Query query = entityManager.createQuery(criteria).setFirstResult(pageInfo.getOffset())
				.setMaxResults(pageInfo.getLimit());
		List<NewsComment> entities = query.getResultList();

		Page<NewsComment> page = new Page<>(entityCount.intValue(), pageInfo.getOffset(), pageInfo.getLimit(),
				entities);
		return page;
	}

	@Override
	public NewsComment update(NewsComment entity) {
		Assert.notNull(entity, "News should not be null.");
		NewsComment newsComment = entityManager.find(NewsComment.class, entity.getId());
		if (newsComment == null) {
			throw new EntityNotFoundException("No comment with such id to update");
		}
		newsComment.setText(entity.getText());
		newsComment.getNews().getId();
		return newsComment;
	}

}
