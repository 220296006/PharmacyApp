package za.ac.cput.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import za.ac.cput.model.Customer;
import za.ac.cput.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/10
 * @Time : 20:19
 **/
public class CustomerRowMapper implements RowMapper<Customer> {
    @Override
    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getLong("id"));
        customer.setCity(rs.getString("city"));
        customer.setState(rs.getString("state"));
        customer.setZipCode(rs.getString("zip_code"));

        User user = new User();
        user.setId(rs.getLong("user_id"));
        user.setFirstName(rs.getString("first_name"));
        user.setImageUrl(rs.getString("image_url"));
        user.setAddress(rs.getString("address"));
        customer.setUser(user);
        return customer;
    }
}

