package com.js.authentication.exception;

public class UserAlreadyExixts extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserAlreadyExixts() {
		super();
	}

	public UserAlreadyExixts(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UserAlreadyExixts(String message, Throwable cause) {
		super(message, cause);
	}

	public UserAlreadyExixts(String message) {
		super("User is already registeded. User Name -> "+message);
	}

	public UserAlreadyExixts(Throwable cause) {
		super(cause);
	}
	

}
