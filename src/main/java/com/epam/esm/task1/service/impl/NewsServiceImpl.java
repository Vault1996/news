package com.epam.esm.task1.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.epam.esm.task1.dto.NewsDTO;
import com.epam.esm.task1.dto.NewsImportContent;
import com.epam.esm.task1.dto.pagination.Page;
import com.epam.esm.task1.dto.pagination.PageInfo;
import com.epam.esm.task1.dto.search.NewsSearchRequestDTO;
import com.epam.esm.task1.entity.News;
import com.epam.esm.task1.repository.NewsRepository;
import com.epam.esm.task1.service.NewsService;

/**
 * Service to work with news.
 * 
 * @author Algis_Ivashkiavichus
 */
@Service
public class NewsServiceImpl extends AbstractPaginatableCrudService<NewsDTO, Long, News> implements NewsService {

	private static final String AUTHORS = "authors";
	private static final String TAGS = "tags";

	@Autowired
	private NewsRepository paginatableSearchRepository;

	@Override
	@Transactional
	public NewsDTO update(NewsDTO news) {
		Assert.notNull(news, "News should not be null.");
		news.setLastModificationTime(LocalDateTime.now());
		News newsEntity = converter.convertToEntity(news);
		return converter.convertToDTO(crudRepository.update(newsEntity));
	}

	@Override
	@Transactional(readOnly = true)
	public Page<NewsDTO> findAll(PageInfo pageInfo, NewsSearchRequestDTO searchRequest) {
		if (pageInfo.getLimit() > LIMIT_TO_PAGINATION) {
			throw new ValidationException("Greatest number of news per page is " + LIMIT_TO_PAGINATION);
		}
		Map<String, List<Long>> searchMap = new HashMap<>();

		List<Long> tagSearch = searchRequest.getTagIds();
		List<Long> authorSearch = searchRequest.getAuthorIds();

		searchMap.put(TAGS, tagSearch);
		searchMap.put(AUTHORS, authorSearch);

		Page<News> pages = paginatableSearchRepository.findAll(pageInfo, searchMap);

		Page<NewsDTO> pageDTO = converter.convertToDTO(pages);

		return pageDTO;
	}

	@Override
	@Transactional
	public void save(@Valid NewsImportContent newsImportContent) {
		newsImportContent.getNews().forEach(this::save);
	}

}
