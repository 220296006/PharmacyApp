package com.pharmacyapp.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import com.pharmacyapp.model.ImageData;
import com.pharmacyapp.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * @author : Thabiso Matsaba
 * @Project : Back-end
 * @Date : 2024/04/18
 * @Time : 17:58
 **/
@Component
public class ImageDataRowMapper implements RowMapper<ImageData> {
    @Override
    public ImageData mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        // Map image data fields from ResultSet to ImageData object
        ImageData imageData = new ImageData();
        imageData.setId(resultSet.getLong("id"));
        imageData.setName(resultSet.getString("name"));
        imageData.setType(resultSet.getString("type"));
        imageData.setImageData(resultSet.getBytes("image_data"));
        imageData.setUserId(resultSet.getLong("user_id")); // Set the user ID property
        return imageData;
    }
}
