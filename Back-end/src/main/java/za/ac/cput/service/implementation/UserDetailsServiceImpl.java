package za.ac.cput.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import za.ac.cput.exception.ApiException;
import za.ac.cput.model.Role;
import za.ac.cput.model.User;
import za.ac.cput.repository.RoleRepository;

import java.util.Arrays;
import java.util.HashSet;
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
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
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
            // Fetch roles and permissions for the user
            Set<Role> roles = user.getRoles();
            roles.forEach(role -> {
                Set<String> permissions = getPermissionsForRole(role.getName());
                role.setPermissions(permissions); // Set permissions for the role
            });
            log.info("User found: {}", user);
            return user;
        } catch (DataAccessException exception) {
            log.error("Error loading user by email: {}", email, exception);
            throw new UsernameNotFoundException("User not found with email: " + email);
        } catch (Exception exception) {
            log.error("Unexpected error loading user by email: {}", email, exception);
            throw new ApiException("An unexpected error occurred");
        }
    }
    private Set<String> getPermissionsForRole(String roleName) {
        Set<String> permissions = new HashSet<>();
        permissions.add("READ:USER");
        permissions.add("READ:CUSTOMER");
        switch (roleName) {
            case "ROLE_ADMIN", "ROLE_MANAGER":
                permissions.add("READ:USER");
                permissions.add("READ:CUSTOMER");
                permissions.add("CREATE:USER");
                permissions.add("DELETE:USER");
                permissions.add("DELETE:CUSTOMER");
                permissions.add("UPDATE:USER");
                permissions.add("UPDATE:CUSTOMER");
                break;
            case "ROLE_SYSADMIN":
                permissions.add("READ:USER");
                permissions.add("READ:CUSTOMER");
                permissions.add("CREATE:USER");
                permissions.add("CREATE:CUSTOMER");
                permissions.add("DELETE:USER");
                permissions.add("DELETE:CUSTOMER");
                permissions.add("UPDATE:USER");
                permissions.add("UPDATE:CUSTOMER");
                break;
            case "ROLE_USER":
                permissions.add("READ:USER"); // Allow user to see their own profile information
                permissions.add("READ:CUSTOMER");
                break;
        }
        // Logging statements to verify permissions
        log.info("Permissions for role {}: {}", roleName, permissions);
        return permissions;
    }
    private Set<GrantedAuthority> getAuthorities(Set<Role> roles) { // Use Set instead of List
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
    }
}
