package za.ac.cput.query;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/13
 * @Time : 20:34
 **/
public class ConfirmationQuery {
    public static final String INSERT_CONFIRMATION_QUERY = "INSERT INTO Confirmations (token, user_id) VALUES (:token, :userId)";
    public static final String SELECT_CONFIRMATION_BY_ID_QUERY = "SELECT * FROM Confirmations WHERE id = :id";
    public static final String UPDATE_CONFIRMATION_QUERY = "UPDATE Confirmations SET token = :token, created_date = :createdDate, user_id = :userId WHERE id = :id";
    public static final String DELETE_CONFIRMATION_QUERY = "DELETE FROM Confirmations WHERE id = :id";
    public static final String FETCH_ALL_CONFIRMATIONS_QUERY = "SELECT * FROM Confirmations";
    public static final String FIND_TOKEN_BY_USER_ID = "SELECT token FROM Confirmations WHERE user_id = :userId";
}

