package com.pharmacyapp.repository;

import com.pharmacyapp.rowmapper.ImageDataRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import com.pharmacyapp.model.ImageData;

import java.util.Optional;

/**
 * @author : Thabiso Matsaba
 * @Project : Back-end
 * @Date : 2024/04/04
 * @Time : 19:37
 **/
@Repository
public class ImageDataRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ImageDataRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<ImageData> findByUserId(Long userId) {
        String sql = "SELECT * FROM imagedata WHERE user_id = :userId";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("userId", userId);
        return jdbcTemplate.query(sql, params, new ImageDataRowMapper()).stream().findFirst();
    }

    public Optional<ImageData> findByNameAndUserId(String fileName, Long userId) {
        String sql = "SELECT * FROM imagedata WHERE name = :fileName AND user_id = :userId";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("fileName", fileName)
                .addValue("userId", userId);
        return jdbcTemplate.query(sql, params, new ImageDataRowMapper()).stream().findFirst();
    }

    public void save(ImageData imageData) {
        String sql = "INSERT INTO imagedata (name, type, image_data, user_id) VALUES (:name, :type, :imageData, :userId)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", imageData.getName())
                .addValue("type", imageData.getType())
                .addValue("imageData", imageData.getImageData())
                .addValue("userId", imageData.getUserId());
        jdbcTemplate.update(sql, params);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM imagedata WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
        jdbcTemplate.update(sql, params);
    }
}