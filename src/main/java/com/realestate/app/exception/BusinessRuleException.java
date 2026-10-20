package com.realestate.app.exception;

/**
 * Thrown when a business rule is violated (e.g., duplicate RERA registration).
 */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
