package com.epam.esm.task1.dto.pagination;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * DTO class that contains info about returned page info such as page number,
 * number of page, count of requested objects and requested objects.
 * 
 * @author Algis_Ivashkiavichus
 *
 * @param <T>
 *            requested object
 */
public class Page<T extends Object> {

	private int pageNumber;

	private int numberOfPages;

	private int totalCount;

	private List<T> entities = new ArrayList<>();

	public Page() {
	}

	public Page(int totalCount, int offset, int limit, List<T> entities) {
		this.totalCount = totalCount;
		this.pageNumber = offset / limit + 1;
		this.numberOfPages = (int) Math.ceil((double) totalCount / limit);
		this.entities = entities;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getNumberOfPages() {
		return numberOfPages;
	}

	public void setNumberOfPages(int numberOfPages) {
		this.numberOfPages = numberOfPages;
	}

	public List<T> getEntities() {
		return entities;
	}

	public void setEntities(List<T> entities) {
		this.entities = entities;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("totalCount", totalCount).append("pageNumber", pageNumber)
				.append("numberOfPages", numberOfPages).append("entities", entities).build();
	}

}
