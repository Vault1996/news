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

import com.epam.esm.task1.dto.NewsAuthorDTO;
import com.epam.esm.task1.exception.ValidationException;
import com.epam.esm.task1.service.NewsAuthorService;

/**
 * Rest Service to perform operations with NewsAuthor resource
 * 
 * @author Algis_Ivashkiavichus
 *
 */
@CrossOrigin
@RestController
@RequestMapping("/authors")
public class NewsAuthorController {

	private static final String ID = "/{id:[\\d]+}";

	private static final String AUTHORS_PATH = "/authors/{id}";

	@Autowired
	private NewsAuthorService newsAuthorService;

	/**
	 * Method to find all authors in the system
	 * 
	 * @return List of all authors
	 */
	@GetMapping
	public List<NewsAuthorDTO> findAll() {
		return newsAuthorService.findAll();
	}

	/**
	 * Method to find NewsAuthor by id
	 * 
	 * @param id
	 *            id of NewsAuthor to find
	 * @return found NewsAuthor or null otherwise
	 */
	@GetMapping(ID)
	public NewsAuthorDTO findById(@PathVariable Long id) {
		return newsAuthorService.findById(id);
	}

	/**
	 * Method to save NewsAuthor in the system
	 * 
	 * @param newsAuthor
	 *            entity to save
	 * @return Response entity with CREATED http status code and location header
	 */
	@PostMapping
	public ResponseEntity<Void> save(@Valid @RequestBody NewsAuthorDTO newsAuthor, UriComponentsBuilder b) {
		NewsAuthorDTO newsAuthorDTO = newsAuthorService.save(newsAuthor);
		UriComponents uriComponents = b.path(AUTHORS_PATH).buildAndExpand(newsAuthorDTO.getId());
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uriComponents.toUri());
		return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}

	/**
	 * Method to edit NewsAuthor in the system. Param id and param newsAuthor.id
	 * should be the same
	 * 
	 * @param id
	 *            id of NewsAuthor to edit
	 * @param newsAuthor
	 *            NewsAuthor entity to update
	 * @return Response entity with CREATED http status code and location header
	 */
	@PutMapping(ID)
	public ResponseEntity<Void> edit(@PathVariable Long id, @Valid @RequestBody NewsAuthorDTO newsAuthor,
			UriComponentsBuilder b) {
		if (!id.equals(newsAuthor.getId())) {
			throw new ValidationException("Id in path and in request body should be the same");
		}
		NewsAuthorDTO newsAuthorDTO = newsAuthorService.update(newsAuthor);
		UriComponents uriComponents = b.path(AUTHORS_PATH).buildAndExpand(newsAuthorDTO.getId());
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uriComponents.toUri());
		return new ResponseEntity<>(headers, HttpStatus.OK);
	}

	/**
	 * Method to delete NewsAuthor from the system
	 * 
	 * @param id
	 *            id of NewsAuthor to delete
	 */
	@DeleteMapping(ID)
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		newsAuthorService.delete(id);
	}
}
