package com.js.authentication.exception;

public class UserDetailsNotFound extends Exception{
    public UserDetailsNotFound() {
        super("User Details Not Found!!!");
    }

    public UserDetailsNotFound(String id) {
        super("User details not found. User Id : "+ id);
    }

    public UserDetailsNotFound(String id, Throwable cause) {
        super("User details not found. User Id : "+ id, cause);
    }

    public UserDetailsNotFound(Throwable cause) {
        super(cause);
    }

    public UserDetailsNotFound(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
