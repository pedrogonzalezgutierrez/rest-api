package com.kiesoft.customer.exception;

public class PersistenceProblemException extends RuntimeException {

    public PersistenceProblemException(String message, Throwable cause) {
        super(message, cause);
    }

}
