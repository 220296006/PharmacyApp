package za.ac.cput.repository.implementation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import za.ac.cput.exception.ApiException;
import za.ac.cput.model.Role;
import za.ac.cput.model.User;
import za.ac.cput.repository.RoleRepository;
import za.ac.cput.repository.UserRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static za.ac.cput.enumeration.RoleType.ROLE_USER;
import static za.ac.cput.enumeration.VerificationType.ACCOUNT;
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
    private final BCryptPasswordEncoder encoder;
    @Override
    public User save(User user) {
        log.info("Saving A User");
        // Check if email is unique
        if(getEmailCount(user.getEmail().trim().toLowerCase()) > 0) throw new
                ApiException("Email already in use. Please use different email and try again");
        //Save a user
        try {
            KeyHolder holder = new GeneratedKeyHolder();
            SqlParameterSource parameters = getSqlParameterSource(user);
            jdbc.update(INSERT_USER_QUERY, parameters, holder);
            user.setId(Objects.requireNonNull(holder.getKey()).longValue());
            // Add role to user
            roleRepository.addRoleToUser(user.getId(), ROLE_USER.name());
            // Send verification URL
            String verificationUrl = getVerificationUrl(UUID.randomUUID().toString(), ACCOUNT.getType());
            //Save URL in table
            jdbc.update(INSERT_ACCOUNT_VERIFICATION_QUERY, Map.of("userId", user.getId(), "url", verificationUrl));
            // Send email to user with verificationUrl
            // emailService.sendVerificationUrl(user.getName().getFirstName(), user.getEmail(), verificationUrl, ACCOUNT);
            user.setEnabled(false);
            user.setNotLocked(true);
            return user;
        }  catch (Exception exception){
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again.");
        }
    }
@Override
public Collection<User> list(String name, int page, int pageSize) {
    log.info("Fetch All Users");
    try {
        // Query database for users
        String query = FETCH_ALL_USERS_FROM_DATABASE_QUERY + " LIMIT :size OFFSET :page";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("size", pageSize)
                .addValue("page", (page - 1) * pageSize);
        return jdbc.query(query, parameters, (resultSet, rowNum) -> {
            User user = new User();
            // Populate the user object from the resultSet
             user.setId(resultSet.getLong("id"));
             user.setFirstName(resultSet.getString("first_name"));
             user.setMiddleName(resultSet.getString("middle_name"));
             user.setLastName(resultSet.getString("last_name"));
             user.setEmail(resultSet.getString("email"));
             user.setPhone(resultSet.getString("phone"));
             user.setAddress(resultSet.getString("address"));
            return user;
        });
    } catch (Exception exception) {
        log.error(exception.getMessage());
        throw new ApiException("No users found. Please try again.");
    }
}

  @Override
public User read(Long id) {
    log.info("Fetch User by Id");
    try {
        return jdbc.queryForObject(FETCH_USER_BY_ID_QUERY, Map.of("user_id", id), (resultSet, rowNum) -> {
            User user = new User();
            user.setId(resultSet.getLong("id"));
             user.setFirstName(resultSet.getString("first_name"));
             user.setMiddleName(resultSet.getString("middle_name"));
             user.setLastName(resultSet.getString("last_name"));
             user.setEmail(resultSet.getString("email"));
             user.setPhone(resultSet.getString("phone"));
             user.setAddress(resultSet.getString("address"));
            return user;
        });
    } catch (Exception exception) {
        log.error(exception.getMessage());
        throw new ApiException("No user with ID" + id + "found. Please try again.");
    }
}

 @Override
public User update(User user) {
    log.info("Updating user");
    try {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", user.getId())
                .addValue("firstName", user.getFirstName())
                .addValue("middleName", user.getMiddleName())
                .addValue("lastName", user.getLastName())
                .addValue("email", user.getEmail())
                .addValue("password", user.getPassword())
                .addValue("phone", user.getPhone())
                .addValue("address", user.getAddress());
        jdbc.update(UPDATE_USER_QUERY, parameters);
        return user;
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


    private Integer getEmailCount(String email){
        return jdbc.queryForObject(COUNT_USER_EMAIL_QUERY, Map.of("email", email), Integer.class);
    }

    private SqlParameterSource getSqlParameterSource(User user) {
        return  new MapSqlParameterSource()
                .addValue("firstName", user.getFirstName())
                .addValue("middleName", user.getMiddleName())
                .addValue("lastName", user.getLastName())
                .addValue("email", user.getEmail())
                .addValue("phone", user.getPhone())
                .addValue("address", user.getAddress())
                .addValue("password", encoder.encode(user.getPassword()));
    }

    private String getVerificationUrl(String key, String type) {
         return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/verify/" + "/" + key + type).toUriString();
    }
}
