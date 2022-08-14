package com.js.authentication.exception;

public class NoSuchUserFound extends Exception {

	public NoSuchUserFound() {
		super();
	}

	public NoSuchUserFound(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NoSuchUserFound(String message, Throwable cause) {
		super(message, cause);
	}

	public NoSuchUserFound(String message) {
		super("User Not Found. UserName ->"+message);
	}

	public NoSuchUserFound(Throwable cause) {
		super(cause);
	}
	
	

}
