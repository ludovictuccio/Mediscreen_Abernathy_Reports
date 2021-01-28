package com.mediscreen.reports.exceptions;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiError {

    private HttpStatus status;
    private String message;
    private List<String> errors;

    public ApiError(HttpStatus httpstatus, final String message,
            final List<String> errors) {
        super();
        this.setStatus(httpstatus);
        this.message = message;
        this.errors = errors;
    }

    public ApiError(HttpStatus httpstatus, final String message,
            final String error) {
        super();
        this.setStatus(httpstatus);
        this.message = message;
        errors = Arrays.asList(error);
    }

}
