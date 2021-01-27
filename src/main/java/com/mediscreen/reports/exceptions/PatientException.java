package com.mediscreen.reports.exceptions;

public class PatientException extends Exception {

    private static final long serialVersionUID = 1L;

    public PatientException(String message) {
        super(message);
    }

    public PatientException(String message, Throwable cause) {
        super(message, cause);
    }
}
