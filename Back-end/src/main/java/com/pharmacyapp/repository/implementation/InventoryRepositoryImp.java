package com.pharmacyapp.repository.implementation;

import com.pharmacyapp.exception.ApiException;
import com.pharmacyapp.model.Inventory;
import com.pharmacyapp.model.Medication;
import com.pharmacyapp.query.InventoryQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import com.pharmacyapp.repository.InventoryRepository;
import com.pharmacyapp.repository.MedicationRepository;
import com.pharmacyapp.rowmapper.InventoryRowMapper;
import com.pharmacyapp.rowmapper.MedicationRowMapper;

import java.util.*;

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
        log.info("Save an Inventory: {}", inventory);
        Medication medication = medicationRepository.read(inventory.getMedication().getId());
        if (medication == null || medication.getId() == null) {
        throw new ApiException("Associated medication not found. Please provide a valid medication id");
    }
        try {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource parameters = getSqlParameterSource(inventory);
        jdbc.update(InventoryQuery.INSERT_INVENTORY_QUERY, parameters, keyHolder);
        inventory.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        Map<String, Object> linkParams = new HashMap<>();
        linkParams.put("id", medication.getId());
        linkParams.put("medication_id", medication.getId());
        jdbc.update(InventoryQuery.UPDATE_INVENTORY_LINKED_MEDICATION, linkParams);
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
        return jdbc.query(InventoryQuery.SELECT_ALL_INVENTORY_QUERY, parameters, new InventoryRowMapper());
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
        return jdbc.queryForObject(InventoryQuery.SELECT_INVENTORY_BY_ID_QUERY, parameters, new InventoryRowMapper());
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
            jdbc.update(InventoryQuery.UPDATE_INVENTORY_QUERY, parameters);
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
        jdbc.update(InventoryQuery.DELETE_INVENTORY_QUERY, parameters);
         } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while deleting the invoice. Please try again.");
        }
    }

    @Override
    public List<Medication> getAvailableMedications() {
        try {
            return jdbc.query(InventoryQuery.GET_AVAILABLE_MEDICATIONS_QUERY, new MedicationRowMapper());
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while fetching available medications. Please try again.");
        }
    }

    private  SqlParameterSource getSqlParameterSource(Inventory inventory) {
        return new MapSqlParameterSource()
                .addValue("name", inventory.getName())
                .addValue("description", inventory.getDescription())
                .addValue("quantity", inventory.getQuantity())
                .addValue("price", inventory.getPrice())
                .addValue("medication_id", inventory.getMedication().getId())
                .addValue("id", inventory.getId())
                .addValue("created_at", inventory.getCreated_at())
                .addValue("updated_at", inventory.getUpdated_at());
    }
}

