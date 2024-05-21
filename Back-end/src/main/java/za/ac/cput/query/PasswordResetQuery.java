package za.ac.cput.query;

/**
 * @author : Thabiso Matsaba
 * @Project : Back-end
 * @Date : 2024/05/21
 * @Time : 00:25
 **/
public class PasswordResetQuery {
    public static final String SELECT_PASSWORD_QUERY = "SELECT password FROM Users WHERE id = :userId";
    public static final String UPDATE_PASSWORD_QUERY = "UPDATE Users SET password = :newPassword WHERE id = :userId";
    public static final String INSERT_RESET_TOKEN_QUERY = "INSERT INTO PasswordResetTokens (url, expirationDate, userId) VALUES (:url, :expirationDate, :userId)";
    public static final String FIND_USER_BY_EMAIL_QUERY = "SELECT * FROM Users WHERE email = :email";
    public static final String FIND_USER_BY_ID_QUERY = "SELECT * FROM Users WHERE id = :id";
    public static final String FIND_RESET_TOKEN_QUERY = "SELECT * FROM PasswordResetTokens WHERE url = :url";
}
