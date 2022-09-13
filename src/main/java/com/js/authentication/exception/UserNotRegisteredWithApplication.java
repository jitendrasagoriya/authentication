package com.js.authentication.exception;

public class UserNotRegisteredWithApplication extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7550655206416893113L;

	public UserNotRegisteredWithApplication() {
		super("User is not registered with this application."); 
	}

	public UserNotRegisteredWithApplication(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public UserNotRegisteredWithApplication(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UserNotRegisteredWithApplication(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UserNotRegisteredWithApplication(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	

}
