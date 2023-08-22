package za.ac.cput.repository.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import za.ac.cput.exception.ApiException;
import za.ac.cput.model.Confirmation;
import za.ac.cput.model.Role;
import za.ac.cput.model.User;
import za.ac.cput.repository.RoleRepository;
import za.ac.cput.repository.UserRepository;
import za.ac.cput.rowmapper.UserRowMapper;
import za.ac.cput.service.EmailService;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static za.ac.cput.enumeration.RoleType.ROLE_USER;
import static za.ac.cput.enumeration.VerificationType.ACCOUNT;
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
    private final BCryptPasswordEncoder encoder;
    private final EmailService emailService;

    @Override
    public User save(User user) {
        log.info("Saving A User");
        if(getEmailCount(user.getEmail().trim().toLowerCase()) > 0) throw new
                ApiException("Email already in use. Please use different email and try again");
        try {
            KeyHolder holder = new GeneratedKeyHolder();
            SqlParameterSource parameters = getSqlParameterSource(user);
            jdbc.update(INSERT_USER_QUERY, parameters, holder);
            user.setId(Objects.requireNonNull(holder.getKey()).longValue());
            roleRepository.addRoleToUser(user.getId(), ROLE_USER.name());
            String verificationToken = getVerificationUrl(UUID.randomUUID().toString(), ACCOUNT.getType());
            jdbc.update(INSERT_CONFIRMATION_QUERY, Map.of("userId", user.getId(), "token", verificationToken));
            Confirmation confirmation = new Confirmation(user);
            emailService.sendSimpleMailMessage(user.getFirstName(), user.getEmail(), confirmation.getToken());
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
    }  catch (EmptyResultDataAccessException exception) {
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
    public User findByEmailIgnoreCase(String email) {
        return null;
    }

    @Override
    public Boolean existByEmail(String email) {
        return null;
    }

    private Integer getEmailCount(String email){
        return jdbc.queryForObject(COUNT_USER_EMAIL_QUERY, Map.of("email", email), Integer.class);
    }

    private SqlParameterSource getSqlParameterSource(User user) {
        return  new MapSqlParameterSource()
               .addValue("id", user.getId())
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
