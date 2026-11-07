package com.realestate.app.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Spring Security configuration for the Real Estate Marketplace API.
 *
 * Strategy:
 * - Stateless sessions (JWT-based, no HttpSession)
 * - CSRF disabled (stateless REST API)
 * - Public: /api/v1/auth/** and all Swagger/OpenAPI endpoints
 * - ADMIN role: /api/v1/admin/**
 * - Authenticated: /api/v1/developers/**
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity        // Enables @PreAuthorize, @PostAuthorize at method level
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    /** Swagger / OpenAPI paths that must remain public */
//    private static final String[] SWAGGER_WHITELIST = {
//        "/swagger-ui/**",
//        "/swagger-ui.html",
//        "/api-docs/**",
//        "/api-docs.yaml",
//        "/v3/api-docs/**"
//    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 0. Enable CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // 1. Disable CSRF — stateless REST API does not need it
            .csrf(AbstractHttpConfigurer::disable)

            // 2. Stateless session — no HttpSession created or used
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // 3. Custom 401 entry point for unauthenticated access
            .exceptionHandling(ex ->
                ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))

            // 4. Authorization rules
            .authorizeHttpRequests(auth -> auth
                // Allow CORS preflight OPTIONS requests
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // Public auth endpoints
                .requestMatchers("/api/v1/auth/**").permitAll()
                // Public developer listing — no auth required
                .requestMatchers("/api/v1/developers/**").permitAll()
                // Swagger / OpenAPI docs
//                .requestMatchers(SWAGGER_WHITELIST).permitAll()
                // Actuator health (optional, useful for container probes)
                .requestMatchers("/actuator/health").permitAll()
                // Admin endpoints — ADMIN role only (method-level @PreAuthorize also enforced)
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                // Everything else also requires authentication (fail-safe default)
                .anyRequest().authenticated()
            )

            // 5. Inject JWT filter before the default username/password filter
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:5173",
                "http://localhost:8080",
                "https://bhoomiisetu.in",
                "https://realestate-fe-topaz.vercel.app",
                "https://realestate-fe-topaz.vercel.app/"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
