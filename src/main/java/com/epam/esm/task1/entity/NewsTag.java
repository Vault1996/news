package com.epam.esm.task1.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Formula;

@Entity
@Table(name = "news_tags")
@AttributeOverride(name = "id", column = @Column(name = "nt_id"))
@NamedQuery(name = "NewsTag.findByName", query = "SELECT t FROM NewsTag t WHERE t.name = :name")
public class NewsTag extends AbstractEntity<Long> {

	@Column(name = "nt_name", nullable = false, unique = true, length = 30)
	private String name;

	@ManyToMany(mappedBy = "tags")
	private Set<News> news = new HashSet<>();

	@Formula("(SELECT COUNT(1) FROM news_to_tags ntt WHERE ntt.nt_id = nt_id)")
	private int newsCount;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<News> getNews() {
		return news;
	}

	public void setNews(Set<News> news) {
		this.news = news;
	}

	public int getNewsCount() {
		return newsCount;
	}

	public void setNewsCount(int newsCount) {
		this.newsCount = newsCount;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().appendSuper(super.hashCode()).build();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NewsTag other = (NewsTag) obj;
		return new EqualsBuilder().appendSuper(super.equals(other)).build();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString()).append("name", name)
				.append("newsCount", newsCount).build();
	}

}
