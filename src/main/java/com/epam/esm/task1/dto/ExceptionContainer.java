package com.epam.esm.task1.dto;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ExceptionContainer implements Serializable {

	private static final long serialVersionUID = 1L;

	private int code;
	private String reason;

	public ExceptionContainer() {
	}

	public ExceptionContainer(int code, String reason) {
		this.code = code;
		this.reason = reason;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(code).append(reason).build();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExceptionContainer other = (ExceptionContainer) obj;
		return new EqualsBuilder().append(code, other.code).append(reason, other.reason).build();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("code", code).append("reason", reason).build();
	}
}