package za.ac.cput.security;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import za.ac.cput.service.implementation.UserDetailsServiceImpl;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2024/01/21
 * @Time : 16:01
 **/

@AllArgsConstructor
@EnableWebSecurity
@Configuration
@ComponentScan
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()  // Enable CORS and disable CSRF
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                // Public endpoints (no authentication required)
                .antMatchers(HttpMethod.POST, "/user/image/**", "/user/login", "/user/register").permitAll()
                .antMatchers(HttpMethod.DELETE, "/user/delete/image/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/user/update/**").permitAll()
                .antMatchers(HttpMethod.GET, "/user/count","/user/image/**").hasAnyRole("USER", "MANAGER", "ADMIN", "SYSADMIN")
                .antMatchers(HttpMethod.GET, "/user/verify/{token}/account").permitAll()
                .antMatchers(HttpMethod.GET, "/user-events/read/**", "/user-events/all", "/user-events/user/{userId}",
                        "/user-events/delete/**", "/user-events/update/**", "/user-events/create").permitAll()

                // User endpoints (authenticated users only)
                .antMatchers(HttpMethod.GET, "/user/read/**").hasAnyRole("USER", "MANAGER", "ADMIN", "SYSADMIN")
                .antMatchers(HttpMethod.POST, "/user/password-reset/forgot", "/user/password-reset/reset", "/user/change-password").permitAll()
                .antMatchers(HttpMethod.GET, "/user/current-password").hasAnyRole("USER", "MANAGER", "ADMIN", "SYSADMIN")
                // Customer endpoints (access based on roles)
                .antMatchers(HttpMethod.GET, "/customer/count", "/customer/all").hasAnyRole("USER", "MANAGER", "ADMIN", "SYSADMIN")
                .antMatchers(HttpMethod.GET, "/customer/read/**").hasAnyRole("USER", "MANAGER", "ADMIN", "SYSADMIN")
                // Prescription, Medication, Invoice, Inventory endpoints (access based on roles)
                .antMatchers(HttpMethod.GET, "/prescription/count", "/medication/count", "/prescription/all", "/prescription/read/**", "/medication/all", "/medication/read/**", "/invoice/count", "/invoice/total-billed-amount", "/invoice/all", "/invoice/read/**", "/inventory/medications", "/inventory/all", "/inventory/read/**").hasAnyRole("MANAGER", "ADMIN", "SYSADMIN")
                // Update endpoints (access based on roles)
                .antMatchers(HttpMethod.PUT, "/user/update/**", "/prescription/update", "/medication/update", "/invoice/update", "/inventory/update", "/customer/update").hasAnyRole("MANAGER", "ADMIN", "SYSADMIN")
                // Delete endpoints (access based on roles)
                .antMatchers(HttpMethod.DELETE, "/prescription/delete/**", "/medication/delete/**", "/invoice/delete/**", "/inventory/delete/**", "/customer/delete/**").hasAnyRole("ADMIN", "SYSADMIN")
                // Role endpoints (restricted access)
                .antMatchers(HttpMethod.POST, "/roles/create").hasRole("SYSADMIN")
                .antMatchers(HttpMethod.GET, "/roles/getRolesByUserId/**", "/roles/getRoleByUserEmail",  "/roles/list").hasAnyRole("ADMIN", "SYSADMIN")
                .antMatchers(HttpMethod.GET, "/roles/read/**").hasRole("SYSADMIN")
                .antMatchers(HttpMethod.PUT, "/roles/updateUserRole").hasAnyRole("ADMIN", "SYSADMIN")
                .and()
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                        .invalidSessionUrl("/logout?expired")
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                )
                .logout(logout -> logout.deleteCookies("SESSION-ID").invalidateHttpSession(true));
    }


    @Override @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
