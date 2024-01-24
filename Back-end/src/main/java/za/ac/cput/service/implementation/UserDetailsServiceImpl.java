package za.ac.cput.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import za.ac.cput.exception.ApiException;
import za.ac.cput.model.Role;
import za.ac.cput.model.User;
import za.ac.cput.query.UserQuery;
import za.ac.cput.rowmapper.UserRowMapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author : Thabiso Matsaba
 * @Project : Back-end
 * @Date : 2024/01/21
 * @Time : 21:47
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            User user = jdbc.queryForObject(UserQuery.FETCH_USER_BY_EMAIL_QUERY, Map.of("mail", email), new UserRowMapper());
            if (user == null) {
                throw new UsernameNotFoundException("User not found with email: " + email);
            }
            List<SimpleGrantedAuthority> authorities = getAuthorities(user.getRoles());
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
        } catch (EmptyResultDataAccessException exception) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        } catch (DataAccessException | ApiException exception) {
            log.error(exception.getMessage());
            throw new ApiException("Error loading user by email");
        }
    }

    private List<SimpleGrantedAuthority> getAuthorities(Set<Role> roles) {
        if (roles == null) {
            return Collections.emptyList();
        }
        return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toList());
    }
}
