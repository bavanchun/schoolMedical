package com.schoolhealth.schoolmedical.exception;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class VaccineAlreadyExistsException extends RuntimeException{
    public VaccineAlreadyExistsException (String message) {
        super(message);
    }

}
