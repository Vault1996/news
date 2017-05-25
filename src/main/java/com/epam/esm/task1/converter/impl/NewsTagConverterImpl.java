package com.epam.esm.task1.converter.impl;

import javax.persistence.PersistenceUnitUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.esm.task1.converter.Converter;
import com.epam.esm.task1.dto.NewsDTO;
import com.epam.esm.task1.dto.NewsTagDTO;
import com.epam.esm.task1.entity.News;
import com.epam.esm.task1.entity.NewsTag;

@Component
public class NewsTagConverterImpl extends AbstractConverter<NewsTag, NewsTagDTO>
		implements Converter<NewsTag, NewsTagDTO> {

	@Autowired
	private PersistenceUnitUtil persistenceUnitUtil;

	@Autowired
	private Converter<News, NewsDTO> newsConverter;

	@Override
	public NewsTagDTO convertToDTO(NewsTag entity) {
		NewsTagDTO newsTagDTO = new NewsTagDTO();
		newsTagDTO.setId(entity.getId());
		newsTagDTO.setName(entity.getName());
		newsTagDTO.setNewsCount(entity.getNewsCount());
		if (persistenceUnitUtil.isLoaded(entity.getNews())) {
			newsTagDTO.setNews(newsConverter.convertToDTO(entity.getNews()));
		}
		return newsTagDTO;
	}

	@Override
	public NewsTag convertToEntity(NewsTagDTO dto) {
		NewsTag newsTag = new NewsTag();
		newsTag.setId(dto.getId());
		newsTag.setName(dto.getName());
		newsTag.setNewsCount(dto.getNewsCount());
		newsTag.setNews(newsConverter.convertToEntity(dto.getNews()));
		return newsTag;
	}
}
