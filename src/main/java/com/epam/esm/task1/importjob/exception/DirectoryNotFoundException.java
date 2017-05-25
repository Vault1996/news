package com.epam.esm.task1.importjob.exception;

public class DirectoryNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -208202933450585465L;

	public DirectoryNotFoundException() {
	}

	public DirectoryNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DirectoryNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public DirectoryNotFoundException(String message) {
		super(message);
	}

	public DirectoryNotFoundException(Throwable cause) {
		super(cause);
	}

}
