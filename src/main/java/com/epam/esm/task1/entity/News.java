package com.epam.esm.task1.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * Entity in the system to represent News.
 * 
 * @author Algis_Ivashkiavichus
 *
 */
@Entity
@Table(name = "news")
@AttributeOverride(name = "id", column = @Column(name = "news_id"))
@DynamicUpdate
@DynamicInsert
@NamedEntityGraph(name = "newsTagsAndAuthors", attributeNodes = { @NamedAttributeNode("tags"),
		@NamedAttributeNode("authors") })
public class News extends AbstractEntity<Long> {

	@Column(name = "news_title", nullable = false, length = 100)
	private String title;

	@Column(name = "news_creation_time", nullable = false)
	private LocalDateTime creationTime;

	@Column(name = "news_last_modification_time")
	private LocalDateTime lastModificationTime;

	@Column(name = "news_description", nullable = false, length = 300)
	private String description;

	@Column(name = "news_full_text", nullable = false, length = 2000)
	private String fullText;

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "news")
	private Set<NewsComment> comments = new HashSet<>();

	@ManyToMany
	@JoinTable(name = "news_to_authors", joinColumns = @JoinColumn(name = "news_id"), inverseJoinColumns = @JoinColumn(name = "na_id"))
	private Set<NewsAuthor> authors = new HashSet<>();

	@ManyToMany
	@JoinTable(name = "news_to_tags", joinColumns = @JoinColumn(name = "news_id"), inverseJoinColumns = @JoinColumn(name = "nt_id"))
	private Set<NewsTag> tags = new HashSet<>();

	@Transient
	private Long commentsCount;

	public News() {
	}

	public News(Long id, String title, LocalDateTime creationTime, LocalDateTime lastModificationTime,
			String description, Long commentsCount) {
		super(id);
		this.title = title;
		this.creationTime = creationTime;
		this.lastModificationTime = lastModificationTime;
		this.description = description;
		this.commentsCount = commentsCount;
	}

	public Long getCommentsCount() {
		return commentsCount;
	}

	public void setCommentsCount(Long commentsCount) {
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

	public Set<NewsComment> getComments() {
		return comments;
	}

	public void setComments(Set<NewsComment> comments) {
		this.comments = comments;
	}

	public Set<NewsAuthor> getAuthors() {
		return authors;
	}

	public void setAuthors(Set<NewsAuthor> authors) {
		this.authors = authors;
	}

	public Set<NewsTag> getTags() {
		return tags;
	}

	public void setTags(Set<NewsTag> tags) {
		this.tags = tags;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(creationTime).build();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		News other = (News) obj;
		return new EqualsBuilder().append(creationTime, other.creationTime).build();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString()).append("title", title)
				.append("description", description).append("fullText", fullText).append("creationTime", creationTime)
				.append("lastModificationTime", lastModificationTime).append("commentsCount", commentsCount).build();
	}

	@PrePersist
	public void beforeSaveActions() {
		creationTime = LocalDateTime.now();
		lastModificationTime = null;
		setId(null);
	}

}
