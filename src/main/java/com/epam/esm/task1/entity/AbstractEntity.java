package com.epam.esm.task1.entity;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * General class for all entities in the system.
 * 
 * @author Algis_Ivashkiavichus
 */
@MappedSuperclass
public abstract class AbstractEntity<T extends Serializable> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private T id;

	public AbstractEntity() {
	}

	public AbstractEntity(T id) {
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

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractEntity<T> other = (AbstractEntity<T>) obj;
		return new EqualsBuilder().append(id, other.id).build();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("id", id).build();
	}
}
