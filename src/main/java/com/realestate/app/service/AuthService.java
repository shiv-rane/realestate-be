package com.realestate.app.service;

import com.realestate.app.dto.auth.AuthResponse;
import com.realestate.app.dto.auth.SendOtpRequest;
import com.realestate.app.dto.auth.SendOtpResponse;
import com.realestate.app.dto.auth.VerifyOtpRequest;

/**
 * Service contract for OTP-based authentication.
 */
public interface AuthService {

    /**
     * Generates and sends an OTP to the given phone number.
     * Creates a placeholder User if one does not already exist.
     *
     * @param request contains phoneNumber and role
     * @return a response confirming dispatch
     */
    SendOtpResponse sendOtp(SendOtpRequest request);

    /**
     * Verifies the OTP for the given phone number.
     * On success, marks the user as phone-verified and returns a JWT.
     *
     * @param request contains phoneNumber and otp
     * @return JWT token and user metadata
     */
    AuthResponse verifyOtp(VerifyOtpRequest request);
}
