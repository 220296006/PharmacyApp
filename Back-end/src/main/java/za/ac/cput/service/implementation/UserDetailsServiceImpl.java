package za.ac.cput.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import za.ac.cput.exception.ApiException;
import za.ac.cput.model.User;
import za.ac.cput.query.UserQuery;
import za.ac.cput.rowmapper.UserRowMapper;

import java.util.Map;

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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            User user = jdbc.queryForObject(UserQuery.FETCH_USER_BY_EMAIL_QUERY, Map.of("mail", email), new UserRowMapper());
            if (user == null) {
                throw new UsernameNotFoundException("User not found with email: " + email);
            }
            return user;
        } catch (EmptyResultDataAccessException exception) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("Error loading user by email");
        }
    }
}
