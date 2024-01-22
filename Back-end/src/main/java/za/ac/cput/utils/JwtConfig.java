package za.ac.cput.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import za.ac.cput.security.JwtRequestFilter;

/**
 * @author : Thabiso Matsaba
 * @Project : Back-end
 * @Date : 2024/01/22
 * @Time : 18:26
 **/
@Configuration
public class JwtConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtConfig(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public JwtRequestFilter myJwtRequestFilter() {
        return new JwtRequestFilter(jwtUtil, userDetailsService);
    }

}