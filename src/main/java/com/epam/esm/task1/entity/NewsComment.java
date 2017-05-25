package com.epam.esm.task1.entity;

import java.time.LocalDateTime;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "news_comments")
@AttributeOverride(name = "id", column = @Column(name = "nc_id"))
public class NewsComment extends AbstractEntity<Long> {

	@Column(name = "nc_text", nullable = false, unique = true, length = 300)
	private String text;

	@Column(name = "nc_creation_time", nullable = false)
	private LocalDateTime creationTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "nc_news_id")
	private News news;

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

	public News getNews() {
		return news;
	}

	public void setNews(News news) {
		this.news = news;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(creationTime).build();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NewsComment other = (NewsComment) obj;
		return new EqualsBuilder().append(creationTime, other.creationTime).build();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString()).append("text", text)
				.append("creationTime", creationTime).build();
	}

}
