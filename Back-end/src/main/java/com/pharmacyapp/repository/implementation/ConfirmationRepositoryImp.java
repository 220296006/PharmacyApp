package com.pharmacyapp.repository.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import com.pharmacyapp.exception.ApiException;
import com.pharmacyapp.model.Confirmation;
import com.pharmacyapp.query.ConfirmationQuery;
import com.pharmacyapp.repository.ConfirmationRepository;
import com.pharmacyapp.rowmapper.ConfirmationRowMapper;

import java.util.Collection;
import java.util.Objects;

import static com.pharmacyapp.query.ConfirmationQuery.FETCH_ALL_CONFIRMATIONS_QUERY;
import static com.pharmacyapp.query.ConfirmationQuery.INSERT_CONFIRMATION_QUERY;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/13
 * @Time : 20:48
 **/
@RequiredArgsConstructor
@Repository
@Slf4j
public class ConfirmationRepositoryImp implements ConfirmationRepository<Confirmation> {

    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public Confirmation findTokenByUserId(String userId) {
        log.info("Finding token by User ID: {}", userId);
        String query = ConfirmationQuery.FIND_TOKEN_BY_USER_ID;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        try {
            return jdbc.queryForObject(query, params, Confirmation.class);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }


    @Override
    public Confirmation save(Confirmation confirmation) {
        log.info("Saving a Confirmation: {}", confirmation);
        try {
            KeyHolder holder = new GeneratedKeyHolder();
            SqlParameterSource parameters = getSqlParameterSource(confirmation);
            jdbc.update(INSERT_CONFIRMATION_QUERY, parameters, holder);
            confirmation.setId(Objects.requireNonNull(holder.getKey()).longValue());
            return confirmation;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while saving the confirmation. Please try again.");
        }
    }

    @Override
    public Collection<Confirmation> list(String name, int page, int pageSize) {
               log.info("Fetch Confirmations");
    try {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("size", pageSize)
                .addValue("page", (page - 1) * pageSize);
        return jdbc.query(FETCH_ALL_CONFIRMATIONS_QUERY, parameters, new ConfirmationRowMapper());
    } catch (Exception exception) {
        log.error(exception.getMessage());
        throw new ApiException("An error occurred while retrieving the list of confirmations. Please try again.");
    }
    }

    @Override
    public Confirmation read(Long id) {
        log.info("Fetching a Confirmation with ID: {}", id);
        try {
            SqlParameterSource parameters = new MapSqlParameterSource("id", id);
            return jdbc.queryForObject(ConfirmationQuery.SELECT_CONFIRMATION_BY_ID_QUERY, parameters, new ConfirmationRowMapper());
        } catch (EmptyResultDataAccessException exception) {
            return null;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while fetching the confirmation. Please try again.");
        }
    }
    @Override
    public Confirmation update(Confirmation confirmation) {
        log.info("Updating a Confirmation: {}", confirmation);
        try {
            SqlParameterSource parameters = getSqlParameterSource(confirmation);
            jdbc.update(ConfirmationQuery.UPDATE_CONFIRMATION_QUERY, parameters);
            return confirmation;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while updating the confirmation. Please try again.");
        }
    }
    @Override
    public void delete(Long id) {
        log.info("Deleting a Confirmation with ID: {}", id);
        try {
            SqlParameterSource parameters = new MapSqlParameterSource("id", id);
            jdbc.update(ConfirmationQuery.DELETE_CONFIRMATION_QUERY, parameters);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while deleting the confirmation. Please try again.");
        }
    }

    private SqlParameterSource getSqlParameterSource(Confirmation confirmation) {
        return new MapSqlParameterSource()
                .addValue("id", confirmation.getId())
                .addValue("token", confirmation.getToken())
                .addValue("createdDate", confirmation.getCreatedDate())
                .addValue("userId", confirmation.getUser().getId());
    }
}

