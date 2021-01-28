package com.mediscreen.reports.exceptions;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import feign.FeignException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FeignException.class)
    public String handleFeignStatusException(final FeignException e,
            final HttpServletResponse response) {
        response.setStatus(e.status());
        return "Patient not found with this id ";
    }

}
