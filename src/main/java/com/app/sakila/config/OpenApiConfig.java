package com.app.sakila.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI sakilaOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Sakila Rental API")
                .description("REST API for Sakila DVD Rental Store")
                .version("1.0.0")
                .contact(new Contact()
                    .name("Brigitte Rodriguez")
                    .email("brigitte@sakila.app"))
                .license(new License()
                    .name("MIT")
                    .url("https://opensource.org/licenses/MIT")));
    }
}
