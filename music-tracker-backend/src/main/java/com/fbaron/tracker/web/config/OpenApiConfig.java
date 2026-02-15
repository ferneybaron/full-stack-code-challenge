package com.fbaron.tracker.web.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${openapi.local-url:http://localhost:8080}")
    private String localUrl;

    // dev, prod URLs

    @Bean
    public OpenAPI openAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Music Tracker App Backend API")
                        .description("REST API for tracking music from a streaming provider backend. " +
                                "This API provides endpoints for adding tracks metadata, cover images and related operations.")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Ferney Estupiñán Barón")
                                .email("ferney.estupinanb@gmail.com")
                                .url("https://www.fbaron.com"))
                        .license(new License()
                                .name("MIT")))
                .servers(List.of(
                        new Server()
                                .url(localUrl + "/tracker")
                                .description("Local development server")
                ))
                .components(new Components()
                        .addSecuritySchemes("basicAuth",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("basic")
                                .description("HTTP Basic authentication (username and password)")))
                .addSecurityItem(new SecurityRequirement().addList("basicAuth"));
    }

}
