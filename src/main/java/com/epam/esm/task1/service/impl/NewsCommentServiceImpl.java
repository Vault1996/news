package com.epam.esm.task1.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.epam.esm.task1.dto.NewsCommentDTO;
import com.epam.esm.task1.dto.pagination.Page;
import com.epam.esm.task1.dto.pagination.PageInfo;
import com.epam.esm.task1.entity.NewsComment;
import com.epam.esm.task1.repository.NewsCommentRepository;
import com.epam.esm.task1.service.NewsCommentService;

@Service
public class NewsCommentServiceImpl extends AbstractPaginatableCrudService<NewsCommentDTO, Long, NewsComment>
		implements NewsCommentService {

	@Autowired
	private NewsCommentRepository newsCommentRepository;

	@Override
	@Transactional
	public NewsCommentDTO save(NewsCommentDTO dto) {
		Assert.notNull(dto, "News comment should not be null.");
		dto.setCreationTime(LocalDateTime.now());
		dto.setId(null);
		NewsComment newsComment = converter.convertToEntity(dto);
		return converter.convertToDTO(crudRepository.save(newsComment));
	}

	@Override
	@Transactional(readOnly = true)
	public Page<NewsCommentDTO> findByNewsId(PageInfo pageInfo, Long newsId) {
		return converter.convertToDTO(newsCommentRepository.findByNewsId(pageInfo, newsId));
	}

}
