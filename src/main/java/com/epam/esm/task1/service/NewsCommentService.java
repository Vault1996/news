package com.epam.esm.task1.service;

import com.epam.esm.task1.dto.NewsCommentDTO;
import com.epam.esm.task1.dto.pagination.Page;
import com.epam.esm.task1.dto.pagination.PageInfo;

public interface NewsCommentService extends CrudPaginatableService<NewsCommentDTO, Long> {

	/**
	 * Find slice of all CommentsDTO that belongs to particular News
	 * 
	 * @param pageInfo
	 *            info to perform pagination
	 * @param newsId
	 *            id of news
	 * @return Page object, containing found comments and page info.
	 */
	Page<NewsCommentDTO> findByNewsId(PageInfo pageInfo, Long newsId);

}
