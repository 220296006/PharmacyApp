package za.ac.cput.repository.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import za.ac.cput.model.ResetPasswordVerification;
import za.ac.cput.model.User;
import za.ac.cput.rowmapper.ResetPasswordVerificationRowMapper;
import za.ac.cput.rowmapper.UserRowMapper;

import java.util.Date;

import static za.ac.cput.query.PasswordResetQuery.*;

/**
 * @author : Thabiso Matsaba
 * @Project : Back-end
 * @Date : 2024/05/17
 * @Time : 22:17
 **/

@Repository
@RequiredArgsConstructor
@Slf4j
public class PasswordResetRepositoryImp {
    private final NamedParameterJdbcTemplate jdbc;

    public String getCurrentPassword(Long userId) {
        String password = null;
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("userId", userId);
        try {
            password = jdbc.queryForObject(SELECT_PASSWORD_QUERY, parameters, String.class);
            log.info("Successfully retrieved password for user ID: {}", userId);
        } catch (Exception e) {
            log.error("Error retrieving password for user ID: {}", userId, e);
        }
        return password;
    }

    @Transactional
    public void updateUserPassword(Long userId, String newPasswordHash) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("newPassword", newPasswordHash)
                .addValue("userId", userId);
        try {
            jdbc.update(UPDATE_PASSWORD_QUERY, parameters);
            log.info("Password updated for user ID: {}", userId);
        } catch (Exception e) {
            log.error("Error updating password for user ID: {}", userId, e);
        }
    }

    public void saveResetToken(String token, Date expirationDate, Long userId) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("url", token)
                .addValue("expirationDate", expirationDate)
                .addValue("userId", userId);
        try {
            jdbc.update(INSERT_RESET_TOKEN_QUERY, parameters);
            log.info("Reset token saved for user ID: {}", userId);
        } catch (Exception e) {
            log.error("Error saving reset token for user ID: {}", userId, e);
        }
    }

    public User findUserByEmail(String email) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("email", email);
        try {
            return jdbc.queryForObject(FIND_USER_BY_EMAIL_QUERY, parameters, new UserRowMapper());
        } catch (Exception e) {
            log.error("Error finding user by email: {}", email, e);
            return null;
        }
    }

    public User findUserById(Long id) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", id);
        try {
            return jdbc.queryForObject(FIND_USER_BY_ID_QUERY, parameters, new UserRowMapper());
        } catch (Exception e) {
            log.error("Error finding user by ID: {}", id, e);
            return null;
        }
    }

    public ResetPasswordVerification findResetToken(String token) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("url", token);
        try {
            return jdbc.queryForObject(FIND_RESET_TOKEN_QUERY, parameters, new ResetPasswordVerificationRowMapper());
        } catch (Exception e) {
            log.error("Error finding reset token: {}", token, e);
            return null;
        }
    }
}