package com.epam.esm.task1.converter.impl;

import javax.persistence.PersistenceUnitUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.esm.task1.converter.Converter;
import com.epam.esm.task1.dto.NewsAuthorDTO;
import com.epam.esm.task1.dto.NewsCommentDTO;
import com.epam.esm.task1.dto.NewsDTO;
import com.epam.esm.task1.dto.NewsTagDTO;
import com.epam.esm.task1.entity.News;
import com.epam.esm.task1.entity.NewsAuthor;
import com.epam.esm.task1.entity.NewsComment;
import com.epam.esm.task1.entity.NewsTag;

@Component
public class NewsConverterImpl extends AbstractConverter<News, NewsDTO> implements Converter<News, NewsDTO> {

	@Autowired
	private PersistenceUnitUtil persistenceUnitUtil;

	@Autowired
	private Converter<NewsTag, NewsTagDTO> newsTagConverter;

	@Autowired
	private Converter<NewsAuthor, NewsAuthorDTO> newsAuthorConverter;

	@Autowired
	private Converter<NewsComment, NewsCommentDTO> newsCommentConverter;

	@Override
	public NewsDTO convertToDTO(News entity) {
		NewsDTO newsDTO = new NewsDTO();
		newsDTO.setId(entity.getId());
		newsDTO.setTitle(entity.getTitle());
		newsDTO.setDescription(entity.getDescription());
		newsDTO.setFullText(entity.getFullText());
		newsDTO.setCreationTime(entity.getCreationTime());
		newsDTO.setLastModificationTime(entity.getLastModificationTime());
		newsDTO.setCommentsCount(entity.getCommentsCount());

		if (persistenceUnitUtil.isLoaded(entity.getAuthors())) {
			newsDTO.setAuthors(newsAuthorConverter.convertToDTO(entity.getAuthors()));
		}
		if (persistenceUnitUtil.isLoaded(entity.getComments())) {
			newsDTO.setComments(newsCommentConverter.convertToDTO(entity.getComments()));
		}
		if (persistenceUnitUtil.isLoaded(entity.getTags())) {
			newsDTO.setTags(newsTagConverter.convertToDTO(entity.getTags()));
		}
		return newsDTO;
	}

	@Override
	public News convertToEntity(NewsDTO dto) {
		News news = new News();
		news.setId(dto.getId());
		news.setTitle(dto.getTitle());
		news.setDescription(dto.getDescription());
		news.setFullText(dto.getFullText());
		news.setCreationTime(dto.getCreationTime());
		news.setLastModificationTime(dto.getLastModificationTime());
		news.setComments(newsCommentConverter.convertToEntity(dto.getComments()));
		news.setAuthors(newsAuthorConverter.convertToEntity(dto.getAuthors()));
		news.setTags(newsTagConverter.convertToEntity(dto.getTags()));
		return news;
	}

}
