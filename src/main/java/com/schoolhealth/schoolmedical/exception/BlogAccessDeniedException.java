package com.schoolhealth.schoolmedical.exception;

/**
 * Thrown when a user tries to perform an action they are not authorized for.
 */
public class BlogAccessDeniedException extends RuntimeException {
    public BlogAccessDeniedException(String message) {
        super(message);
    }
}