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
import za.ac.cput.repository.implementation.RoleRepositoryImp;
import za.ac.cput.rowmapper.UserRowMapper;

import java.util.List;
import java.util.Map;
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
    private final RoleRepositoryImp roleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (email == null) { // Check for null email
            log.warn("Email is null during user loading.");
            throw new UsernameNotFoundException("Email cannot be null");
        }

        try {
            User user = jdbc.queryForObject(UserQuery.FETCH_USER_BY_EMAIL_QUERY,
                    Map.of("email", email), new UserRowMapper());
            if (user == null) {
                throw new UsernameNotFoundException("User not found with email: " + email);
            }
            List<Role> roles = roleRepository.getRolesByUserId(user.getId());
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(), user.getPassword(), user.isEnabled(), true,
                    true, user.isNotLocked(), getAuthorities(roles)
            );
        } catch (DataAccessException exception) { // Catch specific database access exception
            log.error("Error loading user by email: " + email, exception);
            throw new UsernameNotFoundException("User not found with email: " + email);
        } catch (Exception exception) { // Catch unexpected exceptions
            log.error("Unexpected error loading user by email: " + email, exception);
            throw new ApiException("An unexpected error occurred");
        }
    }

    private List<SimpleGrantedAuthority> getAuthorities(List<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_USER" + role.getName()))
                .collect(Collectors.toList());
    }
}
