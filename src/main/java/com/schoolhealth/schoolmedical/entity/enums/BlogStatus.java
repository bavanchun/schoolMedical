package com.schoolhealth.schoolmedical.entity.enums;

/**
 * Represents the status of a blog post within the workflow.
 */
public enum BlogStatus {
    /** Created by nurse, not yet submitted for review */
    DRAFT,
    /** Submitted by nurse, waiting for manager review */
    PENDING,
    /** Approved and published for public view */
    PUBLISHED,
    /** Rejected by manager */
    REJECTED,
    /** Soft deleted and hidden from listings */
    DELETED
}
