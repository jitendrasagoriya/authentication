package com.js.authentication.exception;

public class InvalidUserNameAndPassword extends Exception{

	public InvalidUserNameAndPassword() {
		super("Invalid Username or password."); 
	}

	public InvalidUserNameAndPassword(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public InvalidUserNameAndPassword(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public InvalidUserNameAndPassword(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public InvalidUserNameAndPassword(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
