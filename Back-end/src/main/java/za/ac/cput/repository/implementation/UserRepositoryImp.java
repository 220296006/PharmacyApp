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
            // Return newly created user
            return user;
            // If any errors, throw exception with proper message
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
            jdbc.queryForList(FETCH_ALL_USERS_FROM_DATABASE_QUERY, Map.of("page", 0, "size", 5));
            return null;
        } catch (Exception exception){
            log.error(exception.getMessage());
            throw new ApiException("No users found. Please try again.");
        }
    }

    @Override
    public User read(Long id) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
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
                .addValue("password", encoder.encode(user.getPassword()));
    }

    private String getVerificationUrl(String key, String type) {
         return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/verify/" + "/" + key + type).toUriString();
    }
}
