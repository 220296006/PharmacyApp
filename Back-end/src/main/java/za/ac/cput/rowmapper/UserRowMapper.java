package za.ac.cput.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import za.ac.cput.model.ImageData;
import za.ac.cput.model.Role;
import za.ac.cput.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Set;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setMiddleName(resultSet.getString("middle_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setPassword(resultSet.getString("password"));
        user.setImageUrl(resultSet.getString("image_url"));
        user.setEmail(resultSet.getString("email"));
        user.setPhone(resultSet.getString("phone"));
        user.setAddress(resultSet.getString("address"));

        // Fetching image data
        ImageData imageData = new ImageData();
        imageData.setId(resultSet.getLong("image_id"));
        imageData.setName(resultSet.getString("name"));
        imageData.setType(resultSet.getString("type"));
        imageData.setImageData(resultSet.getBytes("image_data"));
        user.setImageData(imageData);
        // Fetching roles
        // Fetching role information
        Role role = new Role();
        role.setId(resultSet.getLong("id")); // Correct the column name here
        role.setName(resultSet.getString("name")); // Correct the column name here
        user.getRoles().add(role);
        return user;
    }
}
