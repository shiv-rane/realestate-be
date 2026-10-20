package com.realestate.app.exception;

/**
 * Thrown when a user submits an invalid or expired OTP.
 */
public class InvalidOtpException extends RuntimeException {

    public InvalidOtpException(String message) {
        super(message);
    }
}
