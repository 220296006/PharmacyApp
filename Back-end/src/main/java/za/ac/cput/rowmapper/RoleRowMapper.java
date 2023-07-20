package za.ac.cput.rowmapper;
import org.springframework.jdbc.core.RowMapper;
import za.ac.cput.model.Role;

import java.sql.ResultSet;
import java.sql.SQLException;
 /**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/09
 * @Time : 16:00
 **/
public class RoleRowMapper implements RowMapper<Role> {
    @Override
    public Role mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Role.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .permission(resultSet.getString("permission"))
                .build();
    }
}
