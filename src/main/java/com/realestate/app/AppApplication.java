package com.realestate.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Real Estate Marketplace backend API.
 *
 * Phase 1: OTP-based authentication + Developer onboarding (RERA verification).
 *
 * Swagger UI: http://localhost:8080/swagger-ui.html
 * API Docs:   http://localhost:8080/api-docs
 */
@SpringBootApplication
public class AppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }
}
