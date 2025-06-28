package com.schoolhealth.schoolmedical.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException ex, WebRequest req) {
        return new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), req.getDescription(false));
    }

    @ExceptionHandler(UpdateNotAllowedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleUpdateNotAllowedException(UpdateNotAllowedException ex, WebRequest req) {
        return new ErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage(), req.getDescription(false));
    }
}
