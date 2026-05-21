package com.campusnavigator.Config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApplicationConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer(
            @Value("${app.cors.allowed-origins:http://localhost:3000}") String allowedOrigins,
            @Value("${app.cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS}") String allowedMethods,
            @Value("${app.cors.allow-credentials:true}") boolean allowCredentials) {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns(parseCsv(allowedOrigins))
                        .allowedMethods(parseCsv(allowedMethods))
                        .allowedHeaders("*")
                        .allowCredentials(allowCredentials);
            }
        };
    }

    private String[] parseCsv(String csv) {
        return Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(value -> !value.isEmpty())
                .toArray(String[]::new);
    }
}
