package za.ac.cput.repository.implementation;

import com.twilio.rest.api.v2010.Account;
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
import za.ac.cput.dto.UserUpdateDTO;
import za.ac.cput.dtomapper.UserUpdateDTOMapper;
import za.ac.cput.exception.ApiException;
import za.ac.cput.model.Confirmation;
import za.ac.cput.model.Role;
import za.ac.cput.model.User;
import za.ac.cput.repository.RoleRepository;
import za.ac.cput.repository.UserRepository;
import za.ac.cput.rowmapper.UserRowMapper;
import za.ac.cput.service.EmailService;

import java.util.*;

import static za.ac.cput.enumeration.RoleType.ROLE_ADMIN;
import static za.ac.cput.enumeration.RoleType.ROLE_USER;
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
        log.info("Saving A User");
        if (user.getPassword() == null) {
            throw new ApiException("Password cannot be null");
        }
        if (getEmailCount(user.getEmail().trim().toLowerCase()) > 0) throw new
                ApiException("Email already in use. Please use different email and try again");
        try {
            log.info("Password before encoding: {}", user.getPassword()); // Log password before encoding
            String hashedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
            log.info("Password after encoding: {}", user.getPassword()); // Log password after encoding
            KeyHolder holder = new GeneratedKeyHolder();
            SqlParameterSource parameters = getSqlParameterSource(user);
            jdbc.update(INSERT_USER_QUERY, parameters, holder);
            user.setId(Objects.requireNonNull(holder.getKey()).longValue());
            roleRepository.addRoleToUser(user.getId(), ROLE_USER.name());
            String token = UUID.randomUUID().toString();
            jdbc.update(INSERT_CONFIRMATION_QUERY, Map.of("userId", user.getId(), "token", token));
            emailService.sendMimeMessageWithAttachments(user.getFirstName(), user.getEmail(), token);
            user.setEnabled(true);
            user.setNotLocked(false);
            return user;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again.");
        }
    }

    @Override
    public User saveAdmin(User user) {
        log.info("Saving A User");
        try {
            User adminUser = new User();
            adminUser.setFirstName("Thabiso");
            adminUser.setLastName("Matsaba");
            adminUser.setEmail("thabisomatsaba96@gmail.com");
            // Encrypt the password for the admin user
            String adminPassword = "admin@2024"; // Set the desired admin password
            String hashedAdminPassword = passwordEncoder.encode(adminPassword);
            adminUser.setPassword(hashedAdminPassword);
            // Save admin user to the database
            KeyHolder holder = new GeneratedKeyHolder();
            SqlParameterSource parameters = getSqlParameterSource(adminUser);
            jdbc.update(INSERT_USER_QUERY, parameters, holder);
            adminUser.setId(Objects.requireNonNull(holder.getKey()).longValue());
            roleRepository.addRoleToUser(adminUser.getId(), ROLE_ADMIN.name());
            user.setEnabled(true);
            user.setNotLocked(false);
            return user;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again.");
        }

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
        log.info("Fetch User by Id");
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

    public UserUpdateDTO updateSysAdmin(UserUpdateDTO updatedUser) {
        // Convert UserUpdateDTO to User and call the existing update method
        User user = UserUpdateDTOMapper.toUser(updatedUser);
        update(user);
        return updatedUser;
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
            return jdbc.queryForObject(
                    "SELECT u.*, GROUP_CONCAT(r.name) as roles " +
                            "FROM Users u " +
                            "JOIN UserRoles ur ON u.id = ur.user_id " +
                            "JOIN Roles r ON ur.role_id = r.id " +
                            "WHERE u.email = :email " +
                            "GROUP BY u.id",
                    Map.of("email", email), // Pass the email parameter here
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
    public Boolean existByEmail(String email) {
        log.info("Fetch User by Email");
        try {
            jdbc.queryForObject(FETCH_USER_BY_EMAIL_QUERY, Map.of("email", email), new UserRowMapper());
        } catch (EmptyResultDataAccessException exception) {
            return null;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("Email not found. Please use different email and try again");
        }
        return null;
    }

    @Override
    public List<User> getUsersByRole(String roleName) {
        // Implement logic to fetch users by role
        try {
            return jdbc.query(GET_USERS_BY_ROLE_QUERY,
                    Map.of("roleName", roleName),
                    new UserRowMapper());
        } catch (Exception exception) {
            log.error("Error while fetching users by role {}: {}", roleName, exception.getMessage());
            throw new ApiException("Error while fetching users by role");
        }
    }

    // Additional methods for managing roles and permissions
    @Override
    public void assignRole(Long userId, String roleName) {
        // Implement logic to assign a role to a user
        try {
            Role role = roleRepository.findRoleByName(roleName);
            if (role != null) {
                jdbc.update(ASSIGN_ROLE_QUERY,
                        Map.of("userId", userId, "roleId", role.getId()));
            } else {
                throw new ApiException("Role not found: " + roleName);
            }
        } catch (Exception exception) {
            log.error("Error while assigning role {} to user {}: {}", roleName, userId, exception.getMessage());
            throw new ApiException("Error while assigning role to user");
        }
    }

    @Override
    public void revokeRole(Long userId, String roleName) {
        // Implement logic to revoke a role from a user
        try {
            Role role = roleRepository.findRoleByName(roleName);
            if (role != null) {
                jdbc.update(REVOKE_ROLE_QUERY,
                        Map.of("userId", userId, "roleId", role.getId()));
            } else {
                throw new ApiException("Role not found: " + roleName);
            }
        } catch (Exception exception) {
            log.error("Error while revoking role {} from user {}: {}", roleName, userId, exception.getMessage());
            throw new ApiException("Error while revoking role from user");
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
                .addValue("email", user.getEmail())
                .addValue("phone", user.getPhone())
                .addValue("address", user.getAddress())
                .addValue("password", (user.getPassword()));


    }
}

