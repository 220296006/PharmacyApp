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
        log.info("Registering A User");
        if (user.getPassword() == null) {
            throw new ApiException("Password cannot be null");
        }
        if (getEmailCount(user.getEmail().trim().toLowerCase()) > 0) {
            throw new ApiException("Email already in use. Please use a different email and try again.");
        }
        try {
            // Set password and encode it
            String hashedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
            // Save user
            KeyHolder holder = new GeneratedKeyHolder();
            SqlParameterSource parameters = getSqlParameterSource(user);
            jdbc.update(INSERT_USER_QUERY, parameters, holder);
            user.setId(Objects.requireNonNull(holder.getKey()).longValue());
            // Define email to role mapping
            Map<String, String> emailToRole = new HashMap<>();
            emailToRole.put("thabiso.matsaba@younglings.africa", ROLE_ADMIN.name());
            emailToRole.put("thabiso.matsaba@younglings.africa", ROLE_MANAGER.name());
            emailToRole.put("thabiso.matsaba@younglings.africa", ROLE_SYSADMIN.name());
            // Add more mappings as needed
            // Assign role based on email
            String email = user.getEmail().toLowerCase();
            if (emailToRole.containsKey(email)) {
                String roleName = emailToRole.get(email);
                roleRepository.addRoleToUser(user.getId(), roleName);
            } else {
                roleRepository.addRoleToUser(user.getId(), ROLE_USER.name());
            }
            // Generate confirmation token and send confirmation email (if needed)
            String token = UUID.randomUUID().toString();
            jdbc.update(INSERT_CONFIRMATION_QUERY, Map.of("userId", user.getId(), "token", token));
            emailService.sendMimeMessageWithAttachments(user.getFirstName(), user.getEmail(), token);
            // Set user properties
            user.setEnabled(true);
            user.setNotLocked(true);
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

    public UserUpdateDTO updateAdmin(UserUpdateDTO updatedUser) {
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

