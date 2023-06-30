package za.ac.cput.query;

public class UserQuery {
     public static final String COUNT_USER_EMAIL_QUERY = "SELECT COUNT(*) FROM Users WHERE email = :email";
     public static final String INSERT_USER_QUERY = "INSERT INTO Users (first_name, middle_name, last_name, email, password) VALUES (:firstName, :middleName, :lastName, :email, :password)" ;
     public static final String INSERT_ACCOUNT_VERIFICATION_QUERY = "INSERT INTO AccountVerifications (user_id, url) VALUES (:userId, :url)" ;
     public static final String FETCH_ALL_USERS_FROM_DATABASE_QUERY = "SELECT * FROM Users";
     public static final String FETCH_USER_BY_ID_QUERY = "SELECT userId FROM Users WHERE  ";
}
