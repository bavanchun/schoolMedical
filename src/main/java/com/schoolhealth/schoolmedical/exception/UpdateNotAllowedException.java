package com.schoolhealth.schoolmedical.exception;

public class UpdateNotAllowedException extends RuntimeException {
    public UpdateNotAllowedException(String message) {
        super(message);
    }

    public UpdateNotAllowedException(String message, Throwable cause) {
        super(message, cause);
    }
}
