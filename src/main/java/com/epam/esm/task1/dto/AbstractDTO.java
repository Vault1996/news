package com.epam.esm.task1.dto;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class AbstractDTO<T extends Serializable> {

	private T id;

	public AbstractDTO() {
	}

	public AbstractDTO(T id) {
		this.id = id;
	}

	public T getId() {
		return id;
	}

	public void setId(T id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(id).build();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractDTO other = (AbstractDTO) obj;
		return new EqualsBuilder().append(id, other.id).build();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("id", id).build();
	}

}
