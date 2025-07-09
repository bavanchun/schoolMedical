package com.schoolhealth.schoolmedical.exception;

/**
 * Thrown when a parent is already linked to a pupil in the pupil_parent table.
 */
public class ParentAlreadyLinkedException extends RuntimeException {
    public ParentAlreadyLinkedException(String message) {
        super(message);
    }
}
