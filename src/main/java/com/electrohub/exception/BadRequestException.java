package com.electrohub.exception;

/**
 * Thrown when an invalid request is supplied.
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
