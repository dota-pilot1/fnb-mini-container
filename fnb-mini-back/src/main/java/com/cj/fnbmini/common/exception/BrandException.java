package com.cj.fnbmini.common.exception;

public class BrandException extends RuntimeException {

    public BrandException(String message) {
        super(message);
    }

    public BrandException(String message, Throwable cause) {
        super(message, cause);
    }
}
