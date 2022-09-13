package com.js.authentication.exception;

 
public class UserNotAuthorized extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 

	public UserNotAuthorized() {
		super("User is not authorized for this service.");
	}

	public UserNotAuthorized(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public UserNotAuthorized(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UserNotAuthorized(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UserNotAuthorized(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
