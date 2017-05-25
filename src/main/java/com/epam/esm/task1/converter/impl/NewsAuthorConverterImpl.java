package com.epam.esm.task1.converter.impl;

import javax.persistence.PersistenceUnitUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.esm.task1.converter.Converter;
import com.epam.esm.task1.dto.NewsAuthorDTO;
import com.epam.esm.task1.dto.NewsDTO;
import com.epam.esm.task1.entity.News;
import com.epam.esm.task1.entity.NewsAuthor;

@Component
public class NewsAuthorConverterImpl extends AbstractConverter<NewsAuthor, NewsAuthorDTO>
		implements Converter<NewsAuthor, NewsAuthorDTO> {

	@Autowired
	private PersistenceUnitUtil persistenceUnitUtil;

	@Autowired
	private Converter<News, NewsDTO> newsConverter;

	@Override
	public NewsAuthor convertToEntity(NewsAuthorDTO dto) {
		NewsAuthor newsAuthor = new NewsAuthor();
		newsAuthor.setId(dto.getId());
		newsAuthor.setName(dto.getName());
		newsAuthor.setNews(newsConverter.convertToEntity(dto.getNews()));
		newsAuthor.setNewsCount(dto.getNewsCount());
		return newsAuthor;
	}

	@Override
	public NewsAuthorDTO convertToDTO(NewsAuthor entity) {
		NewsAuthorDTO newsAuthorDTO = new NewsAuthorDTO();
		newsAuthorDTO.setId(entity.getId());
		newsAuthorDTO.setName(entity.getName());
		newsAuthorDTO.setNewsCount(entity.getNewsCount());
		if (persistenceUnitUtil.isLoaded(entity.getNews())) {
			newsAuthorDTO.setNews(newsConverter.convertToDTO(entity.getNews()));
		}
		return newsAuthorDTO;
	}

}
