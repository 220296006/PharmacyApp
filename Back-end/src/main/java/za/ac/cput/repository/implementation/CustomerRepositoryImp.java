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
import za.ac.cput.model.User;
import za.ac.cput.repository.CustomerRepository;
import za.ac.cput.repository.UserRepository;
import za.ac.cput.rowmapper.CustomerRowMapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static za.ac.cput.query.CustomerQuery.*;

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
        //Check if the user exists
    User user = userRepository.read(customer.getUser().getId());
    if (user == null || user.getId() == null) {
        throw new ApiException("Associated user not found. Please provide a valid user ID");
    }
        //Save customer
        try {
            KeyHolder holder = new GeneratedKeyHolder();
            SqlParameterSource parameters = getSqlParameterSource(customer);
            jdbc.update(INSERT_CUSTOMER_QUERY, parameters, holder );
            customer.setId(Objects.requireNonNull(holder.getKey()).longValue());

        // Link the customer with the user
            Map<String, Object> linkUserParams = new HashMap<>();
            linkUserParams.put("user_id", user.getId());
            linkUserParams.put("id", customer.getId());
            jdbc.update(UPDATE_CUSTOMER_LINKED_TO_USER_QUERY, linkUserParams);
            customer.setUser(user);
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
        String query = FETCH_ALL_CUSTOMERS_QUERY + " LIMIT :size OFFSET :page";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("size", pageSize)
                .addValue("page", (page - 1) * pageSize);
        return jdbc.query(query, parameters, new CustomerRowMapper());
    } catch (Exception exception) {
        log.error(exception.getMessage());
        throw new ApiException("An error occurred while retrieving the list of customers. Please try again.");
    }
}

@Override
public Customer read(Long id) {
        log.info("Fetch Customer by Id");
    try {
        return jdbc.queryForObject(FETCH_CUSTOMER_BY_ID_QUERY,Map.of("id", id), (resultSet, rowNum) -> {
            Customer customer = new Customer();
            customer.setId(resultSet.getLong("id"));
            customer.setAddress(resultSet.getString("address"));
            customer.setCity(resultSet.getString("city"));
            customer.setState(resultSet.getString("state"));
            customer.setZipCode(resultSet.getString("zip_code"));
            return customer;
        });
    } catch (EmptyResultDataAccessException exception) {
        return null; // Customer with the given ID not found
    } catch (Exception exception) {
        log.error(exception.getMessage());
        throw new ApiException("An error occurred while retrieving the customer. Please try again.");
    }
}
@Override
public Customer update(Customer customer) {
        log.info("Updating Customer");
    try {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", customer.getId())
                .addValue("address", customer.getAddress())
                .addValue("city", customer.getCity())
                .addValue("state", customer.getState())
                .addValue("zipCode", customer.getZipCode());
        jdbc.update(UPDATE_CUSTOMER_QUERY, parameters);
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
        jdbc.update(DELETE_CUSTOMER_QUERY, Map.of("id", id));
    } catch (Exception exception) {
        log.error(exception.getMessage());
        throw new ApiException("An error occurred while deleting the customer. Please try again.");
    }
}

    private SqlParameterSource getSqlParameterSource(Customer customer) {
        return  new MapSqlParameterSource()
                .addValue("address", customer.getAddress())
                .addValue("city",customer.getCity())
                .addValue("state", customer.getState())
                .addValue("zipCode", customer.getZipCode())
                .addValue("user_id", customer.getUser().getId());
    }
}
