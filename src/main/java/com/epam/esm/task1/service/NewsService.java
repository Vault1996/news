package com.epam.esm.task1.service;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.epam.esm.task1.dto.NewsDTO;
import com.epam.esm.task1.dto.NewsImportContent;
import com.epam.esm.task1.dto.pagination.Page;
import com.epam.esm.task1.dto.pagination.PageInfo;
import com.epam.esm.task1.dto.search.NewsSearchRequestDTO;

/**
 * Service to work with news.
 * 
 * @author Algis_Ivashkiavichus
 */

@Validated
public interface NewsService extends CrudPaginatableService<NewsDTO, Long> {

	/**
	 * Method to perform search request.
	 * 
	 * @param pageInfo
	 *            info to perform pagination
	 * @param searchRequest
	 *            search request info.
	 * @return Page object, containing found news and page info.
	 */
	Page<NewsDTO> findAll(PageInfo pageInfo, NewsSearchRequestDTO searchRequest);

	/**
	 * Save news in NewsImportContent
	 * 
	 * @param newsImportContent
	 *            container of news to save
	 */
	void save(@Valid NewsImportContent newsImportContent);

}