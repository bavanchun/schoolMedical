package com.schoolhealth.schoolmedical.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {
    private HttpStatus status;
    private String message;
    private String path;
    private LocalDateTime timestamp;
}