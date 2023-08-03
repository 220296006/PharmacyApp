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
import za.ac.cput.model.Inventory;
import za.ac.cput.model.Medication;
import za.ac.cput.repository.InventoryRepository;
import za.ac.cput.repository.MedicationRepository;
import za.ac.cput.rowmapper.InventoryRowMapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static za.ac.cput.query.InventoryQuery.*;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/13
 * @Time : 14:55
 **/
@Repository
@RequiredArgsConstructor
@Slf4j
public class InventoryRepositoryImp implements InventoryRepository<Inventory> {
    private final NamedParameterJdbcTemplate jdbc;
    private final MedicationRepository<Medication> medicationRepository;
    @Override
    public Inventory save(Inventory inventory) {
        log.info("Saving an Inventory: {}", inventory);
        Medication medication = medicationRepository.read(inventory.getMedication().getId());
        if (medication == null || medication.getId() == null) {
        throw new ApiException("Associated medication not found. Please provide a valid user ID");
    }
        try {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource parameters = getSqlParameterSource(inventory);
        jdbc.update(INSERT_INVENTORY_QUERY, parameters, keyHolder);
        inventory.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        Map<String, Object> linkParams = new HashMap<>();
        linkParams.put("id", medication.getId());
        linkParams.put("medication_id", medication.getId());
        jdbc.update(UPDATE_INVENTORY_LINKED_MEDICATION, linkParams);
        return inventory;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while saving the invoice. Please try again.");
        }
    }

    @Override
    public Collection<Inventory> list(String name, int page, int pageSize) {
        log.info("Fetch Inventory");
    try {
         SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("size", pageSize)
                .addValue("page", (page - 1) * pageSize);
        return jdbc.query(SELECT_ALL_INVENTORY_QUERY, parameters, new InventoryRowMapper());
        } catch (Exception exception) {
        log.error(exception.getMessage());
        throw new ApiException("An error occurred while retrieving the list of inventory. Please try again.");
    }
    }

    @Override
    public Inventory read(Long id) {
         log.info("Fetch Inventory By Id");
        try {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", id);
        return jdbc.queryForObject(SELECT_INVENTORY_BY_ID_QUERY, parameters, new InventoryRowMapper());
          } catch (EmptyResultDataAccessException exception) {
            return null;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while retrieving the inventory. Please try again.");
        }
    }

    @Override
    public Inventory update(Inventory inventory) {
       log.info("Updating an Inventory: {}", inventory);
        try {
            SqlParameterSource parameters = getSqlParameterSource(inventory);
            jdbc.update(UPDATE_INVENTORY_QUERY, parameters);
            return inventory;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while updating the invoice. Please try again.");
        }
    }
    @Override
    public void delete(Long id) {
        log.info("Deleting an Inventory with ID: {}", id);
        try {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", id);
        jdbc.update(DELETE_INVENTORY_QUERY, parameters);
         } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while deleting the invoice. Please try again.");
        }
    }
    private  SqlParameterSource getSqlParameterSource(Inventory inventory) {
        return new MapSqlParameterSource()
                .addValue("name", inventory.getName())
                .addValue("description", inventory.getDescription())
                .addValue("quantity", inventory.getQuantity())
                .addValue("price", inventory.getPrice())
                .addValue("medication_id", inventory.getMedication().getId())
                .addValue("id", inventory.getId());
    }
}

