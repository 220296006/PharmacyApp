package za.ac.cput.query;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/07
 * @Time : 18:43
 **/
public class CustomerQuery {
public static final String INSERT_CUSTOMER_QUERY = "INSERT INTO Customers (address, city, state, zip_code, user_id) VALUES (:address, :city, :state, :zipCode, :user_id)";
public static final String FETCH_CUSTOMER_BY_ID_QUERY = "SELECT * FROM Customers WHERE id = :id";
public static final String FETCH_ALL_CUSTOMERS_QUERY = "SELECT * FROM Customers";
public static final String UPDATE_CUSTOMER_QUERY = "UPDATE Customers SET address = :address, city = :city, state = :state, zip_code = :zipCode WHERE id = :id";
public static final String UPDATE_CUSTOMER_LINKED_TO_USER_QUERY = "UPDATE Customers SET user_id = :user_id WHERE id = :id";
 public static final String DELETE_CUSTOMER_QUERY = "DELETE FROM Customers WHERE id = :id";
}
