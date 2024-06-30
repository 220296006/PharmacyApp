package com.pharmacyapp.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Thabiso Matsaba
 * @Project : Back-end
 * @Date : 2024/06/30
 * @Time : 11:59
 **/
@Configuration
public class DotenvConfig {

    @Bean
    public Dotenv dotenv() {
        return Dotenv.configure().ignoreIfMissing().load();
    }
}