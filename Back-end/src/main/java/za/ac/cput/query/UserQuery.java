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
             "SELECT u.*, GROUP_CONCAT(r.name) as name, GROUP_CONCAT(i.id) as image_id, GROUP_CONCAT(i.type) as type, " +
                     "GROUP_CONCAT(i.image_data) as imageData " +
                     "FROM Users u " +
                     "JOIN UserRoles ur ON u.id = ur.user_id " +
                     "JOIN Roles r ON ur.role_id = r.id " +
                     "LEFT JOIN image_data i ON u.id = i.user_id " +
                     "WHERE u.email = :email " +
                     "GROUP BY u.id ";



     public static final String INSERT_USER_QUERY = "INSERT INTO Users (first_name, middle_name, last_name, email, "
             + "password, phone, address) VALUES (:firstName, :middleName, :lastName, :email, :password, :phone, " +
             ":address)" ;
     public static final String FETCH_ALL_USERS_QUERY = "SELECT * FROM Users LIMIT :size OFFSET :page";
     public static final String FETCH_USER_BY_ID_QUERY = "SELECT * FROM Users WHERE id = :user_id";
     public static final String DELETE_USER_BY_ID_QUERY = "DELETE FROM Users WHERE id = :user_id";
     public static final String UPDATE_USER_QUERY = "UPDATE Users SET first_name = :firstName," +
             " middle_name = :middleName, last_name = :lastName, email = :email, phone = :phone, " +
             "address = :address WHERE id = :id";

     public static final String UPDATE_USER_PROFILE_IMAGE_SQL = "UPDATE Users SET image_data = :imageData WHERE id = :id";

     public static final String GET_USERS_BY_ROLE_QUERY =
              "SELECT u.*, GROUP_CONCAT(r.name) as roles " +
                      "FROM Users u " +
                      "JOIN UserRoles ur ON u.id = ur.user_id " +
                      "JOIN Roles r ON ur.role_id = r.id " +
                      "WHERE r.name = :roleName " +
                      "GROUP BY u.id";
      public static final String ASSIGN_ROLE_QUERY =
              "INSERT INTO UserRoles (user_id, role_id) " +
                      "VALUES (:userId, (SELECT id FROM Roles WHERE name = :roleName))";
      public static final String REVOKE_ROLE_QUERY =
              "DELETE FROM UserRoles WHERE user_id = :userId AND role_id = (SELECT id FROM Roles WHERE " +
                      "name = :roleName)";

 }
