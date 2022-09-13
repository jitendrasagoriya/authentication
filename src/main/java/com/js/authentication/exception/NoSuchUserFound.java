package com.js.authentication.exception;

public class NoSuchUserFound extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3328452967452616649L;

	public NoSuchUserFound() {
		super("User Not Found. UserName");
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
