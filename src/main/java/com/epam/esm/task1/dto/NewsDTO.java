package com.epam.esm.task1.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "news")
public class NewsDTO extends AbstractDTO<Long> {

	@NotNull(message = "Title should not be blank.")
	@Size(min = 1, max = 100, message = "Title length should be from {min} to {max}.")
	private String title;

	private LocalDateTime creationTime;

	private LocalDateTime lastModificationTime;

	@NotNull(message = "Description should not be blank.")
	@Size(min = 1, max = 300, message = "Description length should be from {min} to {max}.")
	private String description;

	@NotNull(message = "Full text should not be blank.")
	@Size(min = 1, max = 2000, message = "Full text length should be from {min} to {max}.")
	private String fullText;

	private Set<NewsCommentDTO> comments;

	@Size(min = 1, message = "News should have at least one author.")
	private Set<NewsAuthorDTO> authors = new HashSet<>();

	private Set<NewsTagDTO> tags;

	private Long commentsCount;

	public NewsDTO() {
	}

	public NewsDTO(Long id, String title, LocalDateTime creationTime, LocalDateTime lastModificationTime,
			String description, String fullText, Set<NewsCommentDTO> comments, Set<NewsAuthorDTO> authors,
			Set<NewsTagDTO> tags, Long commentsCount) {
		super(id);
		this.title = title;
		this.creationTime = creationTime;
		this.lastModificationTime = lastModificationTime;
		this.description = description;
		this.fullText = fullText;
		this.comments = comments;
		this.authors = authors;
		this.tags = tags;
		this.commentsCount = commentsCount;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LocalDateTime getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(LocalDateTime creationTime) {
		this.creationTime = creationTime;
	}

	public LocalDateTime getLastModificationTime() {
		return lastModificationTime;
	}

	public void setLastModificationTime(LocalDateTime lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFullText() {
		return fullText;
	}

	public void setFullText(String fullText) {
		this.fullText = fullText;
	}

	public Set<NewsCommentDTO> getComments() {
		return comments;
	}

	public void setComments(Set<NewsCommentDTO> comments) {
		this.comments = comments;
	}

	public Set<NewsAuthorDTO> getAuthors() {
		return authors;
	}

	public void setAuthors(Set<NewsAuthorDTO> authors) {
		this.authors = authors;
	}

	public Set<NewsTagDTO> getTags() {
		return tags;
	}

	public void setTags(Set<NewsTagDTO> tags) {
		this.tags = tags;
	}

	public Long getCommentsCount() {
		return commentsCount;
	}

	public void setCommentsCount(Long commentsCount) {
		this.commentsCount = commentsCount;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(title).append(description)
				.append(fullText).append(creationTime).append(lastModificationTime).build();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NewsDTO other = (NewsDTO) obj;
		return new EqualsBuilder().appendSuper(super.equals(other)).append(title, other.title)
				.append(description, other.description).append(fullText, other.fullText)
				.append(creationTime, other.creationTime).append(lastModificationTime, other.lastModificationTime)
				.isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString()).append("title", title)
				.append("description", description).append("fullText", fullText).append("creationTime", creationTime)
				.append("lastModificationTime", lastModificationTime).append("commentsCount", commentsCount).build();
	}
}
