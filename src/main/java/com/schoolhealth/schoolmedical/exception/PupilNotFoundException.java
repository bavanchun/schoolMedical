package com.schoolhealth.schoolmedical.exception;

/**
 * Exception thrown when a pupil referenced in a request cannot be found.
 */
public class PupilNotFoundException extends RuntimeException {
    public PupilNotFoundException(String message) {
        super(message);
    }
}
