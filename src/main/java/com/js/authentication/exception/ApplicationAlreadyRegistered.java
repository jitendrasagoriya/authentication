package com.js.authentication.exception;

public class ApplicationAlreadyRegistered extends Exception {

	public ApplicationAlreadyRegistered() {
		 super("Application is already registered with us.");
	}

	public ApplicationAlreadyRegistered(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public ApplicationAlreadyRegistered(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ApplicationAlreadyRegistered(String message) {
		super("Application is already registered with us. Application Name :" + message);
	}

	public ApplicationAlreadyRegistered(Throwable cause) {
		super(cause); 
	}

}
