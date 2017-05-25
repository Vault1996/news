package com.epam.esm.task1.repository.impl;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.epam.esm.task1.entity.NewsTag;
import com.epam.esm.task1.repository.NewsTagRepository;

@Repository
public class NewsTagRepositoryImpl extends AbstractCrudRepository<NewsTag, Long> implements NewsTagRepository {

	private static final String NAMED_QUERY_FIND_BY_NAME = "NewsTag.findByName";
	private static final String NEWS_COUNT = "newsCount";
	private static final String TAG_NAME = "name";

	@Override
	protected Order getDefaultOrder(CriteriaBuilder criteriaBuilder, Root<NewsTag> root) {
		return criteriaBuilder.desc(root.get(NEWS_COUNT));
	}

	@Override
	public NewsTag findByName(String name) {
		TypedQuery<NewsTag> query = entityManager.createNamedQuery(NAMED_QUERY_FIND_BY_NAME, NewsTag.class);
		query.setParameter(TAG_NAME, name);
		List<NewsTag> tag = query.getResultList();
		if (tag.isEmpty()) {
			return null;
		} else {
			return tag.get(0);
		}
	}

}
