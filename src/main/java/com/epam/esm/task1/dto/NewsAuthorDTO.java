package com.epam.esm.task1.dto;

import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "author")
public class NewsAuthorDTO extends AbstractDTO<Long> {

	@NotNull(message = "Name should not be blank.")
	@Size(min = 1, max = 100, message = "Name length should be from {min} to {max}.")
	private String name;

	private int newsCount;

	private Set<NewsDTO> news;

	public NewsAuthorDTO() {
	}

	public NewsAuthorDTO(Long id, String name, Set<NewsDTO> news, int newsCount) {
		super(id);
		this.name = name;
		this.news = news;
		this.newsCount = newsCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<NewsDTO> getNews() {
		return news;
	}

	public void setNews(Set<NewsDTO> news) {
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
		NewsAuthorDTO other = (NewsAuthorDTO) obj;
		return new EqualsBuilder().appendSuper(super.equals(other)).build();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString()).append("name", name)
				.append("newsCount", newsCount).build();
	}

}
