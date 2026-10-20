package com.realestate.app.service;

/**
 * Abstraction for email OTP delivery.
 * In Phase 1: console mock implementation (OTP printed to log).
 * In production: swap with SendGrid / AWS SES / JavaMailSender implementation.
 */
public interface EmailService {

    /**
     * Sends a 6-digit OTP to the given email address.
     *
     * @param email the destination email address
     * @param otp   the 6-digit OTP to send
     */
    void sendOtp(String email, String otp);
}
