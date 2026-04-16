package com.shubham.event_manager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI eventManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Event Management API")
                        .description("REST API for managing events — Phase 3")
                        .version("3.0.0")
                        .contact(new Contact()
                                .name("Your Name")
                                .email("your@email.com"))
                        .license(new License()
                                .name("MIT License")));
    }
}
