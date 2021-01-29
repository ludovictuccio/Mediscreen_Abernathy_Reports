package com.mediscreen.reports.exceptions;

public class PatientException extends Exception {

    private static final long serialVersionUID = 1L;

    public PatientException(final String message) {
        super(message);
    }

    public PatientException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
