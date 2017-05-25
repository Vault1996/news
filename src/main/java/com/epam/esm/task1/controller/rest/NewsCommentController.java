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

import com.epam.esm.task1.dto.NewsCommentDTO;
import com.epam.esm.task1.exception.ValidationException;
import com.epam.esm.task1.service.NewsCommentService;

/**
 * Rest Service to perform operations with NewsComment resource
 * 
 * @author Algis_Ivashkiavichus
 *
 */
@CrossOrigin
@RestController
@RequestMapping("/comments")
public class NewsCommentController {

	private static final String ID = "/{id:[\\d]+}";

	private static final String COMMENTS_PATH = "/comments/{id}";

	@Autowired
	private NewsCommentService newsCommentService;

	/**
	 * Method to find all comments in the system
	 * 
	 * @return List of all comments
	 */
	@GetMapping
	public List<NewsCommentDTO> findAll() {
		return newsCommentService.findAll();
	}

	/**
	 * Method to find NewsComment by id
	 * 
	 * @param id
	 *            id of NewsComment to find
	 * @return found NewsComment or null otherwise
	 */
	@GetMapping(ID)
	public NewsCommentDTO findById(@PathVariable Long id) {
		return newsCommentService.findById(id);
	}

	/**
	 * Method to save NewsComment in the system
	 * 
	 * @param newsComment
	 *            entity to save
	 * @return Response entity with CREATED http status code and location header
	 */
	@PostMapping
	public ResponseEntity<Void> save(@Valid @RequestBody NewsCommentDTO newsComment, UriComponentsBuilder b) {
		NewsCommentDTO newsCommentDTO = newsCommentService.save(newsComment);
		UriComponents uriComponents = b.path(COMMENTS_PATH).buildAndExpand(newsCommentDTO.getId());
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uriComponents.toUri());
		return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}

	/**
	 * Method to edit NewsComment in the system. Param id and param
	 * newsComment.id should be the same
	 * 
	 * @param id
	 *            id of NewsComment to edit
	 * @param newsComment
	 *            NewsComment entity to update
	 * @return Response entity with CREATED http status code and location header
	 */
	@PutMapping(ID)
	public ResponseEntity<Void> edit(@PathVariable Long id, @Valid @RequestBody NewsCommentDTO newsComment,
			UriComponentsBuilder b) {
		if (!id.equals(newsComment.getId())) {
			throw new ValidationException("Id in path and in request body should be the same");
		}
		NewsCommentDTO newsCommentDTO = newsCommentService.update(newsComment);
		UriComponents uriComponents = b.path(COMMENTS_PATH).buildAndExpand(newsCommentDTO.getId());
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uriComponents.toUri());
		return new ResponseEntity<>(headers, HttpStatus.OK);
	}

	/**
	 * Method to delete NewsComment from the system
	 * 
	 * @param id
	 *            id of NewsComment to delete
	 */
	@DeleteMapping(ID)
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		newsCommentService.delete(id);
	}
}
