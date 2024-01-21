package za.ac.cput.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2024/01/21
 * @Time : 21:35
 **/

@Configuration
public class SecurityConfig {
    private static final int STRENGTH = 12;

    @Bean
    public BCryptPasswordEncoder customPasswordEncoder() {
        return new BCryptPasswordEncoder(STRENGTH);
    }
}

