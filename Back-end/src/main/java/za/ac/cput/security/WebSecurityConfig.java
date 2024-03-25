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
        http.csrf().disable()
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                // Public endpoints (no authentication required)
                .antMatchers(HttpMethod.POST, "/user/login", "/user/register")
                .permitAll()
                // User endpoints (authenticated users only)
                .antMatchers(HttpMethod.GET, "/user/profile", "/user/verify/{token}/account",  "/user/read/**")
                .hasAnyRole("USER","MANAGER", "ADMIN", "SYSADMIN") // Restrict based on your needs

                // Customer endpoints (access based on roles)
                .antMatchers(HttpMethod.GET, "/customer/count", "/customer/all")
                .hasAnyRole("MANAGER", "ADMIN", "SYSADMIN")
                .antMatchers(HttpMethod.GET, "/customer/read/**")
                .hasAnyRole("MANAGER") // Restrict based on your needs

                // Prescription, Medication, Invoice, Inventory endpoints (access based on roles)
                .antMatchers(HttpMethod.GET, "/prescription/all", "/prescription/read/**",
                        "/medication/all", "/medication/read/**",
                        "/invoice/count", "/invoice/total-billed-amount",
                        "/invoice/all", "/invoice/read/**",
                        "/inventory/medications", "/inventory/all",
                        "/inventory/read/**")
                .hasAnyRole("MANAGER", "ADMIN", "SYSADMIN")

                // Update endpoints (access based on roles)
                .antMatchers(HttpMethod.PUT, "/prescription/update", "/medication/update",
                        "/invoice/update", "/inventory/update", "/customer/update")
                .hasAnyRole("MANAGER", "ADMIN", "SYSADMIN")

                // Delete endpoints (access based on roles)
                .antMatchers(HttpMethod.DELETE, "/prescription/delete/**", "/medication/delete/**",
                        "/invoice/delete/**", "/inventory/delete/**",
                        "/customer/delete/**")
                .hasAnyRole("ADMIN", "SYSADMIN")

                // Role endpoints (restricted access)
                .antMatchers(HttpMethod.POST, "/roles/create").hasRole("SYSADMIN")
                .antMatchers(HttpMethod.GET, "/roles/getRolesByUserId/**",
                        "/roles/getRoleByUserEmail")
                .hasAnyRole("ADMIN", "SYSADMIN")
                .antMatchers(HttpMethod.GET, "/roles/read/**").hasRole("SYSADMIN")
                .antMatchers(HttpMethod.PUT, "/roles/updateUserRole")
                .hasAnyRole("ADMIN", "SYSADMIN")
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
