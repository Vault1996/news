package com.epam.esm.task1.repository;

import java.util.List;
import java.util.Map;

import com.epam.esm.task1.dto.pagination.Page;
import com.epam.esm.task1.dto.pagination.PageInfo;
import com.epam.esm.task1.entity.News;

/**
 * Interface to work with news.
 * 
 * @author Algis_Ivashkiavichus
 *
 */
public interface NewsRepository extends CrudPaginatableRepository<News, Long> {

	/**
	 * Method to perform search request.
	 * 
	 * @param pageInfo
	 *            info to perform pagination
	 * @param searchRequest
	 *            search info. First parameter is the name of field in Entity by
	 *            which you want to perform search. Second parameter is a List
	 *            of Ids
	 * @return Page object, containing found news and page info.
	 */
	Page<News> findAll(PageInfo pageInfo, Map<String, List<Long>> searchRequest);

	/**
	 * Count comments for specified news
	 * 
	 * @param newsId
	 *            id of news
	 * @return number of comments associated with news
	 */
	int countComments(Long newsId);
}