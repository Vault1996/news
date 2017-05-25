package com.epam.esm.task1.controller.rest;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.epam.esm.task1.dto.NewsCommentDTO;
import com.epam.esm.task1.dto.NewsDTO;
import com.epam.esm.task1.dto.pagination.Page;
import com.epam.esm.task1.dto.pagination.PageInfo;
import com.epam.esm.task1.dto.pagination.PageInfoBuilder;
import com.epam.esm.task1.dto.search.NewsSearchRequestDTO;
import com.epam.esm.task1.exception.ValidationException;
import com.epam.esm.task1.service.NewsCommentService;
import com.epam.esm.task1.service.NewsService;

/**
 * Rest Service to perform operations with News resource
 * 
 * @author Algis_Ivashkiavichus
 *
 */
@CrossOrigin
@RestController
@RequestMapping("/news")
public class NewsController {

	private static final String ID = "/{id:[\\d]+}";

	private static final String LIMIT = "limit";

	private static final String OFFSET = "offset";

	private static final String DEFAULT_OFFSET = "0";

	private static final String DEFAULT_LIMIT = "20";

	private static final String NEWS_PATH = "/news/{id}";

	@Autowired
	private NewsService newsService;

	@Autowired
	private PageInfoBuilder pageInfoBuilder;

	@Autowired
	private NewsCommentService newsCommentService;

	/**
	 * Method to find news in the system
	 * 
	 * @param offset
	 *            offset from which to find
	 * @param limit
	 *            number of news to retrieve
	 * @return Page object with requested news
	 */
	@GetMapping
	public Page<NewsDTO> findAll(@RequestParam(name = OFFSET, defaultValue = DEFAULT_OFFSET) String offset,
			@RequestParam(name = LIMIT, defaultValue = DEFAULT_LIMIT) String limit) {
		PageInfo pageInfo = pageInfoBuilder.buildPageInfo(offset, limit);
		return newsService.findAll(pageInfo);
	}

	/**
	 * Method to find News by id
	 * 
	 * @param id
	 *            id of News to find
	 * @return found News or null otherwise
	 */
	@GetMapping(ID)
	public NewsDTO findById(@PathVariable Long id) {
		return newsService.findById(id);
	}

	/**
	 * Method to save News in the system
	 * 
	 * @param news
	 *            news to save
	 * @return Response entity with CREATED http status code and location header
	 */
	@PostMapping
	public ResponseEntity<Void> save(@Valid @RequestBody NewsDTO news, UriComponentsBuilder b) {
		NewsDTO newsDTO = newsService.save(news);
		UriComponents uriComponents = b.path(NEWS_PATH).buildAndExpand(newsDTO.getId());
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uriComponents.toUri());
		return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}

	/**
	 * Method to find news in the system by requested search criteria
	 * 
	 * @param offset
	 *            offset from which to find
	 * @param limit
	 *            number of news to retrieve
	 * @param searchRequest
	 *            request with info by which to search
	 * @return Page object with requested news
	 */
	@PostMapping("/search")
	public Page<NewsDTO> search(@RequestParam(name = OFFSET, defaultValue = DEFAULT_OFFSET) String offset,
			@RequestParam(name = LIMIT, defaultValue = DEFAULT_LIMIT) String limit,
			@RequestBody NewsSearchRequestDTO searchRequest) {

		PageInfo pageInfo = pageInfoBuilder.buildPageInfo(offset, limit);
		return newsService.findAll(pageInfo, searchRequest);
	}

	/**
	 * Method to edit News in the system. Param id and param newsAuthor.id
	 * should be the same
	 * 
	 * @param id
	 *            id of News to edit
	 * @param news
	 *            News entity to update
	 * @return Response entity with CREATED http status code and location header
	 */
	@PutMapping(ID)
	public ResponseEntity<Void> edit(@PathVariable Long id, @Valid @RequestBody NewsDTO news, UriComponentsBuilder b) {
		if (!id.equals(news.getId())) {
			throw new ValidationException("Id in path and in request body should be the same");
		}
		NewsDTO newsDTO = newsService.update(news);
		UriComponents uriComponents = b.path(NEWS_PATH).buildAndExpand(newsDTO.getId());
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uriComponents.toUri());
		return new ResponseEntity<>(headers, HttpStatus.OK);
	}

	/**
	 * Method to delete News from the system
	 * 
	 * @param id
	 *            id of News to delete
	 */
	@DeleteMapping(ID)
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		newsService.delete(id);
	}

	/**
	 * Method to find newsComments in the system by its news id
	 * 
	 * @param offset
	 *            offset from which to find
	 * @param limit
	 *            number of news comments to retrieve
	 * @param id
	 *            id of news
	 * @return Page object with requested news comments
	 */
	@GetMapping("/{id:[\\d]+}/comments")
	public Page<NewsCommentDTO> findByNewsId(@RequestParam(name = OFFSET, defaultValue = DEFAULT_OFFSET) String offset,
			@RequestParam(name = LIMIT, defaultValue = DEFAULT_LIMIT) String limit, @PathVariable Long id) {
		PageInfo pageInfo = pageInfoBuilder.buildPageInfo(offset, limit);
		return newsCommentService.findByNewsId(pageInfo, id);
	}

}