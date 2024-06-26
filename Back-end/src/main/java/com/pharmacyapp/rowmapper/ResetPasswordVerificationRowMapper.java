package com.pharmacyapp.rowmapper;

import com.pharmacyapp.model.ResetPasswordVerification;
import com.pharmacyapp.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author : Thabiso Matsaba
 * @Project : Back-end
 * @Date : 2024/05/17
 * @Time : 22:31
 **/
public class ResetPasswordVerificationRowMapper implements RowMapper<ResetPasswordVerification> {
    @Override
    public ResetPasswordVerification mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResetPasswordVerification verification = new ResetPasswordVerification();
        verification.setId(rs.getLong("id"));
        verification.setUrl(rs.getString("url"));
        verification.setExpirationDate(rs.getTimestamp("expiration_date"));
        User user = new User();
        verification.setId(rs.getLong("user_id"));
        return verification;
    }
}
