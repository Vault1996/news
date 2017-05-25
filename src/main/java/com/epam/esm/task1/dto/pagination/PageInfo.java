package com.epam.esm.task1.dto.pagination;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Class that contains info about pagination request.
 * 
 * @author Algis_Ivashkiavichus
 *
 */
public class PageInfo {

	private int offset;

	private int limit;

	public PageInfo() {
	}

	public PageInfo(int offset, int limit) {
		this.offset = offset;
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(limit).append(offset).build();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PageInfo other = (PageInfo) obj;
		return new EqualsBuilder().append(limit, other.limit).append(offset, other.offset).build();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("offset", offset).append("limit", limit).build();
	}

}
