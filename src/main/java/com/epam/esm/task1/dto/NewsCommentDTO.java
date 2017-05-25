package com.epam.esm.task1.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.epam.esm.task1.entity.News;

public class NewsCommentDTO extends AbstractDTO<Long> {

	@NotNull(message = "Comment text should not be blank.")
	@Size(min = 1, max = 300, message = "Comment text length should be from {min} to {max}.")
	private String text;

	private News news;

	private LocalDateTime creationTime;

	public NewsCommentDTO() {
	}

	public NewsCommentDTO(Long id, String text, News news, LocalDateTime creationTime) {
		super(id);
		this.text = text;
		this.news = news;
		this.creationTime = creationTime;
	}

	public News getNews() {
		return news;
	}

	public void setNews(News news) {
		this.news = news;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public LocalDateTime getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(LocalDateTime creationTime) {
		this.creationTime = creationTime;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().appendSuper(super.hashCode()).append(text).append(creationTime).build();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NewsCommentDTO other = (NewsCommentDTO) obj;
		return new EqualsBuilder().appendSuper(super.equals(other)).append(text, other.text)
				.append(creationTime, other.creationTime).build();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString()).append("text", text)
				.append("creationtime", creationTime).build();
	}

}
