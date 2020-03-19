package com.js.authentication.exception;

public class ApplicationNotRegistered extends Exception {

    public ApplicationNotRegistered() {
        super("Application is not registered with us.");
    }

    public ApplicationNotRegistered(String applicationId) {
        super("Application is not registered with us, Application id is :"+applicationId);
    }
}
