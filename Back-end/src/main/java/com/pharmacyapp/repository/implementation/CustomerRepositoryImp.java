package com.pharmacyapp.repository.implementation;

import com.pharmacyapp.exception.ApiException;
import com.pharmacyapp.model.Customer;
import com.pharmacyapp.model.User;
import com.pharmacyapp.query.CustomerQuery;
import com.pharmacyapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import com.pharmacyapp.repository.CustomerRepository;
import com.pharmacyapp.rowmapper.CustomerRowMapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/07
 * @Time : 18:09
 **/
@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomerRepositoryImp implements CustomerRepository<Customer> {
    private final NamedParameterJdbcTemplate jdbc;
    private final UserRepository<User> userRepository;

    @Override
    public Customer save(Customer customer) {
    log.info("Saving A Customer");
    User user = userRepository.read(customer.getUser().getId());
    if (user == null || user.getId() == null) {
        throw new ApiException("Associated user not found. Please provide a valid user ID");
    }
    try {
         KeyHolder holder = new GeneratedKeyHolder();
         SqlParameterSource parameters = getSqlParameterSource(customer);
         jdbc.update(CustomerQuery.INSERT_CUSTOMER_QUERY, parameters, holder );
         customer.setId(Objects.requireNonNull(holder.getKey()).longValue());
         Map<String, Object> linkUserParams = new HashMap<>();
         linkUserParams.put("user_id", user.getId());
         linkUserParams.put("id", customer.getId());
         jdbc.update(CustomerQuery.UPDATE_CUSTOMER_LINKED_TO_USER_QUERY, linkUserParams);
         return customer;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while saving the customer. Please try again.");
        }
    }

@Override
public Collection<Customer> list(String name, int page, int pageSize) {
         log.info("Fetch Customers");
    try {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("size", pageSize)
                .addValue("page", (page - 1) * pageSize);
        return jdbc.query(CustomerQuery.FETCH_ALL_CUSTOMERS_QUERY, parameters, new CustomerRowMapper());
    } catch (Exception exception) {
        log.error(exception.getMessage());
        throw new ApiException("An error occurred while retrieving the list of customers. Please try again.");
    }
}

@Override
public Customer read(Long id) {
        log.info("Fetch Customer by Id {}", id);
    try {
        return jdbc.queryForObject(CustomerQuery.FETCH_CUSTOMER_BY_ID_QUERY,Map.of("id", id), new CustomerRowMapper());
    } catch (EmptyResultDataAccessException exception) {
        return null;
    } catch (Exception exception) {
        log.error(exception.getMessage());
        throw new ApiException("An error occurred while retrieving the customer. Please try again.");
    }
}

@Override
public Customer update(Customer customer) {
        log.info("Updating Customer");
          User user = userRepository.read(customer.getUser().getId());
        if (user == null) {
            throw new ApiException("Associated user not found. Please provide a valid customer ID");
        }
    try {
        SqlParameterSource parameters = getSqlParameterSource(customer);
        jdbc.update(CustomerQuery.UPDATE_CUSTOMER_QUERY, parameters);
        return customer;
    } catch (Exception exception) {
        log.error(exception.getMessage());
        throw new ApiException("An error occurred while updating the customer. Please try again.");
    }
}
   @Override
public void delete(Long id) {
           log.info("Deleting Customer by Id");
    try {
        jdbc.update(CustomerQuery.DELETE_CUSTOMER_QUERY, Map.of("id", id));
    } catch (Exception exception) {
        log.error(exception.getMessage());
        throw new ApiException("An error occurred while deleting the customer. Please try again.");
    }
}

    @Override
    public Integer countCustomers() {
        log.info("Fetching Total Customers");
        try {
            return jdbc.queryForObject(CustomerQuery.SELECT_CUSTOMER_COUNT_QUERY, new HashMap<>(), Integer.class);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while fetching customer count. Please try again.");
        }
    }

    private SqlParameterSource getSqlParameterSource(Customer customer) {
        return  new MapSqlParameterSource()
                .addValue("id", customer.getId())
                .addValue("address", customer.getUser().getAddress())
                .addValue("imageUrl", customer.getUser().getImageUrl())
                .addValue("city",customer.getCity())
                .addValue("state", customer.getState())
                .addValue("zipCode", customer.getZipCode())
                .addValue("user_id", customer.getUser().getId())
                .addValue("firstName", customer.getUser().getFirstName());
    }
}
