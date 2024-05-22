package za.ac.cput.repository.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import za.ac.cput.exception.ApiException;
import za.ac.cput.model.Event;
import za.ac.cput.repository.EventRepository;
import za.ac.cput.rowmapper.EventRowMapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author : Thabiso Matsaba
 * @Project : Back-end
 * @Date : 2024/05/22
 * @Time : 00:19
 **/
@RequiredArgsConstructor
@Repository
@Slf4j
public class EventRepositoryImpl implements EventRepository<Event> {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Event> findAll() {
        log.info("Fetching All Events");
        try {
        String sql = "SELECT * FROM Events";
        return jdbcTemplate.query(sql,  new EventRowMapper());
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("No events found. Please try again.");
        }
    }


    @Override
    public Event findById(Long id) {
        log.info("Fetching Event by Id: {}", id);
        try {
        String sql = "SELECT * FROM Events WHERE id = ?";
        return jdbcTemplate.queryForObject(sql,
                Collections.singletonMap("id", id), new EventRowMapper());
        } catch (EmptyResultDataAccessException exception) {
            log.warn("No user found with ID: {}", id);
            return null; // Return null when no user is found
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while fetching user with ID " + id);
        }
    }
    @Override
    public Event save(Event event) {
        log.info("Saving an a Event {}", event);
        try {
        KeyHolder holder = new GeneratedKeyHolder();
            SqlParameterSource parameters = getSqlParameterSource(event);
        String sql = "INSERT INTO Events (type, description) VALUES (:type, :description)";
            jdbcTemplate.update(sql, parameters, holder);
            event.setId(Objects.requireNonNull(holder.getKey()).longValue());
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again.");
        }
        return event;
    }

    @Override
    public Event update(Event event) {
        log.info("Updating event: {}", event);
        try {
            SqlParameterSource parameters = getSqlParameterSource(event);
            String sql = "UPDATE Events SET type = ?, description = ? WHERE id = ?";
        jdbcTemplate.update(sql, parameters);
        return event;
        } catch (EmptyResultDataAccessException exception) {
            return null;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while updating the user. Please try again.");
        }
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting user by Id");
        try {
        String sql = "DELETE FROM Events WHERE id = ?";
        jdbcTemplate.update(sql,Map.of("id", id));
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while deleting the event. Please try again.");
        }
    }


    private SqlParameterSource getSqlParameterSource(Event event) {
        return new MapSqlParameterSource()
                .addValue("id", event.getId())
                .addValue("type", event.getType())
                .addValue("description", event.getDescription());
    }
}
