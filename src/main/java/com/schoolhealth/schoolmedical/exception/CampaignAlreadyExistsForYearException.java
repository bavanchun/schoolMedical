package com.schoolhealth.schoolmedical.exception;

public class CampaignAlreadyExistsForYearException extends RuntimeException{
    public CampaignAlreadyExistsForYearException(String message) {
        super(message);
    }

    public CampaignAlreadyExistsForYearException(String message, Throwable cause) {
        super(message, cause);
    }
}
