package com.pharmacyapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableAsync
@ComponentScan
@Slf4j
@EnableJdbcHttpSession
@ComponentScan(basePackages = "com.pharmacyapp")
public class PharmacyApp {

    @Value("${application.cors.origins:*}")
    private List<String> allowedOrigins;

    public static void main(String[] args) {SpringApplication.run(PharmacyApp.class, args);}

    @Bean
    public BCryptPasswordEncoder customPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        //config.setAllowCredentials(true);
        config.setAllowedOrigins(allowedOrigins);
        config.setAllowedHeaders(List.of("*")); // not recommended for production
        config.setExposedHeaders(List.of("*")); // not recommended for production
        config.setAllowedMethods(List.of("*")); // not recommended for production
        source.registerCorsConfiguration("/**", config);
        // Log the CORS configuration
        log.info("CORS configuration: {}", config);

        return new CorsFilter(source);
    }

}