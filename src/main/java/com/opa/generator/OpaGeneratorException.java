package com.opa.generator;

public class OpaGeneratorException extends RuntimeException {
    public OpaGeneratorException() {
    }

    public OpaGeneratorException(String message) {
        super(message);
    }

    public OpaGeneratorException(String message, Throwable cause) {
        super(message, cause);
    }

    public OpaGeneratorException(Throwable cause) {
        super(cause);
    }

    public OpaGeneratorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
