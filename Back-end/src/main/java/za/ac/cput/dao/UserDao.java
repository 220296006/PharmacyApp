package za.ac.cput.dao;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
public class UserDao {
  private final static List<UserDetails> APPLICATION_USERS = List.of(
            new User(
                    "thabisomatsaba96@gmail.com",
                    "Shaun@96",
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"))
            ),
            new User(
                    "220296006@mycput.ac.za",
                    "Shaun@1234",
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
            )
    );

  public UserDetails findUserByEmail(String email){
      return  APPLICATION_USERS
                .stream()
                .filter(u -> u.getUsername().equals(email))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("No user was found"));
  }


}
