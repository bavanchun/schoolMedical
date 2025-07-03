package com.schoolhealth.schoolmedical.exception;

/**
 * Thrown when a blog operation is attempted in an invalid state.
 */
public class InvalidBlogStatusException extends RuntimeException {
    public InvalidBlogStatusException(String message) {
        super(message);
    }
}
