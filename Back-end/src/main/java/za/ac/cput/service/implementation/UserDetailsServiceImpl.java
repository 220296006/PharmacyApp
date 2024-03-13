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

import java.util.Arrays;
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
                    (rs, rowNum) -> {
                        User u = new User();
                        u.setId(rs.getLong("id"));
                        u.setFirstName(rs.getString("first_name"));
                        u.setMiddleName(rs.getString("middle_name"));
                        u.setLastName(rs.getString("last_name"));
                        u.setEmail(rs.getString("email"));
                        u.setPassword(rs.getString("password"));
                        u.setPhone(rs.getString("phone"));
                        u.setAddress(rs.getString("address"));
                        u.setImageUrl(rs.getString("image_url"));
                        u.setEnabled(rs.getBoolean("enabled"));
                        u.setNotLocked(rs.getBoolean("not_locked"));
                        u.setUsingMfa(rs.getBoolean("using_mfa"));
                        u.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                        // Map roles to Role objects or create new Role objects with role names
                        u.setRoles(Arrays.stream(rs.getString("roles").split(","))
                                .map(roleName -> new Role(null, roleName, null))
                                .collect(Collectors.toSet()));
                        return u;
                    }
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
                    true, // Account not locked
                    getAuthorities(user.getRoles()) // Authorities (roles)
            );
        } catch (DataAccessException exception) {
            log.error("Error loading user by email: " + email, exception);
            throw new UsernameNotFoundException("User not found with email: " + email);
        } catch (Exception exception) {
            log.error("Unexpected error loading user by email: " + email, exception);
            throw new ApiException("An unexpected error occurred");
        }
    }

    private Set<GrantedAuthority> getAuthorities(Set<Role> roles) { // Use Set instead of List
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
    }


}
