package com.js.authentication.exception;

@SuppressWarnings("serial")
public class UserNotVerified extends Exception{

	public UserNotVerified() {
		super("User is not verified."); 
	}

	public UserNotVerified(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public UserNotVerified(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UserNotVerified(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UserNotVerified(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
