package za.ac.cput.security;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import za.ac.cput.service.implementation.UserDetailsServiceImpl;
import za.ac.cput.utils.JwtTokenProvider;

import javax.sql.DataSource;

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

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("SELECT email, password, enabled FROM Users WHERE email = ?")
                .authoritiesByUsernameQuery("SELECT email, name FROM UserRoles WHERE email = ?");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/user/login", "/user/register", "/customer/create", "/prescription/create", "/medication/create", "/invoice/create", "/inventory/create").permitAll()
                .antMatchers(HttpMethod.GET,
                        "/user/all", "/user/read/**", "/prescription/all", "/prescription/read/**",
                        "/medication/all", "/medication/read/**", "/invoice/count", "/invoice/total-billed-amount",
                        "/invoice/all", "/invoice/read/**", "/inventory/medications", "/inventory/all", "/inventory/read/**",
                        "/customer/count", "/customer/all", "/customer/read/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/prescription/update", "/medication/update", "/invoice/update", "/inventory/update", "/customer/update").hasAnyRole("ADMIN", "MANAGER", "SYSADMIN")
                .antMatchers(HttpMethod.DELETE, "/prescription/delete/**", "/medication/delete/**", "/invoice/delete/**", "/inventory/delete/**", "/customer/delete/**").hasAnyRole("ADMIN", "MANAGER", "SYSADMIN")
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider)); // Custom JwtConfigurer to handle JWT authentication
    }

}
