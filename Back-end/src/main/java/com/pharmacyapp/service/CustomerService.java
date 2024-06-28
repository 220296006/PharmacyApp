package com.pharmacyapp.service;

import com.pharmacyapp.model.Customer;

import java.util.Collection;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/07
 * @Time : 18:53
 **/
public interface CustomerService {

    void createCustomer(Customer customer);

     Collection<Customer> getAllCustomers(String name, int page, int pageSize);

     Customer findCustomerById(Long id);

     void updateCustomer(Customer customer);

     boolean deleteCustomer(Long id);
    Integer getCustomerCount();


}
