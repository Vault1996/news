package com.epam.esm.task1.dto.search;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO that is actually a search criteria. Contains list of tag ids and list of
 * author ids to perform search actions.
 * 
 * @author Algis_Ivashkiavichus
 *
 */
public class NewsSearchRequestDTO {

	private List<Long> tagIds = new ArrayList<>();

	private List<Long> authorIds = new ArrayList<>();

	public NewsSearchRequestDTO() {
	}

	public NewsSearchRequestDTO(List<Long> tagIds, List<Long> authorIds) {
		this.tagIds = tagIds;
		this.authorIds = authorIds;
	}

	public List<Long> getTagIds() {
		return tagIds;
	}

	public void setTagIds(List<Long> tagIds) {
		this.tagIds = tagIds;
	}

	public List<Long> getAuthorIds() {
		return authorIds;
	}

	public void setAuthorIds(List<Long> authorIds) {
		this.authorIds = authorIds;
	}

}
