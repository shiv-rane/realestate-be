package com.realestate.app.service.impl;

import com.realestate.app.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Console-based mock implementation of {@link EmailService}.
 *
 * PHASE 1 ONLY — Prints the OTP to the application log instead of sending a real email.
 * Replace this with a real email provider (SendGrid, AWS SES, JavaMailSender) before production.
 */
@Service
@Slf4j
public class ConsoleEmailServiceImpl implements EmailService {

    @Override
    public void sendOtp(String email, String otp) {
        // ============================================================
        // MOCK EMAIL — Replace with real provider in production
        // ============================================================
        log.info("======================================================");
        log.info("  [EMAIL MOCK] Sending OTP to: {}", email);
        log.info("  [EMAIL MOCK] OTP Code       : {}", otp);
        log.info("  [EMAIL MOCK] Subject        : Your verification code");
        log.info("======================================================");
    }
}
