package com.epam.esm.task1.repository;

import com.epam.esm.task1.dto.pagination.Page;
import com.epam.esm.task1.dto.pagination.PageInfo;
import com.epam.esm.task1.entity.NewsComment;

public interface NewsCommentRepository extends CrudPaginatableRepository<NewsComment, Long> {

	/**
	 * Find slice of all Comments that belongs to particular News
	 * 
	 * @param pageInfo
	 *            info to perform pagination
	 * @param newsId
	 *            id of news
	 * @return Page object, containing found comments and page info.
	 */
	Page<NewsComment> findByNewsId(PageInfo pageInfo, Long newsId);

}
