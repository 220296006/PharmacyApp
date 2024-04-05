package za.ac.cput.repository.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import za.ac.cput.exception.ApiException;
import za.ac.cput.model.Role;
import za.ac.cput.model.User;
import za.ac.cput.repository.RoleRepository;
import za.ac.cput.repository.UserRepository;
import za.ac.cput.rowmapper.UserRowMapper;
import za.ac.cput.service.EmailService;

import java.util.*;

import static za.ac.cput.enumeration.RoleType.*;
import static za.ac.cput.query.ConfirmationQuery.INSERT_CONFIRMATION_QUERY;
import static za.ac.cput.query.UserQuery.*;
/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date :  2023/05/10
 * @Time : 13:00
 **/
@RequiredArgsConstructor
@Repository
@Slf4j
public class UserRepositoryImp implements UserRepository<User> {
    private final NamedParameterJdbcTemplate jdbc;
    private final RoleRepository<Role> roleRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public User save(User user) {
        log.info("Registering a User");
        if (user.getPassword() == null) {
            throw new ApiException("Password cannot be null");
        }
        if (getEmailCount(user.getEmail().trim().toLowerCase()) > 0) {
            throw new ApiException("Email already in use. Please use a different email and try again.");
        }
        try {
            // Save user
            KeyHolder holder = new GeneratedKeyHolder();
            SqlParameterSource parameters = getSqlParameterSource(user);
            jdbc.update(INSERT_USER_QUERY, parameters, holder);
            user.setId(Objects.requireNonNull(holder.getKey()).longValue());
            // Define email to role mapping
            Map<String, String> emailToRole = new HashMap<>();
            emailToRole.put("thabiso.matsaba@younglings.africa", ROLE_ADMIN.name());
            emailToRole.put("thabisomatsaba96@gmail.com", ROLE_MANAGER.name());
            emailToRole.put("220296006@mycput.ac.za", ROLE_SYSADMIN.name());
            // Add more mappings as needed
            // Assign role based on email
            String email = user.getEmail().toLowerCase();
            if (emailToRole.containsKey(email)) {
                String roleName = emailToRole.get(email);
                Role role = new Role();
                role.setName(roleName);
                role.setPermissions(getPermissionsForRole(roleName)); // Get permissions for the role
                user.getRoles().add(role);
                // Set userId parameter before adding role to user
                roleRepository.addRoleToUser(user.getId(), roleName, role.getPermissions()); // Add role to user
            } else {
                String roleName = "ROLE_USER";
                Role role = new Role();
                role.setName(roleName);
                role.setPermissions(getPermissionsForRole(roleName)); // Get permissions for the default role
                user.getRoles().add(role);
                // Set userId parameter before adding role to user
                roleRepository.addRoleToUser(user.getId(), roleName, role.getPermissions()); // Add role to user
            }
            // Generate confirmation token and send confirmation email (if needed)
            String token = UUID.randomUUID().toString();
            jdbc.update(INSERT_CONFIRMATION_QUERY, Map.of("userId", user.getId(), "token", token));
            emailService.sendMimeMessageWithAttachments(user.getFirstName(), user.getEmail(), token);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setEnabled(true);
            user.setNotLocked(true);
            return user;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again.");
        }
    }

    private Set<String> getPermissionsForRole(String roleName) {
        Set<String> permissions = new HashSet<>();
        // Default permissions for other roles
        permissions.add("READ:USER");
        permissions.add("READ:CUSTOMER");
        switch (roleName) {
            case "ROLE_ADMIN":
                permissions.add("READ:USER");
                permissions.add("READ:CUSTOMER");
                permissions.add("CREATE:USER");
                permissions.add("DELETE:USER");
                permissions.add("DELETE:CUSTOMER");
                permissions.add("UPDATE:USER");
                permissions.add("UPDATE:CUSTOMER");
                // Add more permissions as needed
                break;
            case "ROLE_MANAGER":
                permissions.add("READ:USER");
                permissions.add("READ:CUSTOMER");
                permissions.add("UPDATE:USER");
                permissions.add("UPDATE:CUSTOMER");
                // Add more permissions as needed
                break;
            case "ROLE_SYSADMIN":
                // ROLE_SYSADMIN has all permissions
                permissions.add("READ:USER");
                permissions.add("READ:CUSTOMER");
                permissions.add("CREATE:USER");
                permissions.add("CREATE:CUSTOMER");
                permissions.add("DELETE:USER");
                permissions.add("DELETE:CUSTOMER");
                permissions.add("UPDATE:USER");
                permissions.add("UPDATE:CUSTOMER");
                // Add more permissions as needed
                break;
            case "ROLE_USER":
                // Define permissions for ROLE_USER
                permissions.add("READ:USER"); // Allow user to see their own profile information
                permissions.add("READ:CUSTOMER");
                // Add more permissions for ROLE_USER as needed
                break;
        }
        // Logging statements to verify permissions
        log.info("Permissions for role {}: {}", roleName, permissions);
        return permissions;
    }



    @Override
    public Collection<User> list(String name, int page, int pageSize) {
        log.info("Fetch All Users");
        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("size", pageSize)
                    .addValue("page", (page - 1) * pageSize);
            return jdbc.query(FETCH_ALL_USERS_QUERY, parameters, new UserRowMapper());
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("No users found. Please try again.");
        }
    }

    @Override
    public User read(Long id) {
        log.info("Fetch User by Id {}", id);
        try {
            return jdbc.queryForObject(FETCH_USER_BY_ID_QUERY, Map.of("user_id", id), new UserRowMapper());
        } catch (EmptyResultDataAccessException exception) {
            return null;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("No user with ID" + id + "found. Please try again.");
        }
    }

    @Override
    public User update(User user) {
        log.info("Updating user");
        try {
            SqlParameterSource parameters = getSqlParameterSource(user);
            jdbc.update(UPDATE_USER_QUERY, parameters);
            return user;
        } catch (EmptyResultDataAccessException exception) {
            return null;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while updating the user. Please try again.");
        }
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting user by Id");
        try {
            jdbc.update(DELETE_USER_BY_ID_QUERY, Map.of("user_id", id));
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while deleting the user. Please try again.");
        }
    }

    @Override
    public User findUserByEmailIgnoreCase(String email) {
        log.info("Fetch User by Email {}", email);
        try {
            return jdbc.queryForObject(FETCH_USER_BY_EMAIL_QUERY, Map.of("email", email),
                    new UserRowMapper()
            );
        } catch (EmptyResultDataAccessException exception) {
            return null;
        } catch (Exception exception) {
            log.error("Error while fetching user by email: {}", exception.getMessage());
            throw new ApiException("Error while fetching user by email");
        }
    }


    private Integer getEmailCount(String email) {
        return jdbc.queryForObject(COUNT_USER_EMAIL_QUERY, Map.of("email", email), Integer.class);
    }

    private SqlParameterSource getSqlParameterSource(User user) {
        return new MapSqlParameterSource()
                .addValue("id", user.getId())
                .addValue("firstName", user.getFirstName())
                .addValue("middleName", user.getMiddleName())
                .addValue("lastName", user.getLastName())
                .addValue("imageUrl", user.getImageUrl())
                .addValue("email", user.getEmail())
                .addValue("phone", user.getPhone())
                .addValue("address", user.getAddress())
                .addValue("imageData", user.getImageData())
                .addValue("password",(passwordEncoder.encode(user.getPassword())));
    }
}

