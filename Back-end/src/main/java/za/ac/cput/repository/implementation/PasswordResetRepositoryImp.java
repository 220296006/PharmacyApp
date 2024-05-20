package za.ac.cput.repository.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import za.ac.cput.model.ResetPasswordVerification;
import za.ac.cput.model.User;
import za.ac.cput.rowmapper.ResetPasswordVerificationRowMapper;
import za.ac.cput.rowmapper.UserRowMapper;

import java.util.Date;

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

    public static final String INSERT_RESET_TOKEN_QUERY = "INSERT INTO ResetPasswordVerifications (url, expiration_date, user_id) VALUES (:url, :expirationDate, :userId)";
    public static final String FIND_USER_BY_EMAIL_QUERY = "SELECT * FROM Users WHERE email = :email";
    public static final String FIND_USER_BY_ID_QUERY = "SELECT * FROM Users WHERE id = :id";
    public static final String FIND_RESET_TOKEN_QUERY = "SELECT * FROM ResetPasswordVerifications WHERE url = :url";
    public static final String UPDATE_USER_PASSWORD_QUERY = "UPDATE Users SET password = :password WHERE id = :userId";

    public void saveResetToken(String token, Date expirationDate, Long userId) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("url", token)
                .addValue("expirationDate", expirationDate)
                .addValue("userId", userId);
        jdbc.update(INSERT_RESET_TOKEN_QUERY, parameters);
    }

    public User findUserByEmail(String email) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("email", email);
        return jdbc.queryForObject(FIND_USER_BY_EMAIL_QUERY, parameters, new UserRowMapper());
    }

    public User findUserById(Long id) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", id);
        return jdbc.queryForObject(FIND_USER_BY_ID_QUERY, parameters, new UserRowMapper());
    }

    public ResetPasswordVerification findResetToken(String token) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("url", token);
        return jdbc.queryForObject(FIND_RESET_TOKEN_QUERY, parameters, new ResetPasswordVerificationRowMapper());
    }

    public void updateUserPassword(Long userId, String password) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("password", password);
        jdbc.update(UPDATE_USER_PASSWORD_QUERY, parameters);
    }
}
