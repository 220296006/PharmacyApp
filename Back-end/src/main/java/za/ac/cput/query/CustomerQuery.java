package za.ac.cput.query;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/07
 * @Time : 18:43
 **/
public class CustomerQuery {
 public static final String INSERT_CUSTOMER_QUERY = "INSERT INTO Customers (imageUrl, address, first_name, city, state, zip_code, user_id) VALUES (:image_url, :address, :first_name, :city, :state, :zipCode, :user_id)";
public static final String FETCH_CUSTOMER_BY_ID_QUERY = "SELECT * FROM Customers WHERE id = :id";
public static final String FETCH_ALL_CUSTOMERS_QUERY = "SELECT * FROM Customers LIMIT :size OFFSET :page";
public static final String UPDATE_CUSTOMER_QUERY = "UPDATE Customers SET imageUrl = :image_url, address = :address, first_name = :first_name, city = :city, state = :state, zip_code = :zipCode WHERE id = :id";
public static final String UPDATE_CUSTOMER_LINKED_TO_USER_QUERY = "UPDATE Customers SET user_id = :user_id, first_name = :first_name WHERE id = :id";
 public static final String DELETE_CUSTOMER_QUERY = "DELETE FROM Customers WHERE id = :id";
}
