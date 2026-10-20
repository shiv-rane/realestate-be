package com.realestate.app.service.impl;

import com.realestate.app.dto.auth.AuthResponse;
import com.realestate.app.dto.auth.SendOtpRequest;
import com.realestate.app.dto.auth.SendOtpResponse;
import com.realestate.app.dto.auth.VerifyOtpRequest;
import com.realestate.app.entity.User;
import com.realestate.app.exception.EntityNotFoundException;
import com.realestate.app.exception.InvalidOtpException;
import com.realestate.app.repository.UserRepository;
import com.realestate.app.security.JwtTokenProvider;
import com.realestate.app.service.AuthService;
import com.realestate.app.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Implementation of {@link AuthService}.
 * Handles email OTP generation, storage, validation, and JWT issuance.
 * Passwordless — the OTP proves ownership of the email address.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${app.otp.expiry-minutes:10}")
    private int otpExpiryMinutes;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    // =========================================================
    // Send OTP
    // =========================================================

    @Override
    @Transactional
    public SendOtpResponse sendOtp(SendOtpRequest request) {
        String email = request.email();

        // Guard: DEVELOPER accounts cannot self-register
        if (request.role() == com.realestate.app.entity.enums.UserRole.DEVELOPER) {
            throw new com.realestate.app.exception.BusinessRuleException(
                "DEVELOPER accounts are managed by admins and cannot self-register.");
        }

        // Find existing user or create a new placeholder with name + role
        User user = userRepository.findByEmail(email)
            .orElseGet(() -> createPlaceholderUser(request));

        // Generate a cryptographically secure 6-digit OTP
        String otp = generateOtp();

        // Persist OTP and expiry to the user record
        user.setOtpCode(otp);
        user.setOtpExpiry(Instant.now().plus(otpExpiryMinutes, ChronoUnit.MINUTES));
        userRepository.save(user);

        // Delegate to EmailService (console mock in Phase 1)
        emailService.sendOtp(email, otp);

        log.info("OTP dispatched to email: {}", email);
        return new SendOtpResponse(
            "OTP sent successfully to " + email + ". Valid for " + otpExpiryMinutes + " minutes.",
            email
        );
    }

    // =========================================================
    // Verify OTP
    // =========================================================

    @Override
    @Transactional
    public AuthResponse verifyOtp(VerifyOtpRequest request) {
        String email = request.email();

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException(
                "No account found for email: " + email + ". Please send an OTP first."));

        // Guard: OTP must exist
        if (user.getOtpCode() == null || user.getOtpExpiry() == null) {
            throw new InvalidOtpException("No OTP was generated for this email. Please request a new OTP.");
        }

        // Guard: OTP must not be expired
        if (Instant.now().isAfter(user.getOtpExpiry())) {
            invalidateOtp(user);
            throw new InvalidOtpException("OTP has expired. Please request a new OTP.");
        }

        // Guard: OTP must match
        if (!user.getOtpCode().equals(request.otp())) {
            throw new InvalidOtpException("Invalid OTP. Please try again.");
        }

        // Mark email as verified and clear the OTP
        user.setIsEmailVerified(true);
        invalidateOtp(user);
        userRepository.save(user);

        log.info("Email verified for userId: {}", user.getId());

        // Issue JWT
        String token = jwtTokenProvider.generateToken(user.getId(), user.getRole().name());

        return new AuthResponse(
            token,
            jwtTokenProvider.getExpirationMs(),
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole()
        );
    }

    // =========================================================
    // Helpers
    // =========================================================

    private User createPlaceholderUser(SendOtpRequest request) {
        log.info("Creating new user — email: {}, name: {}, role: {}",
                request.email(), request.name(), request.role());
        User newUser = User.builder()
            .id(UUID.randomUUID())
            .name(request.name())
            .email(request.email())
            .role(request.role())
            .isEmailVerified(false)
            .build();
        return userRepository.save(newUser);
    }

    private String generateOtp() {
        int otp = 100_000 + SECURE_RANDOM.nextInt(900_000); // range: 100000–999999
        return String.valueOf(otp);
    }

    private void invalidateOtp(User user) {
        user.setOtpCode(null);
        user.setOtpExpiry(null);
    }
}
