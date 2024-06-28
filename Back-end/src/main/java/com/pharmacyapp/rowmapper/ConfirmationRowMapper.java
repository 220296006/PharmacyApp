package com.pharmacyapp.rowmapper;

import com.pharmacyapp.model.Confirmation;
import com.pharmacyapp.model.User;
import org.springframework.jdbc.core.RowMapper;

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
        confirmation.setUser(user);
        return confirmation;
    }
}
