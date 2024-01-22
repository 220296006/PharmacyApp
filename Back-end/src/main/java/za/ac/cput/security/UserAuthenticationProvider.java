package za.ac.cput.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author : Thabiso Matsaba
 * @Project : Back-end
 * @Date : 2024/01/22
 * @Time : 16:30
 **/
@Component
@RequiredArgsConstructor
public class UserAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        // You can add additional checks here if needed
        if(authentication.getCredentials() == null || userDetails.getPassword() == null ){
            throw new BadCredentialsException("Credentials  may not be null");
        }
        if(!passwordEncoder.matches((String) authentication.getCredentials(), userDetails.getPassword())){
            throw new BadCredentialsException("Invalid credentials");
        }
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        UserDetails loadedUser = userDetailsService.loadUserByUsername(username);

        if (loadedUser == null) {
            throw new UsernameNotFoundException("User not found");
        }

        if (!passwordEncoder.matches((String) authentication.getCredentials(), loadedUser.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return loadedUser;
    }
}