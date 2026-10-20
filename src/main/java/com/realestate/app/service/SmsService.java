package com.realestate.app.service;

/**
 * Abstraction for SMS sending.
 * In Phase 1: console mock implementation.
 * In production: swap with Twilio/MSG91/Fast2SMS provider implementation.
 */
public interface SmsService {

    /**
     * Sends an OTP message to the given phone number.
     *
     * @param phoneNumber the destination phone number in E.164 format
     * @param otp         the 6-digit OTP to send
     */
    void sendOtp(String phoneNumber, String otp);
}
