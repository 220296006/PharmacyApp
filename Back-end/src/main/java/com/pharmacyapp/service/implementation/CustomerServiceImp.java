package com.pharmacyapp.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.pharmacyapp.model.Customer;
import com.pharmacyapp.repository.CustomerRepository;
import com.pharmacyapp.service.CustomerService;

import java.util.Collection;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/07
 * @Time : 19:02
 **/
@Service
@RequiredArgsConstructor
public class CustomerServiceImp implements CustomerService {

    private final CustomerRepository<Customer> customerRepository;
    @Override
    public void createCustomer(Customer customer) {

        customerRepository.save(customer);
    }

    @Override
    public Collection<Customer> getAllCustomers(String name, int page, int pageSize) {
        return customerRepository.list("customers", 1, 50);
    }

    @Override
    public Customer findCustomerById(Long id) {return customerRepository.read(id);
    }

    @Override
    public void updateCustomer(Customer customer) {
        customerRepository.update(customer);
    }

    @Override
    public boolean deleteCustomer(Long id) {
        customerRepository.delete(id);
        return true;
    }

    @Override
    public Integer getCustomerCount() {
        return customerRepository.countCustomers();
    }


}
