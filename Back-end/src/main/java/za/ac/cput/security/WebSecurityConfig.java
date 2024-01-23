package za.ac.cput.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.springframework.http.HttpMethod.*;

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
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                // Public routes
                .antMatchers("/user/register", "/customer/create", "/prescription/create", "/medication/create", "/invoice/create", "/inventory/create**").permitAll()

                // Read permissions for all roles
                .antMatchers(GET,
                        "/user/all", "/user/read/**", "/prescription/all", "/prescription/read/**",
                        "/medication/all", "/medication/read/**", "/invoice/count", "/invoice/total-billed-amount",
                        "/invoice/all", "/invoice/read/**", "/inventory/medications", "/inventory/all", "/inventory/read/**",
                        "/customer/count", "/customer/all", "/customer/read/**").permitAll()

                // ADMIN, MANAGER, SYSADMIN Access
                .antMatchers(PUT, "/prescription/update", "/medication/update", "/invoice/update", "/inventory/update", "/customer/update")
                .hasAnyRole("ADMIN", "MANAGER", "SYSADMIN")

                .antMatchers(DELETE, "/prescription/delete/**", "/medication/delete/**", "/invoice/delete/**", "/inventory/delete/**",
                        "/customer/delete/**")
                .hasAnyRole("ADMIN", "MANAGER", "SYSADMIN")

                // Additional antMatchers for specific permissions can be added here

                // Any other request must be authenticated
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll()
                .and()
                .logout().permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user").password(passwordEncoder().encode("userpassword")).roles("USER")
                .and()
                .withUser("admin").password(passwordEncoder().encode("adminpassword")).roles("ADMIN")
                .and()
                .withUser("manager").password(passwordEncoder().encode("managerpassword")).roles("MANAGER")
                .and()
                .withUser("sysadmin").password(passwordEncoder().encode("sysadminpassword")).roles("SYSADMIN");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
