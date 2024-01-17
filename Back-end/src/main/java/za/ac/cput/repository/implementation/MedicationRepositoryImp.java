package za.ac.cput.repository.implementation;

import jakarta.persistence.criteria.CriteriaBuilder;
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
import za.ac.cput.model.Prescription;
import za.ac.cput.repository.InventoryRepository;
import za.ac.cput.repository.MedicationRepository;
import za.ac.cput.repository.PrescriptionRepository;
import za.ac.cput.rowmapper.MedicationRowMapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static za.ac.cput.query.MedicationQuery.*;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/12
 * @Time : 00:58
 **/
@Repository
@RequiredArgsConstructor
@Slf4j
public class MedicationRepositoryImp implements MedicationRepository<Medication> {
   private final NamedParameterJdbcTemplate jdbc;
   private final PrescriptionRepository<Prescription> prescriptionRepository;
    @Override
    public Medication save(Medication medication) {
    log.info("Save a Medication");
    // Check if the associated prescription exists
    Prescription prescription = prescriptionRepository.read(medication.getPrescription().getId());
    if (prescription == null) {
        throw new ApiException("Associated prescription not found. Please provide a valid prescription ID");
    }
        // Check if the provided medication name is in the available medications list
//        String medicationName = medication.getName();
//        if (!Inventory.inventoryRepository.getAvailableMedications().contains(medicationName)) {
//            throw new ApiException("Invalid medication name. Please provide a valid medication name from the inventory.");
//        }

        // Save Medication
    try {
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource parameters = getSqlParameterSource(medication);
        jdbc.update(INSERT_MEDICATION_QUERY, parameters, holder);
        medication.setId(Objects.requireNonNull(holder.getKey()).longValue());
        // Link the medication with the prescription
        Map<String, Object> linkParams = new HashMap<>();
        linkParams.put("prescription_id", prescription.getId());
        linkParams.put("id", medication.getId());
        jdbc.update(UPDATE_PRESCRIPTION_LINKED_TO_MEDICATION_QUERY, linkParams);
        return medication;
    } catch (Exception exception) {
        log.error(exception.getMessage());
        throw new ApiException("An error occurred while saving the medication. Please try again.");
    }
}

    @Override
    public Collection<Medication> list(String name, int page, int pageSize) {
        log.info("Fetching a list of medications with name: {}, page: {}, pageSize: {}", name, page, pageSize);
        try{
        MapSqlParameterSource parameters = new MapSqlParameterSource()
               .addValue("size", pageSize)
               .addValue("page", (page - 1) * pageSize);
        return jdbc.query(FETCH_ALL_MEDICATIONS_QUERY, parameters, new MedicationRowMapper());
        } catch (Exception exception) {
        log.error(exception.getMessage());
        throw new ApiException("An error occurred while fetching the list of medications. Please try again.");
    }
}
    @Override
    public Medication read(Long id) {
        log.info("Fetching a Medication with ID: {}", id);
        try{
        SqlParameterSource parameters = new MapSqlParameterSource("id", id);
         return jdbc.queryForObject(SELECT_MEDICATION_BY_ID_QUERY, parameters, new MedicationRowMapper());
        } catch (EmptyResultDataAccessException exception) {
            return null;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while fetching the prescription. Please try again.");
        }
    }

    @Override
    public Medication update(Medication medication) {
       log.info("Updating a Medication: {}", medication);
        // Check if the associated prescription exists
        Prescription prescription = prescriptionRepository.read(medication.getPrescription().getId());
    if (prescription == null) {
        throw new ApiException("Associated prescription not found. Please provide a valid prescription ID");
    }
    try{
        SqlParameterSource parameters = getSqlParameterSource(medication);
        jdbc.update(UPDATE_MEDICATION_QUERY, parameters);
        return medication;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while updating the prescription. Please try again.");
        }
    }

    @Override
    public boolean delete(Long id) {
        log.info("Deleting a Medication with ID: {}", id);
        try{
        MapSqlParameterSource parameters = new MapSqlParameterSource("id", id);
        jdbc.update(DELETE_MEDICATION_QUERY, parameters);
         } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while deleting the prescription. Please try again.");
        }
         return true;
    }

    private SqlParameterSource getSqlParameterSource(Medication medication) {
    return new MapSqlParameterSource()
            .addValue("id", medication.getId())
            .addValue("prescription_id", medication.getPrescription().getId())
            .addValue("name", medication.getName())
            .addValue("dosage", medication.getDosage())
            .addValue("frequency", medication.getFrequency());
}

}
