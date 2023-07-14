package za.ac.cput.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import za.ac.cput.model.Confirmation;
import za.ac.cput.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/13
 * @Time : 20:59
 **/
public class ConfirmationRowMapper implements RowMapper<Confirmation> {
   @Override
    public Confirmation mapRow(ResultSet rs, int rowNum) throws SQLException {
        Confirmation confirmation = new Confirmation();
        confirmation.setId(rs.getLong("id"));
        confirmation.setToken(rs.getString("token"));
        confirmation.setCreatedDate(rs.getTimestamp("created_date").toLocalDateTime());

        User user = new User();
        user.setId(rs.getLong("user_id"));

        return confirmation;
    }
}
