package com.demoaws.textract.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//Configuration Class for Swagger UI
@Configuration
@SuppressWarnings("unused")
public class OpenApiConfig {

    // Configuring custom open API documentation
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Security Bootcamp 2025")
                        .version("1.0")
                        .description("""
                                API Documentation for Spring boot security of batch 2025
                                
                                **Developers
                                -Raymart Sarmiento
                                
                                """)
                        .contact(new Contact()
                                .name("Raymart Sarmiento")
                                .email("raymart.srbootcamp2025@gmail.com")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .name("Authorization")
                                .description("Bearer Token")));

    }
}