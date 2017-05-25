package com.epam.esm.task1.repository;

import com.epam.esm.task1.entity.NewsAuthor;

public interface NewsAuthorRepository extends CrudRepository<NewsAuthor, Long> {

	/**
	 * Find author by author name
	 * 
	 * @param name
	 *            name to find
	 * @return Found Author or null otherwise
	 */
	NewsAuthor findByName(String name);

}
