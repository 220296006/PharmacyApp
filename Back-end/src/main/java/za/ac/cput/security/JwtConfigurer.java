package za.ac.cput.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import za.ac.cput.utils.JwtTokenProvider;
/**
 * @author : Thabiso Matsaba
 * @Project : Back-end
 * @Date : 2024/01/24
 * @Time : 23:46
 **/
@AllArgsConstructor
@Configuration
public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void configure(HttpSecurity http) {
        JwtRequestFilter customFilter = new JwtRequestFilter(jwtTokenProvider, null);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
