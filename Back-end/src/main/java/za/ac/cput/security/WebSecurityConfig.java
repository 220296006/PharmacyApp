package za.ac.cput.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2024/01/21
 * @Time : 16:01
 **/

@AllArgsConstructor
@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    private final UserDetailsService userDetailsService;
    private final JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                //Public Access
                .antMatchers(POST, "/user/register", "/customer/create", "/prescription/create", "/medication/create", "/invoice/create", "/inventory/create**").permitAll()
                .antMatchers(GET, "/user/all", "/user/read/**", "/prescription/all", "/prescription/read/**", "/medication/all", "/medication/read/**", "/invoice/count",
                        "/invoice/total-billed-amount", "/invoice/all", "/invoice/read/**", "/inventory/medications", "/inventory/all", "/inventory/read/**", "/customer/count", "/customer/all", "/customer/read/**").permitAll()
                //ADMIN, MANAGER, SYSADMIN Access
                .antMatchers(PUT, "/prescription/update", "/medication/update", "/invoice/update", "/inventory/update", "/customer/update").permitAll()
                //.hasAnyRole("ROLE_ADMIN", "ROLE_MANAGER", "ROLE_SYSADMIN")
                .antMatchers(DELETE, "/prescription/delete/**", "/medication/delete/**", "/invoice/delete/**", "/inventory/delete/**",
                        "/customer/delete/**").permitAll()
                //.hasAnyRole("ROLE_ADMIN", "ROLE_MANAGER", "ROLE_SYSADMIN")
                .antMatchers("/**").authenticated()
                .anyRequest()
                .hasAnyRole("ROLE_USER", "ROLE_ADMIN", "ROLE_SYSADMIN", "ROLE_MANAGER")
                .and()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement()
                .sessionCreationPolicy(STATELESS);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
