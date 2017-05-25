package com.epam.esm.task1.controller.rest;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.epam.esm.task1.dto.NewsTagDTO;
import com.epam.esm.task1.exception.ValidationException;
import com.epam.esm.task1.service.NewsTagService;

/**
 * Rest Service to perform operations with NewsTag resource
 * 
 * @author Algis_Ivashkiavichus
 *
 */
@CrossOrigin
@RestController
@RequestMapping("/tags")
public class NewsTagController {

	private static final String ID = "/{id:[\\d]+}";

	private static final String TAGS_PATH = "/tags/{id}";

	@Autowired
	private NewsTagService newsTagService;

	/**
	 * Method to find all tags in the system
	 * 
	 * @return List of all tags
	 */
	@GetMapping
	public List<NewsTagDTO> findAll() {
		return newsTagService.findAll();
	}

	/**
	 * Method to find NewsTag by id
	 * 
	 * @param id
	 *            id of NewsTag to find
	 * @return found NewsTag or null otherwise
	 */
	@GetMapping(ID)
	public NewsTagDTO findById(@PathVariable Long id) {
		return newsTagService.findById(id);
	}

	/**
	 * Method to save NewsTag in the system
	 * 
	 * @param newsTag
	 *            entity to save
	 * @return Response entity with CREATED http status code and location header
	 */
	@PostMapping
	public ResponseEntity<Void> save(@Valid @RequestBody NewsTagDTO newsTag, UriComponentsBuilder b) {
		NewsTagDTO newsTagDTO = newsTagService.save(newsTag);
		UriComponents uriComponents = b.path(TAGS_PATH).buildAndExpand(newsTagDTO.getId());
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uriComponents.toUri());
		return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}

	/**
	 * Method to edit NewsTag in the system. Param id and param newsTag.id
	 * should be the same
	 * 
	 * @param id
	 *            id of NewsTag to edit
	 * @param newsTag
	 *            NewsTag entity to update
	 * @return Response entity with CREATED http status code and location header
	 */
	@PutMapping(ID)
	public ResponseEntity<Void> edit(@PathVariable Long id, @Valid @RequestBody NewsTagDTO newsTag,
			UriComponentsBuilder b) {
		if (!id.equals(newsTag.getId())) {
			throw new ValidationException("Id in path and in request body should be the same");
		}
		NewsTagDTO newsTagDTO = newsTagService.update(newsTag);
		UriComponents uriComponents = b.path(TAGS_PATH).buildAndExpand(newsTagDTO.getId());
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uriComponents.toUri());
		return new ResponseEntity<>(headers, HttpStatus.OK);
	}

	/**
	 * Method to delete NewsTag from the system
	 * 
	 * @param id
	 *            id of NewsTag to delete
	 */
	@DeleteMapping(ID)
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		newsTagService.delete(id);
	}
}
