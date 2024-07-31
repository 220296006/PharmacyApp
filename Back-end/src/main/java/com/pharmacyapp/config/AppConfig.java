package com.pharmacyapp.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

/**
 * @author : Thabiso Matsaba
 * @Project : Back-end
 * @Date : 2024/06/30
 * @Time : 16:26
 **/
@Configuration
public class AppConfig {
    public static final Dotenv dotenv = Dotenv.load();

    // Database Configuration
    public static final String DB_URL = dotenv.get("DB_URL");
    public static final String DB_USERNAME = dotenv.get("DB_USERNAME");
    public static final String DB_PASSWORD = dotenv.get("DB_PASSWORD");

    // Email Configuration
    public static final String EMAIL_HOST = dotenv.get("EMAIL_HOST_NAME");
    public static final int EMAIL_PORT = Integer.parseInt(Objects.requireNonNull(dotenv.get("EMAIL_PORT")));
    public static final String EMAIL_USER_NAME = dotenv.get("EMAIL_USER_NAME");
    public static final String EMAIL_PASSWORD = dotenv.get("EMAIL_PASSWORD");

    // Application Configuration
    public static final String VERIFY_EMAIL_HOST = dotenv.get("VERIFY_EMAIL_HOST");
    public static final String JWT_SECRET = dotenv.get("JWT_SECRET");
    public static final int SERVER_PORT = Integer.parseInt(Objects.requireNonNull(dotenv.get("SERVER_PORT")));

    // Elasticsearch Configuration
    public static final String ELASTICSEARCH_HOST = dotenv.get("ELASTICSEARCH_HOST");
    public static final int ELASTICSEARCH_PORT = Integer.parseInt(Objects.requireNonNull(dotenv.get("ELASTICSEARCH_PORT")));
    public static final String ELASTICSEARCH_USERNAME = dotenv.get("ELASTICSEARCH_USERNAME");
}
