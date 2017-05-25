package com.epam.esm.task1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.epam.esm.task1.dto.NewsAuthorDTO;
import com.epam.esm.task1.entity.NewsAuthor;
import com.epam.esm.task1.repository.NewsAuthorRepository;
import com.epam.esm.task1.repository.exception.UniqueConstraintViolationException;
import com.epam.esm.task1.service.NewsAuthorService;

@Service
public class NewsAuthorServiceImpl extends AbstractCrudService<NewsAuthorDTO, Long, NewsAuthor>
		implements NewsAuthorService {

	@Autowired
	private NewsAuthorRepository newsAuthorRepository;

	@Override
	@Transactional
	public NewsAuthorDTO save(NewsAuthorDTO dto) {
		Assert.notNull(dto, "News author should not be null");
		dto.setId(null);
		NewsAuthor author = newsAuthorRepository.findByName(dto.getName());
		if (author == null) {
			NewsAuthor entity = converter.convertToEntity(dto);
			return converter.convertToDTO(crudRepository.save(entity));
		} else {
			throw new UniqueConstraintViolationException("Author name is already exists.");
		}

	}

	@Override
	@Transactional
	public NewsAuthorDTO update(NewsAuthorDTO dto) {
		Assert.notNull(dto, "News author should not be null.");
		NewsAuthor author = newsAuthorRepository.findByName(dto.getName());
		if (author == null) {
			NewsAuthor entity = converter.convertToEntity(dto);
			return converter.convertToDTO(crudRepository.update(entity));
		} else {
			throw new UniqueConstraintViolationException("Author name is already exists.");
		}

	}
}
