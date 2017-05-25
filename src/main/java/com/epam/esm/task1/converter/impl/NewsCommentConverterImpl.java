package com.epam.esm.task1.converter.impl;

import javax.persistence.PersistenceUnitUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.esm.task1.converter.Converter;
import com.epam.esm.task1.dto.NewsCommentDTO;
import com.epam.esm.task1.entity.NewsComment;

@Component
public class NewsCommentConverterImpl extends AbstractConverter<NewsComment, NewsCommentDTO>
		implements Converter<NewsComment, NewsCommentDTO> {

	@Autowired
	private PersistenceUnitUtil persistenceUnitUtil;

	@Override
	public NewsComment convertToEntity(NewsCommentDTO dto) {
		NewsComment newsComment = new NewsComment();
		newsComment.setId(dto.getId());
		newsComment.setText(dto.getText());
		newsComment.setCreationTime(dto.getCreationTime());
		newsComment.setNews(dto.getNews());
		return newsComment;
	}

	@Override
	public NewsCommentDTO convertToDTO(NewsComment entity) {
		NewsCommentDTO newsCommentDTO = new NewsCommentDTO();
		newsCommentDTO.setId(entity.getId());
		newsCommentDTO.setText(entity.getText());
		newsCommentDTO.setCreationTime(entity.getCreationTime());
		if (persistenceUnitUtil.isLoaded(entity.getNews())) {
			newsCommentDTO.setNews(entity.getNews());
		}
		return newsCommentDTO;

	}

}
