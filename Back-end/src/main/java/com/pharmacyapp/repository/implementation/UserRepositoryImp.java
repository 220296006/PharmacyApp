package com.pharmacyapp.repository.implementation;

import com.pharmacyapp.enumeration.RoleType;
import com.pharmacyapp.exception.ApiException;
import com.pharmacyapp.model.Role;
import com.pharmacyapp.model.User;
import com.pharmacyapp.query.UserQuery;
import com.pharmacyapp.repository.RoleRepository;
import com.pharmacyapp.repository.UserRepository;
import com.pharmacyapp.rowmapper.ImageDataRowMapper;
import com.pharmacyapp.rowmapper.UserRowMapper;
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
import com.pharmacyapp.repository.ElasticsearchUserRepository;
import com.pharmacyapp.service.EmailService;

import java.sql.SQLException;
import java.util.*;

import static com.pharmacyapp.query.ConfirmationQuery.INSERT_CONFIRMATION_QUERY;

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
    private final ImageDataRowMapper imageDataRowMapper;
    private static final String UPDATE_USER_PROFILE_IMAGE_SQL = "UPDATE users SET image_url = :imageUrl WHERE id = :userId";
    private final ElasticsearchUserRepository elasticsearchUserRepository;


    @Override
    public User save(User user) {
        log.info("Registering a User {}", user);
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
            jdbc.update(UserQuery.INSERT_USER_QUERY, parameters, holder);
            user.setId(Objects.requireNonNull(holder.getKey()).longValue());
            // Define email to role mapping
            Map<String, String> emailToRole = new HashMap<>();
            emailToRole.put("thabiso.matsaba@liquidc2.com", RoleType.ROLE_ADMIN.name());
            emailToRole.put("220296006@mycput.ac.za", RoleType.ROLE_MANAGER.name());
            emailToRole.put("thabisomatsaba96@gmail.com", RoleType.ROLE_SYSADMIN.name());
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

            // Elasticsearch part
            elasticsearchUserRepository.save(user);
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
            case "ROLE_ADMIN", "ROLE_MANAGER":
                permissions.add("READ:USER");
                permissions.add("READ:CUSTOMER");
                permissions.add("CREATE:CUSTOMER");
                permissions.add("CREATE:USER");
                permissions.add("DELETE:USER");
                permissions.add("DELETE:CUSTOMER");
                permissions.add("UPDATE:USER");
                permissions.add("UPDATE:CUSTOMER");
                // Add more permissions as needed
                break;
            // Add more permissions as needed
            case "ROLE_SYSADMIN":
                // ROLE_SYSADMIN has all permissions
                permissions.add("READ:USER");
                permissions.add("READ:CUSTOMER");
                permissions.add("CREATE:USER");
                permissions.add("CREATE:INVOICE");
                permissions.add("CREATE:MEDICATION");
                permissions.add("CREATE:PRESCRIPTION");
                permissions.add("CREATE:INVENTORY");
                permissions.add("CREATE:CUSTOMER");
                permissions.add("DELETE:USER");
                permissions.add("DELETE:CUSTOMER");
                permissions.add("DELETE:INVOICE");
                permissions.add("DELETE:MEDICATION");
                permissions.add("DELETE:PRESCRIPTION");
                permissions.add("DELETE:INVENTORY");
                permissions.add("UPDATE:USER");
                permissions.add("UPDATE:CUSTOMER");
                permissions.add("UPDATE:INVOICE");
                permissions.add("UPDATE:MEDICATION");
                permissions.add("UPDATE:PRESCRIPTION");
                permissions.add("UPDATE:INVENTORY");

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
        log.info("Fetching All Users");
        try {
            int offset = (page - 1) * pageSize; // Calculate offset for pagination
            // Create parameter source with values for pagination
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("limit", pageSize)
                    .addValue("offset", offset);
            // Execute the query with parameters and process the result set
            return jdbc.query(UserQuery.FETCH_ALL_USERS_QUERY, parameters, resultSet -> {
                Map<Long, User> userMap = new LinkedHashMap<>(); // Using LinkedHashMap to maintain insertion order
                while (resultSet.next()) {
                    long userId = resultSet.getLong("id");
                    User user = userMap.computeIfAbsent(userId, id -> {
                        try {
                            UserRowMapper userRowMapper = new UserRowMapper(); // Create an instance of UserRowMapper
                            return userRowMapper.mapRow(resultSet, -1); // Call the instance method mapRow
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }); // Map basic user information
                }

              elasticsearchUserRepository.findAll();

                // Return the collection of mapped users
                return userMap.values();
            });
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("No users found. Please try again.");
        }
    }


    @Override
    public User read(Long id) {
        log.info("Fetching User by Id: {}", id);
        try {
            return jdbc.queryForObject(UserQuery.FETCH_USER_BY_ID_QUERY,
                    Collections.singletonMap("user_id", id),
                    new UserRowMapper());
        } catch (EmptyResultDataAccessException exception) {
            log.warn("No user found with ID: {}", id);
            return null; // Return null when no user is found
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while fetching user with ID " + id);
        }
    }


    @Override
    public User update(User user) {
        log.info("Updating user: {}", user);
        try {
            SqlParameterSource parameters = getSqlParameterSource(user);
            jdbc.update(UserQuery.UPDATE_USER_QUERY, parameters);
            // Elasticsearch part
           elasticsearchUserRepository.save(user);
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
            jdbc.update(UserQuery.DELETE_USER_BY_ID_QUERY, Map.of("user_id", id));
            // Elasticsearch part
            elasticsearchUserRepository.deleteById(id);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while deleting the user. Please try again.");
        }
    }

    @Override
    public User findUserByEmailIgnoreCase(String email) {
        log.info("Fetch User by Email {}", email);
        try {
            return jdbc.queryForObject(UserQuery.FETCH_USER_BY_EMAIL_QUERY, Map.of("email", email),
                    new UserRowMapper()
            );
        } catch (EmptyResultDataAccessException exception) {
            return null;
        } catch (Exception exception) {
            log.error("Error while fetching user by email: {}", exception.getMessage());
            throw new ApiException("Error while fetching user by email");
        }
    }

    @Override
    public Integer countUsers() {
        log.info("Fetching Total Users");
        try {
            return jdbc.queryForObject(UserQuery.SELECT_USER_COUNT_QUERY, new HashMap<>(), Integer.class);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while fetching users count. Please try again.");
        }
    }

    @Override
    public void updateUserImageUrl(Long userId, String imageUrl) {
        log.info("Updating image URL for user ID: {}", userId);
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("imageUrl", imageUrl);
        int updateCount = jdbc.update(UPDATE_USER_PROFILE_IMAGE_SQL, parameters);
        if (updateCount != 1) {
            log.error("Failed to update image URL for user ID: {}", userId);
            throw new ApiException("Failed to update image URL for user ID: " + userId);
        }
        log.info("Successfully updated image URL for user ID: {}", userId);
    }


    private Integer getEmailCount(String email) {
        return jdbc.queryForObject(UserQuery.COUNT_USER_EMAIL_QUERY, Map.of("email", email), Integer.class);
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

