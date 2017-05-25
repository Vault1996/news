package com.epam.esm.task1.repository;

import com.epam.esm.task1.entity.NewsTag;

public interface NewsTagRepository extends CrudRepository<NewsTag, Long> {

	/**
	 * Find tag by author name
	 * 
	 * @param name
	 *            name to find
	 * @return Found Tag or null otherwise
	 */
	NewsTag findByName(String name);

}
