package com.js.authentication.exception;

public class NoSuchBeanException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoSuchBeanException(String message) {
        super(message);
    }

    public NoSuchBeanException(String message, Throwable cause) {
        super(message, cause);
    }
}
