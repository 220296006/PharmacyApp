package za.ac.cput.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import za.ac.cput.exception.ApiException;
import za.ac.cput.model.Role;
import za.ac.cput.model.User;
import za.ac.cput.repository.RoleRepository;
import za.ac.cput.repository.UserRepository;
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
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private final RoleRepository<Role> roleRepository;
    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (email == null) {
            log.warn("Email is null during user loading.");
            throw new UsernameNotFoundException("Email cannot be null");
        }

        try {
            log.info("Fetching user details for email: {}", email);
            User user = jdbc.queryForObject(
                    "SELECT u.*, GROUP_CONCAT(r.name) as roles " +
                            "FROM Users u " +
                            "JOIN UserRoles ur ON u.id = ur.user_id " +
                            "JOIN Roles r ON ur.role_id = r.id " +
                            "WHERE u.email = :email " +
                            "GROUP BY u.id",
                    Map.of("email", email),
                    new UserRowMapper()
            );

            if (user == null) {
                throw new UsernameNotFoundException("User not found with email: " + email);
            }

            log.info("User found: {}", user);

            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(), // Username
                    user.getPassword(), // Password (already encoded)
                    user.isEnabled(), // Enabled status
                    true, // Account not expired
                    true, // Credentials not expired
                    user.isNotLocked(), // Account not locked
                    getAuthorities(roleRepository.getRolesByUserId(user.getId())) // Authorities (roles)
            );
        } catch (DataAccessException exception) {
            log.error("Error loading user by email: " + email, exception);
            throw new UsernameNotFoundException("User not found with email: " + email);
        } catch (Exception exception) {
            log.error("Unexpected error loading user by email: " + email, exception);
            throw new ApiException("An unexpected error occurred");
        }
    }
        private List<GrantedAuthority> getAuthorities (List < Role > roles) {
            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                    .collect(Collectors.toList());
        }


}
