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
import za.ac.cput.model.Customer;
import za.ac.cput.model.Prescription;
import za.ac.cput.repository.CustomerRepository;
import za.ac.cput.repository.PrescriptionRepository;
import za.ac.cput.rowmapper.PrescriptionRowMapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static za.ac.cput.query.PrescriptionQuery.*;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/11
 * @Time : 20:22
 **/

@Repository
@RequiredArgsConstructor
@Slf4j
public class PrescriptionRepositoryImp implements PrescriptionRepository<Prescription> {
        private final NamedParameterJdbcTemplate jdbc;
        private final CustomerRepository<Customer> customerRepository;
     @Override
    public Prescription save(Prescription prescription) {
        log.info("Saving a Prescription: {}", prescription);
        // Check if the associated customer exists
        Customer customer = customerRepository.read(prescription.getCustomer().getId());
        if (customer == null) {
            throw new ApiException("Associated customer not found. Please provide a valid customer ID");
        }
        // Save prescription
        try {
            KeyHolder holder = new GeneratedKeyHolder();
            SqlParameterSource parameters = getSqlParameterSource(prescription);
            jdbc.update(INSERT_PRESCRIPTION_QUERY, parameters, holder);
            prescription.setId(Objects.requireNonNull(holder.getKey()).longValue());
        // Link the prescription with customer
            Map<String, Object> linkCustomerParams = new HashMap<>();
            linkCustomerParams.put("id", prescription.getId());
            linkCustomerParams.put("customerId", customer.getId());
            jdbc.update(UPDATE_PRESCRIPTION_QUERY_LINKED_TO_CUSTOMER, linkCustomerParams);
            return prescription;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while saving the prescription. Please try again.");
        }
    }
  @Override
public Collection<Prescription> list(String name, int page, int pageSize) {
    log.info("Fetching a list of prescriptions with name: {}, page: {}, pageSize: {}", name, page, pageSize);
        try {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("size", pageSize)
                .addValue("page", (page - 1) * pageSize);
        return jdbc.query(FETCH_ALL_PRESCRIPTIONS_QUERY, parameters, new PrescriptionRowMapper());
       } catch (Exception exception) {
        log.error(exception.getMessage());
        throw new ApiException("An error occurred while fetching the list of prescriptions. Please try again.");
    }
}

     @Override
    public Prescription read(Long id) {
        log.info("Fetching a Prescription with ID: {}", id);
        try {
            SqlParameterSource parameters = new MapSqlParameterSource("id", id);
            return jdbc.queryForObject(SELECT_PRESCRIPTION_BY_ID_QUERY, parameters, new PrescriptionRowMapper());
        } catch (EmptyResultDataAccessException exception) {
            return null;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while fetching the prescription. Please try again.");
        }
    }

    @Override
    public Prescription update(Prescription prescription) {
        log.info("Updating a Prescription: {}", prescription);
        // Check if the associated customer exists
        Customer customer = customerRepository.read(prescription.getCustomer().getId());
        if (customer == null) {
            throw new ApiException("Associated customer not found. Please provide a valid customer ID");
        }
        // Update prescription
        try {
            SqlParameterSource parameters = getSqlParameterSource(prescription);
            jdbc.update(UPDATE_PRESCRIPTION_QUERY, parameters);
            return prescription;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while updating the prescription. Please try again.");
        }
    }

    @Override
    public boolean delete(Long id) {
        log.info("Deleting a Prescription with ID: {}", id);
        try {
            SqlParameterSource parameters = new MapSqlParameterSource("id", id);
            jdbc.update(DELETE_PRESCRIPTION_QUERY, parameters);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while deleting the prescription. Please try again.");
        }
        return true;
    }
     private SqlParameterSource getSqlParameterSource(Prescription prescription) {
        return new MapSqlParameterSource()
                .addValue("id", prescription.getId())
                .addValue("customerId", prescription.getCustomer().getId())
                .addValue("doctorName", prescription.getDoctorName())
                .addValue("doctorAddress", prescription.getDoctorAddress())
                .addValue("issueDate", prescription.getIssue_date());
    }
}
