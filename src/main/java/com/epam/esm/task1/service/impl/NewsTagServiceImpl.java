package com.epam.esm.task1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.epam.esm.task1.dto.NewsTagDTO;
import com.epam.esm.task1.entity.NewsTag;
import com.epam.esm.task1.repository.NewsTagRepository;
import com.epam.esm.task1.repository.exception.UniqueConstraintViolationException;
import com.epam.esm.task1.service.NewsTagService;

@Service
public class NewsTagServiceImpl extends AbstractCrudService<NewsTagDTO, Long, NewsTag> implements NewsTagService {

	@Autowired
	private NewsTagRepository newsTagRepository;

	@Override
	@Transactional
	public NewsTagDTO save(NewsTagDTO dto) {
		Assert.notNull(dto, "News tag should not be null");
		dto.setId(null);
		NewsTag author = newsTagRepository.findByName(dto.getName());
		if (author == null) {
			NewsTag entity = converter.convertToEntity(dto);
			return converter.convertToDTO(crudRepository.save(entity));
		} else {
			throw new UniqueConstraintViolationException("Tag name is already exists.");
		}
	}

	@Override
	@Transactional
	public NewsTagDTO update(NewsTagDTO dto) {
		Assert.notNull(dto, "News tag should not be null.");
		NewsTag author = newsTagRepository.findByName(dto.getName());
		if (author == null) {
			NewsTag entity = converter.convertToEntity(dto);
			return converter.convertToDTO(crudRepository.update(entity));
		} else {
			throw new UniqueConstraintViolationException("Tag name is already exists.");
		}

	}

}
