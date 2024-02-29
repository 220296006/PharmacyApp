package za.ac.cput.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import za.ac.cput.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setMiddleName(resultSet.getString("middle_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setEmail(resultSet.getString("email"));
        // Omit password mapping
        user.setPhone(resultSet.getString("phone"));
        user.setAddress(resultSet.getString("address"));
        user.setEnabled(resultSet.getBoolean("enabled"));

        // Retrieve roles as string from ResultSet
        String rolesString = resultSet.getString("roles");
        List<GrantedAuthority> authorities = Arrays.stream(rolesString.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // Defer role loading (consider using JPA relationships or a separate service)
        // user.setAuthorities(authorities);

        return user;
    }
}
