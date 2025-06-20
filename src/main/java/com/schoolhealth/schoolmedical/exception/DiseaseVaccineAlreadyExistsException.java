package com.schoolhealth.schoolmedical.exception;

public class DiseaseVaccineAlreadyExistsException extends RuntimeException {
    public DiseaseVaccineAlreadyExistsException(String message) {
        super(message);
    }
}
