package za.ac.cput.query;


/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/07
 * @Time : 18:43
 **/
public class CustomerQuery {
 public static final String INSERT_CUSTOMER_QUERY = "INSERT INTO Customers (city, state, zip_code, user_id) VALUES (:city, :state, :zipCode, :user_id)";
public static final String FETCH_CUSTOMER_BY_ID_QUERY = """
                SELECT Customers.*, Users.first_name, Users.image_url, Users.address
                FROM Customers
                JOIN Users ON Customers.user_id = Users.id
                WHERE Customers.id = :id
                """;
public static final String FETCH_ALL_CUSTOMERS_QUERY = """
        SELECT Customers.*, Users.first_name, Users.image_url, Users.address
        FROM Customers
        JOIN Users ON Customers.user_id = Users.id
        LIMIT :size OFFSET :page    \s
        """;
public static final String UPDATE_CUSTOMER_QUERY = "UPDATE Customers SET city = :city, state = :state, zip_code = :zipCode WHERE id = :id";
public static final String UPDATE_CUSTOMER_LINKED_TO_USER_QUERY = "UPDATE Customers SET user_id = :user_id HERE id = :id";
 public static final String DELETE_CUSTOMER_QUERY = "DELETE FROM Customers WHERE id = :id";
}
