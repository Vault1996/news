package com.epam.esm.task1.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityGraph;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.jpa.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.epam.esm.task1.dto.pagination.Page;
import com.epam.esm.task1.dto.pagination.PageInfo;
import com.epam.esm.task1.entity.News;
import com.epam.esm.task1.entity.NewsComment;
import com.epam.esm.task1.repository.NewsRepository;
import com.epam.esm.task1.repository.exception.EntityNotFoundException;

@Repository
public class NewsRepositoryImpl extends AbstractPaginatableCrudRepository<News, Long> implements NewsRepository {

	private static final String SELECT_IDS_FROM_NEWS_ORDERED = "SELECT nn.id FROM News nn ORDER BY nn.creationTime DESC, nn.id DESC";
	private static final String COMMENTS = "comments";
	private static final String DESCRIPTION = "description";
	private static final String LAST_MODIFICATION_TIME = "lastModificationTime";
	private static final String CREATION_TIME = "creationTime";
	private static final String TITLE = "title";
	private static final String NEWS_TAGS_AND_AUTHORS_FETCHGRAPH = "newsTagsAndAuthors";
	private static final String ID = "id";

	@Override
	protected Order getDefaultOrder(CriteriaBuilder criteriaBuilder, Root<News> root) {
		return criteriaBuilder.desc(root.get(CREATION_TIME));
	}

	@Override
	public Page<News> findAll(PageInfo pageInfo) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
		cq.select(criteriaBuilder.count(cq.from(News.class)));
		Long entityCount = entityManager.createQuery(cq).getSingleResult();

		TypedQuery<Long> qy = entityManager.createQuery(SELECT_IDS_FROM_NEWS_ORDERED, Long.class);
		qy.setFirstResult(pageInfo.getOffset());
		qy.setMaxResults(pageInfo.getLimit());

		List<News> entities = findNewsByIds(criteriaBuilder, qy.getResultList());

		Page<News> pageDTO = new Page<>(entityCount.intValue(), pageInfo.getOffset(), pageInfo.getLimit(), entities);
		return pageDTO;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<News> findAll(PageInfo pageInfo, Map<String, List<Long>> searchMap) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
		Root<News> newsRoot = countQuery.from(News.class);
		countQuery.select(criteriaBuilder.count(newsRoot));
		countQuery.where(searchWherePredicate(searchMap, criteriaBuilder, newsRoot));
		Long entityCount = entityManager.createQuery(countQuery).getSingleResult();

		CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
		Root<News> root = criteria.from(News.class);
		criteria.select(root.get(ID));
		criteria.where(searchWherePredicate(searchMap, criteriaBuilder, root));
		criteria.orderBy(criteriaBuilder.desc(root.get(CREATION_TIME)), criteriaBuilder.desc(root.get(ID)));
		Query qy = entityManager.createQuery(criteria).setFirstResult(pageInfo.getOffset())
				.setMaxResults(pageInfo.getLimit());

		List<News> entities = findNewsByIds(criteriaBuilder, qy.getResultList());

		Page<News> pageDTO = new Page<>(entityCount.intValue(), pageInfo.getOffset(), pageInfo.getLimit(), entities);
		return pageDTO;
	}

	@Override
	public Optional<News> findById(Long id) {
		Assert.notNull(id, "Id should not be null.");

		EntityGraph<?> eg = entityManager.getEntityGraph(NEWS_TAGS_AND_AUTHORS_FETCHGRAPH);

		Map<String, Object> hints = new HashMap<>();
		hints.put(QueryHints.HINT_FETCHGRAPH, eg);

		return Optional.ofNullable(entityManager.find(News.class, id, hints));
	}

	@Override
	public News update(News news) {
		Assert.notNull(news, "News should not be null.");
		News actualNews = entityManager.find(News.class, news.getId());
		if (actualNews == null) {
			throw new EntityNotFoundException("No entity with such id to update");
		}
		actualNews.setDescription(news.getDescription());
		actualNews.setFullText(news.getFullText());
		actualNews.setLastModificationTime(news.getLastModificationTime());
		actualNews.setTitle(news.getTitle());
		actualNews.setAuthors(news.getAuthors());
		actualNews.setTags(news.getTags());
		return actualNews;
	}

	@Override
	public int countComments(Long newsId) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
		Root<News> newsRoot = countQuery.from(News.class);
		countQuery.select(criteriaBuilder.count(newsRoot));
		countQuery.where(criteriaBuilder.equal(newsRoot.get(ID), newsId));
		Long entityCount = entityManager.createQuery(countQuery).getSingleResult();
		return entityCount.intValue();
	}

	private Predicate searchWherePredicate(Map<String, List<Long>> searchMap, CriteriaBuilder criteriaBuilder,
			Root<News> root) {
		Predicate predicate = criteriaBuilder.and();
		if (searchMap != null && !searchMap.isEmpty()) {
			for (String key : searchMap.keySet()) {
				if (searchMap.get(key) != null && !searchMap.get(key).isEmpty()) {
					for (Long id : searchMap.get(key)) {
						predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.join(key).get(ID), id));
					}
				}
			}
		}
		return predicate;
	}

	@SuppressWarnings("unchecked")
	private List<News> findNewsByIds(CriteriaBuilder criteriaBuilder, List<Long> ids) {
		CriteriaQuery<News> criteria = criteriaBuilder.createQuery(News.class);
		Root<News> root = criteria.from(News.class);
		Join<News, NewsComment> join = root.join(COMMENTS, JoinType.LEFT);
		criteria.multiselect(root.get(ID), root.get(TITLE), root.get(CREATION_TIME), root.get(LAST_MODIFICATION_TIME),
				root.get(DESCRIPTION), criteriaBuilder.count(join.get(ID)));
		criteria.where(root.get(ID).in(ids));
		criteria.groupBy(root.get(ID));
		criteria.orderBy(criteriaBuilder.desc(root.get(CREATION_TIME)), criteriaBuilder.desc(root.get(ID)));
		Query query = entityManager.createQuery(criteria);
		return query.getResultList();
	}
}