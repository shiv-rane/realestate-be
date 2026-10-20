package com.realestate.app.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI 3.0 configuration for Swagger UI.
 * Defines the Bearer JWT security scheme and global API metadata.
 *
 * Access Swagger UI at: http://localhost:8080/swagger-ui.html
 */
@Configuration
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "Enter the JWT token obtained from POST /api/v1/auth/verify-otp"
)
public class OpenApiConfig {

    @Bean
    public OpenAPI realEstateOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Real Estate Marketplace API")
                .description("""
                    3-Sided Real Estate Marketplace connecting **Land Owners**, **Verified Agents**, and **Developers**.
                    
                    **Phase 1** covers:
                    - OTP-based authentication (send OTP, verify OTP, issue JWT)
                    - Developer onboarding with RERA verification
                    
                    **Phase 2** covers:
                    - Admin dashboard: platform stats (total / pending / approved / rejected developers)
                    - Admin can list all developers and filter by verification status
                    - Admin can update a developer's verification status, total projects done, and project details
                    
                    **Authentication**: Use `POST /api/v1/auth/verify-otp` to get a JWT token,
                    then click **Authorize** and paste: `<your-token>`.
                    
                    **Admin role**: Set `role = ADMIN` when calling `POST /api/v1/auth/send-otp` to obtain an admin token.
                    """)
                .version("1.0.0")
                .contact(new Contact()
                    .name("Platform Team")
                    .email("dev@realestate.com"))
                .license(new License()
                    .name("Proprietary")
                    .url("https://realestate.com/terms"))
            )
            .servers(List.of(
                new Server().url("http://localhost:8080").description("Local Development"),
                new Server().url("https://api.realestate.com").description("Production")
            ));
    }
}
