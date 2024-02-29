package za.ac.cput.query;
 /**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/07
 * @Time : 18:00
 **/
public class UserQuery {
     public static final String COUNT_USER_EMAIL_QUERY = "SELECT COUNT(*) FROM Users WHERE email = :email";
     public static final String FETCH_USER_BY_EMAIL_QUERY =
             "SELECT u.*, GROUP_CONCAT(r.name) as roles \n" +
                     "FROM Users u \n" +
                     "JOIN UserRoles ur ON u.id = ur.user_id \n" +
                     "JOIN Roles r ON ur.role_id = r.id \n" +
                     "WHERE u.email = ?\n" +
                     "GROUP BY u.id\n";
     public static final String INSERT_USER_QUERY = "INSERT INTO Users (first_name, middle_name, last_name, email, password, phone, address) VALUES (:firstName, :middleName, :lastName, :email, :password, :phone, :address)" ;
     public static final String FETCH_ALL_USERS_QUERY = "SELECT * FROM Users LIMIT :size OFFSET :page";
     public static final String FETCH_USER_BY_ID_QUERY = "SELECT * FROM Users WHERE id = :user_id";
     public static final String DELETE_USER_BY_ID_QUERY = "DELETE FROM Users WHERE id = :user_id";
     public static final String UPDATE_USER_QUERY = "UPDATE Users SET first_name = :firstName, middle_name = :middleName, last_name = :lastName, email = :email, phone = :phone, address = :address WHERE id = :id";
}
