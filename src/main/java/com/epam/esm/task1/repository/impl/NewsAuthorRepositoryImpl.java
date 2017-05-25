package com.epam.esm.task1.repository.impl;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.epam.esm.task1.entity.NewsAuthor;
import com.epam.esm.task1.repository.NewsAuthorRepository;

@Repository
public class NewsAuthorRepositoryImpl extends AbstractCrudRepository<NewsAuthor, Long> implements NewsAuthorRepository {

	private static final String NAMED_QUERY_FIND_BY_NAME = "NewsAuthor.findByName";
	private static final String AUTHOR_NAME = "name";
	private static final String NEWS_COUNT = "newsCount";

	@Override
	protected Order getDefaultOrder(CriteriaBuilder criteriaBuilder, Root<NewsAuthor> root) {
		return criteriaBuilder.desc(root.get(NEWS_COUNT));
	}

	@Override
	public NewsAuthor findByName(String name) {
		TypedQuery<NewsAuthor> query = entityManager.createNamedQuery(NAMED_QUERY_FIND_BY_NAME, NewsAuthor.class);
		query.setParameter(AUTHOR_NAME, name);
		List<NewsAuthor> author = query.getResultList();
		if (author.isEmpty()) {
			return null;
		} else {
			return author.get(0);
		}
	}

}