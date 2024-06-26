package com.pharmacyapp.repository.implementation;

import com.pharmacyapp.exception.ApiException;
import com.pharmacyapp.model.Customer;
import com.pharmacyapp.model.Invoice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import com.pharmacyapp.query.InvoiceQuery;
import com.pharmacyapp.repository.CustomerRepository;
import com.pharmacyapp.repository.InvoiceRepository;
import com.pharmacyapp.rowmapper.InvoiceRowMapper;

import java.math.BigInteger;
import java.util.*;

import static com.pharmacyapp.query.InvoiceQuery.*;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/12
 * @Time : 17:24
 **/
@Repository
@RequiredArgsConstructor
@Slf4j
public class InvoiceRepositoryImp implements InvoiceRepository<Invoice> {

    private final NamedParameterJdbcTemplate jdbc;
    private final CustomerRepository<Customer> customerRepository;


    @Override
    public Invoice save(Invoice invoice) {
        log.info("Saving an Invoice: {}", invoice);
        // Check if the associated customer exists
        Customer customer = customerRepository.read(invoice.getCustomer().getId());
        log.info("Customer ID: {}", invoice.getCustomer().getId());

        // Check if the associated customer exists
        if (invoice.getCustomer() == null || invoice.getCustomer().getId() == null) {
            throw new ApiException("Associated customer not found. Please provide a valid customer ID");
        }
        // Log the payment status before inserting
        log.info("Payment status: {}", invoice.getPaymentStatus());
        try {
            KeyHolder holder = new GeneratedKeyHolder();
            SqlParameterSource parameters = getSqlParameterSource(invoice);
            log.info("Executing SQL: {} with parameters: {}", INSERT_INVOICE_QUERY, parameters);
            jdbc.update(INSERT_INVOICE_QUERY, parameters, holder);
            invoice.setId(Objects.requireNonNull(holder.getKey()).longValue());
            Map<String, Object> linkCustomerParams = new HashMap<>();
            linkCustomerParams.put("id", invoice.getId());
            linkCustomerParams.put("customerId", customer.getId());
            jdbc.update(UPDATE_CUSTOMER_LINKED_TO_INVOICE_QUERY, linkCustomerParams);
            return invoice;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while saving the invoice. Please try again.");
        }
    }

    @Override
    public Collection<Invoice> list(String name, int page, int pageSize) {
   log.info("Fetch Invoices");
    try {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("size", pageSize)
                .addValue("page", (page - 1) * pageSize);
        return jdbc.query(FETCH_ALL_INVOICES_QUERY, parameters, new InvoiceRowMapper());
    } catch (Exception exception) {
        log.error(exception.getMessage());
        throw new ApiException("An error occurred while retrieving the list of invoices. Please try again.");
    }
    }

    @Override
    public Invoice read(Long id) {
        log.info("Fetch Invoice By Id");
        try {
            SqlParameterSource parameters = new MapSqlParameterSource("id", id);
            return jdbc.queryForObject(SELECT_INVOICE_BY_ID_QUERY, parameters, new InvoiceRowMapper());
        } catch (EmptyResultDataAccessException exception) {
            return null;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while retrieving the invoice. Please try again.");
        }
    }

    @Override
    public Invoice update(Invoice invoice) {
        log.info("Updating an Invoice: {}", invoice);
        // Check if the associated customer exists
        Customer customer = customerRepository.read(invoice.getCustomer().getId());
        if (customer == null) {
            throw new ApiException("Associated customer not found. Please provide a valid customer ID");
        }
        try {
            SqlParameterSource parameters = getSqlParameterSource(invoice);
            jdbc.update(UPDATE_INVOICE_QUERY, parameters);
            return invoice;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while updating the invoice. Please try again.");
        }
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting an Invoice with ID: {}", id);
        try {
            SqlParameterSource parameters = new MapSqlParameterSource("id", id);
            jdbc.update(InvoiceQuery.DELETE_INVOICE_QUERY, parameters);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while deleting the invoice. Please try again.");
        }
    }

    @Override
    public List<Invoice> findInvoiceByCustomerId(Long customerId) {
        log.info("Fetching a Invoice by Customer ID: {}", customerId);
        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("customerId", customerId);
            return jdbc.query(SELECT_INVOICE_BY_CUSTOMER_ID_QUERY, paramMap, new InvoiceRowMapper());
        } catch (EmptyResultDataAccessException exception) {
            return null;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while fetching the invoices. Please try again.");
        }
    }

    @Override
    public Integer countInvoices() {
        log.info("Fetching Total Invoices");
        try {
            return jdbc.queryForObject(SELECT_INVOICE_COUNT_QUERY, new HashMap<>(), Integer.class);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while fetching invoice count. Please try again.");
        }
    }

    @Override
    public BigInteger getTotalBilledAmount() {
        log.info("Fetching Total Billed Amount");
        try {
            return jdbc.queryForObject(SELECT_TOTAL_BILLED_AMOUNT_QUERY, new HashMap<>(), BigInteger.class);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while fetching total billed amount. Please try again.");
        }
    }

    private SqlParameterSource getSqlParameterSource(Invoice invoice) {
        return new MapSqlParameterSource()
                .addValue("id", invoice.getId())
                .addValue("customerId", invoice.getCustomer().getId())
                .addValue("amount", invoice.getAmount())
                .addValue("dueDate", invoice.getDueDate())
                .addValue("paymentStatus", invoice.getPaymentStatus().name());
    }
}
