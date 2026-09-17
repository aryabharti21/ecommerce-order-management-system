package com.arya.ecommerce_order_management.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(buildInfo())
                .addSecurityItem(buildSecurityRequirement())
                .components(new Components()
                        .addSecuritySchemes(
                                "bearerAuth",
                                buildSecurityScheme()
                        ));
    }

    private Info buildInfo(){
        return new Info()
                .title("E-Commerce Order Management API")
                .description("""
                        REST API for E-Commerce Order Management System.
                        
                        Features:
                        - User registration and authentication (JWT)
                        - Product and category management
                        - Order placement and tracking
                        - Payment processing
                        - Inventory management
                        
                        Authentication:
                        1. Register at POST /api/v1/auth/register
                        2. Login at POST /api/v1/auth/login
                        3. Copy the token from response
                        4. Click Authorize button above
                        5. Enter: Bearer {your-token}
                        """)
                .version("1.0.0")
                .contact(new Contact()
                        .name("Arya Bharti")
                        .email("arya@gmail.com"))
                .license(new License()
                        .name("MIT License"));
    }

    private SecurityRequirement buildSecurityRequirement(){
        return new SecurityRequirement()
                .addList("bearerAuth");
    }

    private SecurityScheme buildSecurityScheme(){
        return new SecurityScheme()
                .name("bearerAuth")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .description("Enter JWT token.\n\n"
                        + "Get token from /api/v1/auth/login\n"
                        + "Format: Bearer {token}");
    }
}
