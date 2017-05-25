package com.epam.esm.task1.repository.exception;

public class UniqueConstraintViolationException extends RuntimeException {

	private static final long serialVersionUID = 6591983381912175986L;

	public UniqueConstraintViolationException() {
	}

	public UniqueConstraintViolationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UniqueConstraintViolationException(String message, Throwable cause) {
		super(message, cause);
	}

	public UniqueConstraintViolationException(String message) {
		super(message);
	}

	public UniqueConstraintViolationException(Throwable cause) {
		super(cause);
	}

}
