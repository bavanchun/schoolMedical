package com.schoolhealth.schoolmedical.exception;

/**
 * Thrown when a requested blog cannot be found.
 */
public class BlogNotFoundException extends RuntimeException {
    public BlogNotFoundException(String message) {
        super(message);
    }
}
