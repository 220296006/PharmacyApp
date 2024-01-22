package za.ac.cput.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2024/01/21
 * @Time : 16:01
 **/
@AllArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors().and() // Enable CORS
                .csrf().disable() // Disable CSRF for simplicity (you may enable it based on your requirements)
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll() // Allow pre-flight requests
                .anyRequest().permitAll(); // Allow all other requests

//        http.csrf().disable()
//                .authorizeRequests()
//                .antMatchers(POST, "user/create/**").permitAll()
//                .antMatchers("/**").authenticated()
//                .anyRequest().hasAnyRole("ROLE_USER", "ROLE_ADMIN", "ROLE_SYSADMIN", "ROLE_MANAGER")
//                .and()
//                .httpBasic(Customizer.withDefaults())
//                .sessionManagement().sessionCreationPolicy(STATELESS);

        return http.build();
    }
}
