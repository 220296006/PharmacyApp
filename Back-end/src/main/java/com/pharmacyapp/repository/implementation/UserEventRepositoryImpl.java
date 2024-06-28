package com.pharmacyapp.repository.implementation;

import com.pharmacyapp.exception.ApiException;
import com.pharmacyapp.rowmapper.UserEventRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import com.pharmacyapp.model.UserEvent;
import com.pharmacyapp.repository.UserEventRepository;

import java.util.*;

/**
 * @author : Thabiso Matsaba
 * @Project : Back-end
 * @Date : 2024/05/22
 * @Time : 00:55
 **/
@RequiredArgsConstructor
@Repository
@Slf4j
public class UserEventRepositoryImpl implements UserEventRepository<UserEvent> {
    private static final String UPDATE_USER_EVENT_LINKED_TO_EVENT_QUERY = "UPDATE UserEvents SET event_id = :eventId WHERE id = :id";
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<UserEvent> findAll() {
        log.info("Fetching all UserEvents");
        try {
            String sql = "SELECT ue.*, u.*, e.* FROM UserEvents ue " +
                    "JOIN Users u ON ue.user_id = u.id " +
                    "JOIN Events e ON ue.event_id = e.id";
            return jdbcTemplate.query(sql, new UserEventRowMapper());
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("No user events found. Please try again.");
        }
    }

    @Override
    public List<UserEvent> findByUserId(Long userId) {
        log.info("Fetching UserEvents by userId: {}", userId);
        try {
            String sql = "SELECT ue.*, u.*, e.* FROM UserEvents ue " +
                    "JOIN Users u ON ue.user_id = u.id " +
                    "JOIN Events e ON ue.event_id = e.id " +
                    "WHERE ue.id = :id";
            return jdbcTemplate.query(sql, Collections.singletonMap("userId", userId), new UserEventRowMapper());
        } catch (EmptyResultDataAccessException exception) {
            log.warn("No user events found for userId: {}", userId);
            return Collections.emptyList();
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while fetching user events for userId " + userId);
        }
    }

    @Override
    public UserEvent save(UserEvent userEvent) {
        log.info("Saving a UserEvent: {}", userEvent);
        try {
            KeyHolder holder = new GeneratedKeyHolder();
            SqlParameterSource parameters = getSqlParameterSource(userEvent);
            String sql = "INSERT INTO UserEvents (user_id, event_id, device, ip_address, created_at)" +
                    " VALUES (:userId, :eventId, :device, :ipAddress, :createdAt)";
            jdbcTemplate.update(sql, parameters, holder);
            Map<String, Object> linkUserParams = new HashMap<>();
            linkUserParams.put("userId", userEvent.getUser().getId());
            linkUserParams.put("eventId", userEvent.getEvent().getId());
            linkUserParams.put("id", userEvent.getId());
            jdbcTemplate.update(UPDATE_USER_EVENT_LINKED_TO_EVENT_QUERY, linkUserParams);
            userEvent.setId(Objects.requireNonNull(holder.getKey()).longValue());
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while saving the user event. Please try again.");
        }
        return userEvent;
    }

    @Override
    public UserEvent read(Long id) {
        log.info("Fetching Event by Id: {}", id);
        try {
            String sql = "SELECT * FROM UserEvents WHERE id = ?";
            return jdbcTemplate.queryForObject(sql,
                    Collections.singletonMap("id", id), new UserEventRowMapper());
        } catch (EmptyResultDataAccessException exception) {
            log.warn("No event found with ID: {}", id);
            return null; // Return null when no user is found
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while fetching user with ID " + id);
        }
    }

    @Override
    public UserEvent update(UserEvent userEvent) {
        log.info("Updating UserEvent: {}", userEvent);
        try {
            SqlParameterSource parameters = getSqlParameterSource(userEvent);
            String sql = "UPDATE UserEvents SET user_id = :userId, event_id = :eventId, device = :device, " +
                    "ip_address = :ipAddress, created_at = :createdAt WHERE id = :id";
            jdbcTemplate.update(sql, parameters);
            return userEvent;
        } catch (EmptyResultDataAccessException exception) {
            return null;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while updating the user event. Please try again.");
        }
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting UserEvent by Id: {}", id);
        try {
            String sql = "DELETE FROM UserEvents WHERE id = :id";
            jdbcTemplate.update(sql, Map.of("id", id));
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while deleting the user event. Please try again.");
        }
    }

    private SqlParameterSource getSqlParameterSource(UserEvent userEvent) {
        return new MapSqlParameterSource()
                .addValue("id", userEvent.getId())
                .addValue("userId", userEvent.getUser().getId())
                .addValue("eventId", userEvent.getEvent().getId())
                .addValue("device", userEvent.getDevice())
                .addValue("ipAddress", userEvent.getIpAddress())
                .addValue("createdAt", userEvent.getCreatedAt());
    }
}