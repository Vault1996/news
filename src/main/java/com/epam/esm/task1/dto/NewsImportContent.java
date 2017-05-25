package com.epam.esm.task1.dto;

import java.util.Collection;

import javax.validation.Valid;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * Class for importing a batch of news
 * 
 * @author Algis_Ivashkiavichus
 *
 */
@JacksonXmlRootElement(localName = "content")
public class NewsImportContent {

	@Valid
	@JacksonXmlElementWrapper(useWrapping = false)
	private Collection<NewsDTO> news;

	public Collection<NewsDTO> getNews() {
		return news;
	}

	public void setNews(Collection<NewsDTO> news) {
		this.news = news;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(news).build();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NewsImportContent other = (NewsImportContent) obj;
		return new EqualsBuilder().append(news, other.news).build();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("news", news).build();
	}

}
