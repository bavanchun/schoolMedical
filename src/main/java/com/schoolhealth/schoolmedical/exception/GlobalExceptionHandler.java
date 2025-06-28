package com.schoolhealth.schoolmedical.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ErrorResponse buildError(HttpStatus status, String message, HttpServletRequest request) {
        return new ErrorResponse(status, message, request.getRequestURI(), java.time.LocalDateTime.now());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex, HttpServletRequest request) {
        ErrorResponse error = buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        ErrorResponse error = buildError(HttpStatus.FORBIDDEN, ex.getMessage(), request);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateException(DuplicateException ex, HttpServletRequest request) {
        ErrorResponse error = buildError(HttpStatus.CONFLICT, ex.getMessage(), request);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex, HttpServletRequest request) {
        ErrorResponse error = buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + " " + err.getDefaultMessage())
                .findFirst().orElse("Validation error");
        ErrorResponse error = buildError(HttpStatus.BAD_REQUEST, message, request);
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOthers(Exception ex, HttpServletRequest request) {
        ErrorResponse error = buildError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
