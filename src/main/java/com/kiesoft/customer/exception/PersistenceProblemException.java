package com.kiesoft.customer.exception;

public class PersistenceProblemException extends RuntimeException {

    private final Exception exception;

    public PersistenceProblemException(final Exception exception) {
        super();
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }

}
