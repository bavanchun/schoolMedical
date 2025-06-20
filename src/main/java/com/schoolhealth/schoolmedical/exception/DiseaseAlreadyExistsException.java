package com.schoolhealth.schoolmedical.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DiseaseAlreadyExistsException extends RuntimeException{
    public DiseaseAlreadyExistsException(String message) {
        super(message);
    }
}
