package za.ac.cput.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * @author : Thabiso Matsaba
 * @Project : Back-end
 * @Date : 2024/01/21
 * @Time : 16:01
 **/
@Configuration
@AllArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = (AuthenticationManagerBuilder) http.getSharedObjects();
        authenticationManagerBuilder.authenticationProvider(null);
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers(POST, "user/create/**")
                .permitAll().antMatchers("/**")
                .authenticated()
                .anyRequest()
                .hasAnyRole("ROLE_USER", "ROLE_ADMIN", "ROLE_SYSADMIN", "ROLE_MANAGER")
                .and().httpBasic(Customizer.withDefaults())
                .sessionManagement()
                .sessionCreationPolicy(STATELESS);
        return http.build();
    }


}