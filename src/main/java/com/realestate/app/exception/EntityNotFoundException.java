package com.realestate.app.exception;

/**
 * Thrown when a requested entity (User, DeveloperProfile, etc.) cannot be found.
 */
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String entityName, Object identifier) {
        super(String.format("%s not found with identifier: %s", entityName, identifier));
    }

    public EntityNotFoundException(String message) {
        super(message);
    }
}
