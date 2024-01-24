package za.ac.cput.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * @author : Thabiso Matsaba
 * @Project : Back-end
 * @Date : 2024/01/24
 * @Time : 23:48
 **/
public interface JwtConfig {
    void configure(HttpSecurity http);
}
