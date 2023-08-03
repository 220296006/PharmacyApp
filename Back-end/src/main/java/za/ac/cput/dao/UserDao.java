package za.ac.cput.dao;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/18
 * @Time : 21:55
 **/
@Repository
@Getter
@Setter
public class UserDao {

    private static final int STRENGTH = 12;

    private final static List<UserDetails>  APPLICATION_USERS = List.of(


        new User(
            "thabisomatsaba96@gmail.com",
            getPasswordEncoder().encode("Shaun@1234"), // Non-empty user password
            Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        ),
        new User(
            "220296006@mycput.ac.za",
            getPasswordEncoder().encode("Shaun@1234"), // Non-empty admin password
            Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"))
        )
    );

    public UserDetails findUserByEmail(String email) {
        return APPLICATION_USERS
            .stream()
            .filter(u -> u.getUsername().equals(email))
            .findFirst()
            .orElseThrow(() -> new UsernameNotFoundException("No user was found"));
    }

    private static PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(STRENGTH);
    }
}