package com.realestate.app.config;

import com.realestate.app.entity.User;
import com.realestate.app.entity.enums.UserRole;
import com.realestate.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Initializes or promotes the designated administrator account on application startup.
 * Guarantees that bhoomiisetu@gmail.com always has the ADMIN role.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    @Value("${app.admin.email:bhoomiisetu@gmail.com}")
    private String adminEmail;

    @Override
    public void run(String... args) {
        String email = adminEmail.trim().toLowerCase();
        userRepository.findByEmail(email).ifPresentOrElse(
            user -> {
                if (user.getRole() != UserRole.ADMIN) {
                    log.info("Promoting user [{}] to ADMIN role", email);
                    user.setRole(UserRole.ADMIN);
                    userRepository.save(user);
                } else {
                    log.info("Admin account [{}] is confirmed with ADMIN role", email);
                }
            },
            () -> {
                log.info("Creating initial admin account for [{}]", email);
                User admin = User.builder()
                    .id(UUID.randomUUID())
                    .name("Bhoomi Setu Admin")
                    .email(email)
                    .role(UserRole.ADMIN)
                    .isEmailVerified(true)
                    .build();
                userRepository.save(admin);
                log.info("Admin account [{}] successfully created with ADMIN role", email);
            }
        );
    }
}

