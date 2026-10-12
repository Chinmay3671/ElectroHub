package com.electrohub.exception;

/**
 * Thrown when a requested resource (product or cart item) is not found.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
