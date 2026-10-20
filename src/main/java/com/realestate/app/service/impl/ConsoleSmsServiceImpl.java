package com.realestate.app.service.impl;

import com.realestate.app.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Console-based mock implementation of {@link SmsService}.
 *
 * PHASE 1 ONLY — Prints the OTP to the application log.
 * Replace this with a real SMS provider (Twilio, MSG91, Fast2SMS) before going to production.
 */
@Service
@Slf4j
public class ConsoleSmsServiceImpl implements SmsService {

    @Override
    public void sendOtp(String phoneNumber, String otp) {
        // ============================================================
        // MOCK SMS — Replace with real provider in production
        // ============================================================
        log.info("======================================================");
        log.info("  [SMS MOCK] Sending OTP to phone: {}", phoneNumber);
        log.info("  [SMS MOCK] OTP Code: {}", otp);
        log.info("======================================================");
    }
}
